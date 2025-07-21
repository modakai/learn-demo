package com.sakura.fastexcel.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author https://www.json.cn/
 * @description drug_full_info
 * @date 2025-07-18
 */
@Data
@Table("drug_instruction_vo")
public class DrugFullInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（自增）
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 药品说明书编号（varchar）
     */
    private String instructionCode;

    /**
     * 药品说明书编号2（varchar）
     */
    private String instructionCode2;

    /**
     * 显示名称（text）
     */
    private String showName;

    /**
     * 英文名（text）
     */
    private String engName;

    /**
     * 药品通用名称（text）
     */
    private String commonName;

    /**
     * 药品中文名称（text）
     */
    private String cnName;

    /**
     * 成份（mediumtext）
     */
    private String component;

    /**
     * 适应症（text）
     */
    private String indication;

    /**
     * 警告（text）
     */
    private String warn;

    /**
     * 规格（text）
     */
    private String form;

    /**
     * 用法用量（longtext）
     */
    private String dosage;

    /**
     * 不良反应（longtext）
     */
    private String adverseReactions;

    /**
     * 禁忌（text）
     */
    private String contraindications;

    /**
     * 注意事项（text）（非空）
     */
    private String precautions;

    /**
     * 孕妇及哺乳期妇女用药（text）
     */
    private String useinPreglact;

    /**
     * 儿童用药（text）
     */
    private String useinChildren;

    /**
     * 老年用药（text）
     */
    private String useinElderly;

    /**
     * 药物相互作用（text）
     */
    private String drugInteractions;

    /**
     * 药物过量（longtext）
     */
    private String overdosage;

    /**
     * 药理作用（text）
     */
    private String mechanismAction;

    /**
     * 药代动力学（text）
     */
    private String pharmacokinetics;

    /**
     * 性状（text）
     */
    private String description;

    /**
     * 贮藏（text）
     */
    private String storage;

    /**
     * 包装（text）
     */
    private String pack;

    /**
     * 有效期（text）
     */
    private String period;

    /**
     * 执行标准（text）
     */
    private String standard;

    /**
     * 批准文号（text）
     */
    private String approveCode;

    /**
     * 是否OTC（varchar）
     */
    private String otc;

    /**
     * 上市许可持有人（varchar）
     */
    private String marketingAuthorisationHolder;

    /**
     * 生产企业（varchar）
     */
    private String companyName;

    /**
     * 药物分类（varchar）
     */
    private String cateName;

    /**
     * 核准日期（varchar）
     */
    private String approveDate;

    /**
     * 修改日期（varchar）
     */
    private String modifyDate;

    /**
     * 创建时间（datetime）
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 创建人ID（varchar）
     */
    private String createUserId;

    /**
     * 创建人名称（varchar）
     */
    private String createUserName;

    /**
     * 更新时间（datetime）
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime updateTime;

    /**
     * 更新人ID（varchar）
     */
    private String updateUserId;

    /**
     * 更新人名称（varchar）
     */
    private String updateUserName;

    /**
     * 版本号（乐观锁）（int）
     */
    private Integer versionNum;

    /**
     * 是否删除：0.否 1.是（int）
     */
    private Integer deleteFlag;

    public DrugFullInfo() {
    }
}