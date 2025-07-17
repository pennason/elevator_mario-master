package com.shmashine.pm.api.enums;

public enum TblPmImageTypeEnum {
    ElevatorGreenCode("电梯绿码", 1),
    Floor("平层照片", 2),
    MashRoomSignal("机房信号", 3),
    CarInside("轿厢照片", 4),
    MachRoomBox("机房盒子照片", 5),
    CarRoofBox("轿顶盒子图片", 6),
    EquipmentCode("电梯注册码", 7),
    ElevatorDoorInside("开门按钮内侧", 8);

    private String name;
    private int value;

    TblPmImageTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }


    public static String getName(int value) {
        for (TblPmImageTypeEnum e : TblPmImageTypeEnum.values()) {
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
