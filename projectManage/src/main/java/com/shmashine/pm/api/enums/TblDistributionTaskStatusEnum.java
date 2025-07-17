package com.shmashine.pm.api.enums;

public enum TblDistributionTaskStatusEnum {

    DistributeLess("未配货", 1),
    Uploaded("配货中", 2),
    Completed("完成", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblDistributionTaskStatusEnum(String name, int value) {
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
        for (TblDistributionTaskStatusEnum e : TblDistributionTaskStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
