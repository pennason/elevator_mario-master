package com.shmashine.pm.api.module.distributionBill;

public class DistributionBillModule {

    private String vElevatorId;

    private String vDistributionTaskId;

    private String vDistributionBillId;

    private String vProjectId;

    private String vVillageId;

    private String vElevatorCode;

    private Integer iStatus;

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getvDistributionTaskId() {
        return vDistributionTaskId;
    }

    public void setvDistributionTaskId(String vDistributionTaskId) {
        this.vDistributionTaskId = vDistributionTaskId;
    }

    public String getvDistributionBillId() {
        return vDistributionBillId;
    }

    public void setvDistributionBillId(String vDistributionBillId) {
        this.vDistributionBillId = vDistributionBillId;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }
}
