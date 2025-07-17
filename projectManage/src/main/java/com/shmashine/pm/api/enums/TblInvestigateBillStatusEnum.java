package com.shmashine.pm.api.enums;

public enum TblInvestigateBillStatusEnum {

    UploadLess("未现勘", 1), // 暂时未用
    ImproveLess("现勘中", 2),
    Completed("已现勘", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblInvestigateBillStatusEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static String getName(int value) {
        for (TblInvestigateBillStatusEnum e : TblInvestigateBillStatusEnum.values()) {
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
