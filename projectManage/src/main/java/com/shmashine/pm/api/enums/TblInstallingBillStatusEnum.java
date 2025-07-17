package com.shmashine.pm.api.enums;

public enum TblInstallingBillStatusEnum {

    Doing("安装中", 1),
    Done("已安装", 2),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblInstallingBillStatusEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static String getName(int value) {
        for (TblInstallingBillStatusEnum e : TblInstallingBillStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
