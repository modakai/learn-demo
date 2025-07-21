package com.sakura.fastexcel.domain.excel;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DrugFullInfoExcelModel implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 药品说明书编号（varchar）
     */
    @ExcelProperty("药品说明书编号")
    private String instructionCode;

    /**
     * 药品说明书编号2（varchar）
     */
    @ExcelProperty("药品说明书编号2")
    private String instructionCode2;

    /**
     * 显示名称（text）
     */
    @ExcelProperty("显示名称")
    private String showName;

    /**
     * 英文名（text）
     */
    @ExcelProperty("英文名称")
    private String engName;

    /**
     * 药品通用名称（text）
     */
    @ExcelProperty("通用名称")
    private String commonName;

    /**
     * 药品中文名称（text）
     */
    @ExcelProperty("中文名称")
    private String cnName;

    /**
     * 成份（mediumtext）
     */
    @ExcelProperty({"成份"})
    private String component;

    /**
     * 适应症（text）
     */
    @ExcelProperty("适应症")
    private String indication;

    /**
     * 警告（text）
     */
    @ExcelProperty("警告")
    private String warn;

    /**
     * 规格（text）
     */
    @ExcelProperty("规格")
    private String form;

    /**
     * 用法用量（longtext）
     */
    @ExcelProperty("用法用量")
    private String dosage;

    /**
     * 不良反应（longtext）
     */
    @ExcelProperty("不良反应")
    private String adverseReactions;

    /**
     * 禁忌（text）
     */
    @ExcelProperty("禁忌")
    private String contraindications;

    /**
     * 注意事项（text）（非空）
     */
    @ExcelProperty("注意事项")
    private String precautions;

    /**
     * 孕妇及哺乳期妇女用药（text）
     */
    @ExcelProperty("孕妇及哺乳期妇女用药")
    private String useinPreglact;

    /**
     * 儿童用药（text）
     */
    @ExcelProperty("儿童用药")
    private String useinChildren;

    /**
     * 老年用药（text）
     */
    @ExcelProperty("老年用药")
    private String useinElderly;

    /**
     * 药物相互作用（text）
     */
    @ExcelProperty("药物相互作用")
    private String drugInteractions;

    /**
     * 药物过量（longtext）
     */
    @ExcelProperty("药物过量")
    private String overdosage;

    /**
     * 药理作用（text）
     */
    @ExcelProperty("药理作用")
    private String mechanismAction;

    /**
     * 药代动力学（text）
     */
    @ExcelProperty("药代动力学")
    private String pharmacokinetics;

    /**
     * 性状（text）
     */
    @ExcelProperty("性状")
    private String description;

    /**
     * 贮藏（text）
     */
    @ExcelProperty("贮藏")
    private String storage;

    /**
     * 包装（text）
     */
    @ExcelProperty("包装")
    private String pack;

    /**
     * 有效期（text）
     */
    @ExcelProperty("有效期")
    private String period;

    /**
     * 执行标准（text）
     */
    @ExcelProperty("执行标准")
    private String standard;

    /**
     * 批准文号（text）
     */
    @ExcelProperty("批准文号")
    private String approveCode;

    /**
     * 是否OTC（varchar）
     */
    @ExcelProperty("是否OTC")
    private String otc;

    /**
     * 上市许可持有人（varchar）
     */
    @ExcelProperty("上市许可持有人")
    private String marketingAuthorisationHolder;

    /**
     * 生产企业（varchar）
     */
    @ExcelProperty("生产企业")
    private String companyName;

    /**
     * 药物分类（varchar）
     */
    @ExcelProperty("药物分类")
    private String cateName;

    /**
     * 核准日期（varchar）
     */
    @ExcelProperty("核准日期")
    private String approveDate;

    /**
     * 修改日期（varchar）
     */
    @ExcelProperty("修改日期")
    private String modifyDate;


}
