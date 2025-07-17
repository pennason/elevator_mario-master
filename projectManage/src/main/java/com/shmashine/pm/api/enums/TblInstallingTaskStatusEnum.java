package com.shmashine.pm.api.enums;

public enum TblInstallingTaskStatusEnum {

//    InstallLess("安装中", 1),
//    Installed("待审核", 2),
//    NeedFix("需调整", 3),
//    ExperAdjust("专家调测", 4),
//    Completed("已完成", 5),

    InstallLess("未安装", 1),
    Installing("安装中", 2),
    Installed("完成", 3),

    Canceled("已取消", -1);

    private String name;
    private int value;

    TblInstallingTaskStatusEnum(String name, int value) {
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
        for (TblInstallingTaskStatusEnum e : TblInstallingTaskStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }
}
