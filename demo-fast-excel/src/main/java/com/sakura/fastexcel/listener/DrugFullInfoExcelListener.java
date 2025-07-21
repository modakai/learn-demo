package com.sakura.fastexcel.listener;

import cn.hutool.core.collection.CollUtil;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import com.sakura.fastexcel.domain.DrugFullInfo;
import com.sakura.fastexcel.domain.excel.DrugFullInfoExcelModel;
import com.sakura.fastexcel.mapper.DrugFullInfoMapper;
import com.sakura.fastexcel.util.PerformanceMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class DrugFullInfoExcelListener implements ReadListener<DrugFullInfoExcelModel> {

    @Autowired
    private DrugFullInfoMapper drugFullInfoMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private Executor excelTaskExecutor;

    private static final int BATCH_SIZE = 20;

    // 使用线程安全的集合
    private final List<DrugFullInfo> drugFullInfos = new CopyOnWriteArrayList<>();
    
    // 用于跟踪正在处理的任务数量
    private final AtomicInteger processingTasks = new AtomicInteger(0);
    
    // 用于存储所有异步任务
    private final List<CompletableFuture<Void>> futures = new ArrayList<>();
    
    // 当前处理的Excel文件名
    private String currentExcelFile;

    /**
     * 设置当前处理的Excel文件名
     * @param excelFileName Excel文件名
     */
    public void setCurrentExcelFile(String excelFileName) {
        this.currentExcelFile = excelFileName;
        // 开始性能监控
        PerformanceMonitor.start("Excel导入-" + excelFileName);
    }
    
    @Override
    public void invoke(DrugFullInfoExcelModel data, AnalysisContext context) {
        // 增加处理记录计数
        PerformanceMonitor.incrementCount("Excel导入-" + currentExcelFile, 1);
        
        // 异步处理数据转换
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            DrugFullInfo drugFullInfo = new DrugFullInfo();
            BeanUtils.copyProperties(data, drugFullInfo);
            String code = UUID.randomUUID().toString().replaceAll("-", "");
            drugFullInfo.setInstructionCode(code);
            drugFullInfos.add(drugFullInfo);
            
            // 当达到批处理大小时，触发批量插入
            synchronized (this) {
                if (drugFullInfos.size() >= BATCH_SIZE) {
                    List<DrugFullInfo> batchList = new ArrayList<>(drugFullInfos);
                    drugFullInfos.clear();
                    processingTasks.incrementAndGet();
                    CompletableFuture<Void> insertFuture = CompletableFuture.runAsync(() -> {
                        batchInsert(batchList);
                        processingTasks.decrementAndGet();
                    }, excelTaskExecutor);
                    futures.add(insertFuture);
                }
            }
        }, excelTaskExecutor);
        
        futures.add(future);
    }

    public void batchInsert(List<DrugFullInfo> batchList) {
        if (CollUtil.isEmpty(batchList)) return;

        log.info("触发批量导入，数据量：{}", batchList.size());
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            drugFullInfoMapper.insertBatchSelective(batchList);
            transactionManager.commit(transaction);
            log.info("批量导入完成，数据量：{}", batchList.size());
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            log.error("批量插入执行异常：{}", e.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel文件[{}]读取完成，等待处理任务完成...", currentExcelFile);
        
        // 等待所有正在进行的异步任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // 处理剩余的数据
        if (!drugFullInfos.isEmpty()) {
            List<DrugFullInfo> remainingList = new ArrayList<>(drugFullInfos);
            drugFullInfos.clear();
            batchInsert(remainingList);
        }
        
        // 等待所有处理任务完成
        while (processingTasks.get() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("等待处理任务完成时被中断", e);
            }
        }
        
        // 结束性能监控
        PerformanceMonitor.end("Excel导入-" + currentExcelFile);
        
        log.info("Excel文件[{}]所有数据处理完成", currentExcelFile);
        
        // 清空futures列表，为下一个Excel文件做准备
        futures.clear();
    }
}
