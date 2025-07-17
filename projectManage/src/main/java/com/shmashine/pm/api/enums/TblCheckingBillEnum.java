package com.shmashine.pm.api.enums;

public enum TblCheckingBillEnum {

    Doing("验收中", 1),
    Done("已验收", 2),

    Revise("需调整", 11),
    Canceled("已取消", -1);

    private String name;
    private int value;

    TblCheckingBillEnum(String name, int value) {
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
        for (TblCheckingBillEnum e : TblCheckingBillEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
