package com.sakura.fastexcel.domain;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("medicine")
public class Medicine {

    @ExcelIgnore
    @Id(keyType = KeyType.Auto)
    private Integer id;

    @ExcelProperty("药名")
    private String medicineName;

    @ExcelProperty("适应症")
    private String indication;

    @ExcelProperty("主要成份")
    private String ingredients;

    @ExcelProperty("功能主治")
    private String functionalIndications;

    @ExcelProperty("用法用量")
    private String usage;
    @ExcelProperty("批准文号")
    private String approvalNumber;
    @ExcelProperty("生产企业")
    private String manufacturer;
}
