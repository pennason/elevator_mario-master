ALTER TABLE tbl_village
    ADD neighborhood varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所属居委会（村）' AFTER `street`;

ALTER TABLE tbl_elevator
    ADD lift_gate varchar(50) DEFAULT NULL COMMENT '电梯门， 如：6厅门7层6站，那么此字段应为6厅门，传参：6' AFTER gcj02_longitude;
ALTER TABLE tbl_elevator
    ADD show_floor varchar(50) DEFAULT NULL COMMENT '电梯显示楼层' AFTER lift_gate;
ALTER TABLE tbl_elevator
    ADD traction varchar(50) DEFAULT NULL COMMENT '曳引比' AFTER show_floor;
ALTER TABLE tbl_elevator
    ADD has_insurance tinyint(2) DEFAULT 0 COMMENT '是否购买电梯保险, 0:否，1：是' AFTER traction;
ALTER TABLE tbl_elevator
    ADD have_house tinyint(2) DEFAULT 0 COMMENT '有无机房, 0:无，1：有' AFTER has_insurance;
ALTER TABLE tbl_elevator
    ADD examine_date date DEFAULT null COMMENT '年检检验日期 yyyy-MM-dd' AFTER have_house;
ALTER TABLE tbl_elevator
    ADD installation_date date DEFAULT null COMMENT '电梯安装日期 yyyy-MM-dd' AFTER examine_date;
ALTER TABLE tbl_elevator
    ADD model_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '电梯型号' AFTER installation_date;
ALTER TABLE tbl_elevator
    ADD examine_entities varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '年检检验单位' AFTER model_name;

ALTER TABLE tbl_elevator
    ADD maintenance_participate_in_date date DEFAULT NULL COMMENT '按需维保开始日期' AFTER examine_entities;
ALTER TABLE tbl_elevator
    ADD use_unit_phone varchar(12) DEFAULT NULL COMMENT '使用单位人电话' AFTER maintenance_participate_in_date;
ALTER TABLE tbl_elevator
    ADD next_inspection_date date DEFAULT NULL COMMENT '下次检验/检测日期 yyyy-mm-dd' AFTER use_unit_phone;
ALTER TABLE tbl_elevator
    ADD inspection_unit varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '检验/检测单位' AFTER next_inspection_date;
ALTER TABLE tbl_elevator
    ADD insurance_status varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '保险状态' AFTER inspection_unit;
ALTER TABLE tbl_elevator
    ADD insurance_type varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '保险险种' AFTER insurance_status;
ALTER TABLE tbl_elevator
    ADD contractor varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '承保单位' AFTER insurance_type;
ALTER TABLE tbl_elevator
    ADD contractor_start date DEFAULT NULL COMMENT '承保有效期开始日期 yyyy-mm-dd' AFTER contractor;
ALTER TABLE tbl_elevator
    ADD contractor_end date DEFAULT NULL COMMENT '承保有效期到期日期 yyyy-mm-dd' AFTER contractor_start;
ALTER TABLE tbl_elevator
    ADD control_mode varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '电梯控制方式' AFTER contractor_end;
ALTER TABLE tbl_elevator
    ADD normal_rise_height varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '扶梯 提升高度' AFTER control_mode;
ALTER TABLE tbl_elevator
    ADD normal_angle varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '扶梯/人行道倾斜角' AFTER normal_rise_height;
ALTER TABLE tbl_elevator
    ADD normal_width varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '扶梯/人行道名义宽度' AFTER normal_angle;
ALTER TABLE tbl_elevator
    ADD normal_length varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '扶梯/人行道使用区段长度' AFTER normal_width;