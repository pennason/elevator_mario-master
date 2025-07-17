ALTER TABLE tbl_elevator
    ADD equ_code varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '特种设备代码' AFTER v_equipment_code;
ALTER TABLE tbl_elevator
    ADD identification_number varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '电梯识别码' AFTER equ_code;
