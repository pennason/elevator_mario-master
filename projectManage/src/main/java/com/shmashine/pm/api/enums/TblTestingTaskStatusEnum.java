package com.shmashine.pm.api.enums;

public enum TblTestingTaskStatusEnum {

    //    TestingLess("调测中", 1),
//    Unimprove("未通过", 2),
//    Imporved("已通过", 3),
    TestingLess("未调测", 1),
    Testing("调测中", 2),
    Tested("完成", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblTestingTaskStatusEnum(String name, int value) {
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
        for (TblTestingTaskStatusEnum e : TblTestingTaskStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
