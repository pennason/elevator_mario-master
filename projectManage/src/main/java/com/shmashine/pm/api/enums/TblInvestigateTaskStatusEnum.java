package com.shmashine.pm.api.enums;

public enum TblInvestigateTaskStatusEnum {

    UploadLess("现勘中", 1),
    ImproveLess("已现勘", 2),
    Completed("完成", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblInvestigateTaskStatusEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static String getName(int value) {
        for (TblInvestigateTaskStatusEnum e : TblInvestigateTaskStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
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
}
