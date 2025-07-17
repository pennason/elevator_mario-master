package com.shmashine.pm.api.module.installingBill;

public class InstallingBillModule {

    /**
     * 安装单id
     */
    private String vInstallingBillId;

    /**
     * 安装任务
     */
    private String vInstallingTaskId;
    /**
     * 电梯
     */
    private String vElevatorCode;

    private String vProjectId;

    private String vVillageId;

    public String getvInstallingBillId() {
        return vInstallingBillId;
    }

    public void setvInstallingBillId(String vInstallingBillId) {
        this.vInstallingBillId = vInstallingBillId;
    }

    public String getvInstallingTaskId() {
        return vInstallingTaskId;
    }

    public void setvInstallingTaskId(String vInstallingTaskId) {
        this.vInstallingTaskId = vInstallingTaskId;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
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

    @Override
    public String toString() {
        return "InstallingBillModule{" +
                "vInstallingBillId='" + vInstallingBillId + '\'' +
                ", vInstallingTaskId='" + vInstallingTaskId + '\'' +
                ", vElevatorCode='" + vElevatorCode + '\'' +
                ", vProjectId='" + vProjectId + '\'' +
                ", vVillageId='" + vVillageId + '\'' +
                '}';
    }
}
