ALTER TABLE elevator_master.tbl_elevator
    ADD installation_unit varchar(255) NULL COMMENT '电梯安装单位' AFTER reforming_date;

ALTER TABLE elevator_master.tbl_elevator MODIFY COLUMN v_building_id varchar (50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '楼宇id（楼栋号）';
ALTER TABLE elevator_master.tbl_elevator MODIFY COLUMN v_unit_code varchar (20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单位内编号（使用单位内部编号）';

ALTER TABLE elevator_master.tbl_elevator
    ADD building_type int(10) DEFAULT 0 NULL COMMENT '0:未设置，1 住宅 2 办公楼宇 3 商场超市 4 宾馆饭店 5 交通场所 6 医院 7 学校  8 文体娱场所 9 其他' AFTER v_building_id;
ALTER TABLE elevator_master.tbl_elevator
    ADD nominal_speed_up DECIMAL(10, 2) NULL COMMENT '电梯上行额定速度（m/s）' AFTER dc_speed;
ALTER TABLE elevator_master.tbl_elevator
    ADD nominal_speed_down DECIMAL(10, 2) NULL COMMENT '电梯下行额定速度（m/s）' AFTER nominal_speed_up;
ALTER TABLE elevator_master.tbl_elevator
    ADD floor_count int(10) NULL COMMENT '层数' AFTER i_min_floor;
ALTER TABLE elevator_master.tbl_elevator
    ADD station_count int(10) NULL COMMENT '站数' AFTER floor_count;
ALTER TABLE elevator_master.tbl_elevator
    ADD base_station varchar(10) NULL COMMENT '基站（默认停的楼层）' AFTER station_count;
ALTER TABLE elevator_master.tbl_elevator
    ADD nominal_load_capacity varchar(20) NULL COMMENT '额定载重量（KG）' AFTER nominal_speed_down;
ALTER TABLE elevator_master.tbl_elevator
    ADD property_safe_man varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '物业安全管理员';
ALTER TABLE elevator_master.tbl_elevator
    ADD property_safe_phone varchar(32) NULL COMMENT '物业安全管理员电话';
ALTER TABLE elevator_master.tbl_elevator
    ADD maintain_principal_person varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '维保第一责任人';
ALTER TABLE elevator_master.tbl_elevator
    ADD maintain_principal_phone varchar(12) NULL COMMENT '维保第一责任人电话';
ALTER TABLE elevator_master.tbl_elevator
    ADD maintain_subordinate_person varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '维保第二责任人';
ALTER TABLE elevator_master.tbl_elevator
    ADD maintain_subordinate_phone varchar(12) NULL COMMENT '维保第二责任人电话';
ALTER TABLE elevator_master.tbl_elevator
    ADD maintain_equ_insurance_info varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '维保设备保险信息（如果有）';
ALTER TABLE elevator_master.tbl_elevator
    ADD maintain_emergency_phone varchar(15) NULL COMMENT '维保应急救援电话';


ALTER TABLE elevator_master.tbl_elevator
    ADD gcj02_latitude varchar(20) NULL COMMENT '高德地图-纬度' AFTER maintain_emergency_phone;
ALTER TABLE elevator_master.tbl_elevator
    ADD gcj02_longitude varchar(100) NULL COMMENT '高德地图-经度' AFTER gcj02_latitude;



