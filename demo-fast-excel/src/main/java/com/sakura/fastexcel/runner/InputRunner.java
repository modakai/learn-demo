package com.sakura.fastexcel.runner;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.idev.excel.FastExcel;
import com.sakura.fastexcel.domain.Medicine;
import com.sakura.fastexcel.listener.MedicineListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class InputRunner implements ApplicationRunner {
    @Autowired
    private MedicineListener medicineListener;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 从类路径加载资源
        Resource resource = new ClassPathResource("药品信息.xlsx");
        InputStream inputStream = resource.getStream();
        // Read the first sheet
        FastExcel.read(inputStream, Medicine.class, medicineListener).sheet().doRead();
    }
}
