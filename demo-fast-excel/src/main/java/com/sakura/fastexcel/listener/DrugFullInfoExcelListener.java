package com.sakura.fastexcel.listener;

import cn.hutool.core.collection.CollUtil;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import com.sakura.fastexcel.domain.DrugFullInfo;
import com.sakura.fastexcel.domain.excel.DrugFullInfoExcelModel;
import com.sakura.fastexcel.mapper.DrugFullInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class DrugFullInfoExcelListener implements ReadListener<DrugFullInfoExcelModel> {

    @Autowired
    private DrugFullInfoMapper drugFullInfoMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private static final int BATCH_SIZE = 20;

    // 线程安全问题
    private final List<DrugFullInfo> drugFullInfos = new ArrayList<>();

    @Override
    public void invoke(DrugFullInfoExcelModel data, AnalysisContext context) {
        log.info("读取到数据：{}", data);
        DrugFullInfo drugFullInfo = new DrugFullInfo();
        BeanUtils.copyProperties(data, drugFullInfo);
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        drugFullInfo.setInstructionCode(code);
        drugFullInfos.add(drugFullInfo);

        if (drugFullInfos.size() >= BATCH_SIZE) {
            batchInsert();
        }
    }

    public void batchInsert() {
        if (CollUtil.isEmpty(drugFullInfos)) return;

        log.info("触发批量导入");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            drugFullInfoMapper.insertBatchSelective(drugFullInfos);

            transactionManager.commit(transaction);
        }catch (Exception e) {
            transactionManager.rollback(transaction);
            log.error("批量插入执行异常：{}", e.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        batchInsert();
    }
}
