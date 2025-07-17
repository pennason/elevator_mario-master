package com.shmashine.pm.api.module.testingBill.input;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 测试单
 *
 * @author chenxue
 */

@Data
@ToString
public class TestingBillModule implements Serializable {

    private String vTestingTaskId;

    private String vProjectId;

    private String vVillageId;

    private String vElevatorCode;

    private String vTestingBillId;


}
