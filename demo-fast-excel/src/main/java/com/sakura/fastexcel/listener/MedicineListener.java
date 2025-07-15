package com.sakura.fastexcel.listener;

import cn.hutool.json.JSONUtil;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import com.sakura.fastexcel.domain.Medicine;
import com.sakura.fastexcel.mapper.MedicineMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MedicineListener implements ReadListener<Medicine> {

    @Autowired
    private MedicineMapper medicineMapper;
    private static final int BATCH_SIZE = 20;
    // 线程安全问题
    private final List<Medicine> medicineList = new ArrayList<>();

    @Override
    public void invoke(Medicine data, AnalysisContext context) {
        medicineList.add(data);

        if (medicineList.size() >= BATCH_SIZE) {
            batchInsert();
            medicineList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!medicineList.isEmpty()) {
            batchInsert();
        }
    }

    private void batchInsert() {
        log.info("触发批量导入");
        medicineMapper.insertBatchSelective(medicineList);
    }
}