-- 药品基础信息表 (drug_basic_info)
CREATE TABLE drug_basic_info
(
    drug_id              BIGINT PRIMARY KEY COMMENT '主键（可用批准文号替代）',
    show_name            VARCHAR(100) NOT NULL COMMENT '显示名称',
    common_name          VARCHAR(100) NOT NULL COMMENT '通用名称',
    eng_name             VARCHAR(100) COMMENT '英文名称',
    description          TEXT COMMENT '性状描述',
    form                 VARCHAR(100) COMMENT '剂型（如：每瓶100毫升）',
    dosage               TEXT COMMENT '用法用量',
    storage              VARCHAR(255) COMMENT '贮藏条件',
    pack                 VARCHAR(255) COMMENT '包装规格',
    `period`             VARCHAR(10) COMMENT '有效期（月）',
    standard             VARCHAR(255) COMMENT '执行标准（如：WS3-B-4010-98）',
    approve_code         VARCHAR(50) COMMENT '批准文号',
    cate_name            VARCHAR(100) COMMENT '药品分类（如：止咳化痰类）',
    otc                  VARCHAR(100) COMMENT 'OTC标识（非处方药）',
    approve_date         DATE COMMENT '批准日期',
    modify_date          DATE COMMENT '最后修订日期',
    marketing_authoriser VARCHAR(255) COMMENT '上市许可持有人',
    manufacturer         VARCHAR(255) COMMENT '生产企业',
    create_time          DATETIME COMMENT '创建时间',
    create_user_id       VARCHAR(50) COMMENT '创建人ID',
    create_user_name     VARCHAR(50) COMMENT '创建人姓名',
    update_time          DATETIME COMMENT '更新时间',
    update_user_id       VARCHAR(50) COMMENT '更新人ID',
    update_user_name     VARCHAR(50) COMMENT '更新人姓名',
    version_num          INT COMMENT '版本号',
    delete_flag          TINYINT DEFAULT 0 COMMENT '删除标志（0-未删除，1-已删除）'
);

-- 成分表 (drug_components)
CREATE TABLE drug_components
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    drug_id   BIGINT,
    component VARCHAR(255) NOT NULL COMMENT '成分（如：麻黄、苦杏仁）',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);

-- 适应症/功能主治表 (drug_indications)
CREATE TABLE drug_indications
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    drug_id    BIGINT,
    indication TEXT NOT NULL COMMENT '适应症描述',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);
-- 不良反应表 (drug_adverse_reactions)
CREATE TABLE drug_adverse_reactions
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    drug_id  BIGINT,
    reaction TEXT NOT NULL COMMENT '不良反应描述',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);

-- 禁忌表 (drug_contraindications)
CREATE TABLE drug_contraindications
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    drug_id          BIGINT,
    contraindication TEXT NOT NULL COMMENT '禁忌描述',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);

-- 注意事项表 (drug_precautions)
CREATE TABLE drug_precautions
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    drug_id    BIGINT,
    precaution TEXT NOT NULL COMMENT '注意事项',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);

-- 药物相互作用表 (drug_interactions)
CREATE TABLE drug_interactions
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    drug_id     BIGINT,
    interaction TEXT NOT NULL COMMENT '药物相互作用描述',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);

-- 特殊人群用药表 (special_population)
CREATE TABLE special_population
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    drug_id       BIGINT,
    pregnancy_use TEXT COMMENT '妊娠期用药',
    lactation_use TEXT COMMENT '哺乳期用药',
    children_use  TEXT COMMENT '儿童用药',
    elderly_use   TEXT COMMENT '老年用药',
    FOREIGN KEY (drug_id) REFERENCES drug_basic_info (drug_id) ON DELETE CASCADE
);