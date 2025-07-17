package com.shmashine.pm.api.module.genTaskModule;

public class GenerateTaskModule {

    /**
     * 电梯id 多个
     */
    private String vElevatorIds;

    /**
     * 小区id
     */
    private String vVillageId;

    /**
     * 任务id
     */
    private String vTaskId;

    /**
     * 负责人id
     */
    private String vPrincipalId;

    public String getvElevatorIds() {
        return vElevatorIds;
    }

    public void setvElevatorIds(String vElevatorIds) {
        this.vElevatorIds = vElevatorIds;
    }

    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    public String getvTaskId() {
        return vTaskId;
    }

    public void setvTaskId(String vTaskId) {
        this.vTaskId = vTaskId;
    }

    public String getvPrincipalId() {
        return vPrincipalId;
    }

    public void setvPrincipalId(String vPrincipalId) {
        this.vPrincipalId = vPrincipalId;
    }
}
