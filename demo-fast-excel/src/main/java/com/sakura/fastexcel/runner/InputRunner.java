package com.sakura.fastexcel.runner;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.idev.excel.FastExcel;
import com.sakura.fastexcel.domain.DrugFullInfo;
import com.sakura.fastexcel.domain.Medicine;
import com.sakura.fastexcel.domain.excel.DrugFullInfoExcelModel;
import com.sakura.fastexcel.listener.DrugFullInfoExcelListener;
import com.sakura.fastexcel.listener.MedicineListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;

@Component
@Slf4j
public class InputRunner implements ApplicationRunner {

    private final DrugFullInfoExcelListener drugFullInfoExcelListener;

    public InputRunner(DrugFullInfoExcelListener drugFullInfoExcelListener) {
        this.drugFullInfoExcelListener = drugFullInfoExcelListener;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 从类路径加载资源
        Resource resource = new ClassPathResource("demo.xlsx");
        InputStream inputStream = resource.getStream();
        // Read the first sheet
        FastExcel.read(inputStream, DrugFullInfoExcelModel.class, drugFullInfoExcelListener).sheet().doRead();
        String fileName = "simpleWriteDemo.xlsx";
//        FastExcel.write("./demo.xlsx", DrugFullInfoExcelModel.class)
//                .sheet("药用说明书-demo") // Optionally specify sheet name
//                .doWrite(new ArrayList<>());
    }
}
