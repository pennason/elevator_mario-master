ALTER TABLE tbl_elevator
    ADD `lon` varchar(100) DEFAULT NULL COMMENT '重庆-经度' AFTER `examine_date`;
ALTER TABLE tbl_elevator
    ADD `lat` varchar(100) DEFAULT NULL COMMENT '重庆-纬度' AFTER `lon`;
ALTER TABLE tbl_elevator
    ADD `factory_number` varchar(100) DEFAULT NULL COMMENT '重庆-电梯出厂编号' AFTER `lat`;
ALTER TABLE tbl_elevator
    ADD `registration_code` varchar(100) DEFAULT NULL COMMENT '重庆-电梯注册代码' AFTER `factory_number`;
ALTER TABLE tbl_elevator
    ADD `code_96333` varchar(100) DEFAULT NULL COMMENT '重庆-96333电梯识别码' AFTER `registration_code`;
ALTER TABLE tbl_elevator
    ADD `category` varchar(100) DEFAULT NULL COMMENT '重庆-电梯品种' AFTER `code_96333`;
ALTER TABLE tbl_elevator
    ADD `equipment_number` varchar(100) DEFAULT NULL COMMENT '重庆-电梯型号' AFTER `category`;
ALTER TABLE tbl_elevator
    ADD `use_unit_name` varchar(200) DEFAULT NULL COMMENT '重庆-使用单位名称' AFTER `equipment_number`;
ALTER TABLE tbl_elevator
    ADD `addr` varchar(255) DEFAULT NULL COMMENT '重庆-地址' AFTER `use_unit_name`;
ALTER TABLE tbl_elevator
    ADD `inside_number` varchar(100) DEFAULT NULL COMMENT '重庆-电梯内部编号' AFTER `addr`;
ALTER TABLE tbl_elevator
    ADD `suitable_place` varchar(255) DEFAULT NULL COMMENT '重庆-电梯适用场所' AFTER `inside_number`;
ALTER TABLE tbl_elevator
    ADD `manufacturer` varchar(255) DEFAULT NULL COMMENT '重庆-电梯生产商、进口商' AFTER `suitable_place`;
ALTER TABLE tbl_elevator
    ADD `production_date` date DEFAULT NULL COMMENT '重庆-电梯出厂日期' AFTER `manufacturer`;
ALTER TABLE tbl_elevator
    ADD `reforming_unit` varchar(255) DEFAULT NULL COMMENT '重庆-电梯改造单位' AFTER `production_date`;
ALTER TABLE tbl_elevator
    ADD `reforming_date` date DEFAULT NULL COMMENT '重庆-电梯改造日期' AFTER `reforming_unit`;

ALTER TABLE tbl_elevator
    ADD `floor_number` int(10) DEFAULT NULL COMMENT '重庆-楼层数' AFTER `next_inspection_date`;
ALTER TABLE tbl_elevator
    ADD `stops_number` int(10) DEFAULT NULL COMMENT '重庆-停站数' AFTER `floor_number`;
ALTER TABLE tbl_elevator
    ADD `speed` decimal(10, 2) DEFAULT NULL COMMENT '重庆-额定速度' AFTER `stops_number`;
ALTER TABLE tbl_elevator
    ADD `load_capacity` decimal(10, 2) DEFAULT NULL COMMENT '重庆-额定载重量' AFTER `speed`;

ALTER TABLE tbl_elevator
    ADD `emergency_phone` varchar(100) DEFAULT NULL COMMENT '重庆-紧急联系人电话' AFTER `normal_length`;
ALTER TABLE tbl_elevator
    ADD `inner_code` varchar(100) DEFAULT NULL COMMENT '重庆-单位内部编号' AFTER `emergency_phone`;
ALTER TABLE tbl_elevator
    ADD `safety_manager` varchar(100) DEFAULT NULL COMMENT '重庆-安全管理人员' AFTER `inner_code`;
ALTER TABLE tbl_elevator
    ADD `safety_manager_phone` varchar(100) DEFAULT NULL COMMENT '重庆-安全人员联系电话' AFTER `safety_manager`;
ALTER TABLE tbl_elevator
    ADD `supervisor_name` varchar(100) DEFAULT NULL COMMENT '重庆-维保责任人姓名' AFTER `safety_manager_phone`;
ALTER TABLE tbl_elevator
    ADD `supervisor_phone` varchar(100) DEFAULT NULL COMMENT '重庆-维保责任人电话' AFTER `supervisor_name`;
ALTER TABLE tbl_elevator
    ADD `maintenance_unit` varchar(250) DEFAULT NULL COMMENT '重庆-维护保养单位名称' AFTER `supervisor_phone`;
ALTER TABLE tbl_elevator
    ADD `is_maintenaince` tinyint(1) DEFAULT NULL COMMENT '重庆-是否参与按需维保' AFTER `maintenance_unit`;
ALTER TABLE tbl_elevator
    ADD `use_phone` varchar(100) DEFAULT NULL COMMENT '重庆-使用单位人员电话' AFTER `is_maintenaince`;
ALTER TABLE tbl_elevator
    ADD `maintenance_phone` varchar(100) DEFAULT NULL COMMENT '重庆-应急救援电话（维保单位）' AFTER `use_phone`;
ALTER TABLE tbl_elevator
    ADD `maintenance_cycle` varchar(100) DEFAULT NULL COMMENT '重庆-维保周期' AFTER `maintenance_phone`;
ALTER TABLE tbl_elevator
    ADD `next_inspection` date DEFAULT NULL COMMENT '重庆-下次检验/检测日期' AFTER `maintenance_cycle`;
