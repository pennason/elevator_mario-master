package com.shmashine.pm.api.enums;

public enum TblProjectStatusEnum {
    Doing("进行中", 1),
    Done("已完成", 2);

    private String name;
    private int value;

    TblProjectStatusEnum(String name, int value) {
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
        for (TblProjectStatusEnum e : TblProjectStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
