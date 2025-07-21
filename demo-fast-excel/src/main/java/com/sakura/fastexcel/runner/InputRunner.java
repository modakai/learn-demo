package com.sakura.fastexcel.runner;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.idev.excel.FastExcel;
import com.sakura.fastexcel.domain.excel.DrugFullInfoExcelModel;
import com.sakura.fastexcel.listener.DrugFullInfoExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class InputRunner implements ApplicationRunner {

    private final DrugFullInfoExcelListener drugFullInfoExcelListener;
    private final Executor excelTaskExecutor;

    public InputRunner(DrugFullInfoExcelListener drugFullInfoExcelListener, Executor excelTaskExecutor) {
        this.drugFullInfoExcelListener = drugFullInfoExcelListener;
        this.excelTaskExecutor = excelTaskExecutor;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始多线程导入Excel数据");
        
        // 定义要处理的Excel文件列表
        List<String> excelFiles = new ArrayList<>();
        excelFiles.add("demo.xlsx");
        
        // 创建异步任务列表
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // 并行处理多个Excel文件
        for (String excelFile : excelFiles) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    log.info("开始处理Excel文件: {}", excelFile);
                     Resource resource = new ClassPathResource(excelFile);
                     InputStream inputStream = resource.getStream();
                     
                     // 设置当前处理的Excel文件名
                     drugFullInfoExcelListener.setCurrentExcelFile(excelFile);
                     
                     // 使用FastExcel读取并处理Excel文件
                     FastExcel.read(inputStream, DrugFullInfoExcelModel.class, drugFullInfoExcelListener)
                             .sheet()
                             .doRead();
//
                    log.info("Excel文件处理完成: {}", excelFile);
                } catch (Exception e) {
                    log.error("处理Excel文件时发生错误: {}, 错误信息: {}", excelFile, e.getMessage(), e);
                }
            }, excelTaskExecutor);
            
            futures.add(future);
        }
        
        // 等待所有Excel文件处理完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        log.info("所有Excel文件处理完成");
    }
}
