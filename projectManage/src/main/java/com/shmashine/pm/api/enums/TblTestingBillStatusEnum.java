package com.shmashine.pm.api.enums;

public enum TblTestingBillStatusEnum {

    Doing("调测中", 1),
    Unimprove("未通过", 2),
    Done("已调测", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblTestingBillStatusEnum(String name, int value) {
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
        for (TblTestingBillStatusEnum e : TblTestingBillStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
