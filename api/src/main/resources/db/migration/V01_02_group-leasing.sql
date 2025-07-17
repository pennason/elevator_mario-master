use
`elevator_master`;

---
--- 群租基于小区配置
---

ALTER TABLE `tbl_village`
    ADD `group_leasing_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '群租识别开启 0：不开启 1：开启中' AFTER `v_contacts_name`,
    ADD `group_leasing_time_coefficient` varchar(125) NOT NULL DEFAULT '2,2,2,2,1.5,1.5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1.5,1.5' COMMENT '群租 时间系数 0-23' AFTER `group_leasing_status`,
    ADD `group_leasing_step_range` varchar(50) NOT NULL DEFAULT '100, 150, 200' COMMENT '群租 阈值分段' AFTER `group_leasing_time_coefficient`,
    ADD `group_leasing_start_date` DATE NULL COMMENT '群租 开始取证日期 yyyy-MM-dd' AFTER `group_leasing_step_range`,
    ADD `group_leasing_end_date` DATE NULL COMMENT '群租 结束取证日期 yyyy-MM-dd, 一般间隔30天' AFTER `group_leasing_start_date`,
    ADD `group_leasing_start_time` TIME NULL COMMENT '群租 每日取证开始时间 HH:mm:ss' AFTER `group_leasing_end_date`,
    ADD `group_leasing_end_time` TIME NULL COMMENT '群租 每日取证结束时间 HH:mm:ss' AFTER `group_leasing_start_time`,
    ADD `group_leasing_result` tinyint(1) NOT NULL DEFAULT 0 COMMENT '群租状态确认 0：未确认 1：取证中 2：已确认' AFTER `group_leasing_end_time`;



CREATE TABLE `tbl_group_leasing_floor_coefficient`
(
    `id`                 bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `elevator_code`      varchar(50)    DEFAULT NULL COMMENT '电梯码',
    `day`                date           DEFAULT NULL COMMENT '年月日',
    `floor`              varchar(10)    DEFAULT '' COMMENT '所在楼层',
    `day_total_quantity` decimal(10, 4) DEFAULT '0' COMMENT '当日总和（小时停靠次数*时间系数）， 计算当日系数：此值/24',
    `coefficient`        decimal(10, 4) DEFAULT '0' COMMENT '最近30天计算的系数, 30天的平均数， 每小时的',
    `create_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`        datetime       DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_elevator_day_floor` (`elevator_code`,`day`,`floor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='电梯楼层每日群租系数';


CREATE TABLE `tbl_group_leasing_elevator_coefficient`
(
    `id`                 bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `elevator_code`      varchar(50)    DEFAULT NULL COMMENT '电梯码',
    `day`                DATE           DEFAULT NULL COMMENT '年月日',
    `floor_count`        int(10) unsigned DEFAULT '0' COMMENT '楼层数： 2楼起算，到最高层',
    `day_total_quantity` decimal(10, 4) unsigned DEFAULT '0' COMMENT '当日总和，每个楼层的统计加在一起， 计算当日：此值/floorCount',
    `coefficient`        decimal(10, 4) DEFAULT '0' COMMENT '最近30天计算的系数, 30天的平均数， 每层楼每天的',
    `create_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`        datetime       DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_elevator_day_floor` (`elevator_code`, `day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='电梯每日群租系数';

CREATE TABLE `tbl_group_leasing_village_coefficient`
(
    `id`                 bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `village_id`         varchar(20)    DEFAULT NULL COMMENT '小区ID',
    `day`                DATE           DEFAULT NULL COMMENT '年月日',
    `elevator_count`     int(10) unsigned DEFAULT '0' COMMENT '电梯总数',
    `day_total_quantity` decimal(10, 4) unsigned DEFAULT '0' COMMENT '当日总和，所有电梯平均每层(电梯日统计/floorCount)的加在一起， 计算当日此值/elevatorCount',
    `coefficient`        decimal(10, 4) DEFAULT '0' COMMENT '最近30天计算的系数, 30天的平均数，每电梯每天',
    `create_time`        datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`        datetime       DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_elevator_day_floor` (`village_id`, `day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='电梯每日群租系数';

CREATE TABLE `tbl_group_leasing_statistics`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `village_id`          varchar(20)    DEFAULT NULL COMMENT '小区ID',
    `elevator_code`       varchar(50)    DEFAULT NULL COMMENT '电梯码',
    `statistics_type`     varchar(15) NOT NULL COMMENT '统计级别，elevator, floor',
    `floor`               varchar(10)    DEFAULT '' COMMENT '所在楼层，如果是电梯级别，此值非空，或者0',
    `day_coefficient`     decimal(10, 4) DEFAULT '0' COMMENT '日群租系数',
    `average_coefficient` decimal(10, 4) DEFAULT '0' COMMENT '平均的群租系数',
    `percent`             decimal(10, 4) DEFAULT '0' COMMENT '百分比，使用小数表示',
    `level`               int(10) DEFAULT '0' COMMENT '等级，0：不可能（均值以下）， 1：可疑（均值~均值*1.5），2：很可疑（均值*1.5~均值*2.0），3：非常可疑（均值*2.0~）',
    `create_time`         datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`         datetime       DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_elevator_type_floor` (`elevator_code`, `statistics_type`, `floor`),
    KEY                   `idx_village` (`village_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='电梯群租系数与可疑等级';

CREATE TABLE `tbl_group_leasing_floor_evidence`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `elevator_code` varchar(50) DEFAULT NULL COMMENT '电梯码',
    `floor`         varchar(10) DEFAULT '' COMMENT '所在楼层',
    `start_time`    datetime    DEFAULT NULL COMMENT '年月日时分秒',
    `end_time`      datetime    DEFAULT NULL COMMENT '年月日时分秒',
    `status`        tinyint(4) NOT NULL DEFAULT 0 COMMENT '群租状态确认 0：未确认 1：是群租 2：非群租',
    `create_time`   datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`   datetime    DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_elevator_day_floor` (`elevator_code`,`floor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='电梯楼层群租取证配置';