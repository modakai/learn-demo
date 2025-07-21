package com.sakura.fastexcel;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.idev.excel.FastExcel;
import com.sakura.fastexcel.domain.Medicine;
import com.sakura.fastexcel.listener.MedicineListener;
import com.sakura.fastexcel.mapper.MedicineMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.InputStream;

@SpringBootApplication
@MapperScan("com.sakura.fastexcel.mapper")
@EnableTransactionManagement
public class DemoFastExcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoFastExcelApplication.class, args);
    }

}
