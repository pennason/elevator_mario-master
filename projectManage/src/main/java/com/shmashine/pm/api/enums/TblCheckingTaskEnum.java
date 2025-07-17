package com.shmashine.pm.api.enums;

public enum TblCheckingTaskEnum {

    CheckLess("未验收", 1),
    Checking("验收中", 2),
    Checked("完成", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblCheckingTaskEnum(String name, int value) {
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
        for (TblCheckingTaskEnum e : TblCheckingTaskEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}