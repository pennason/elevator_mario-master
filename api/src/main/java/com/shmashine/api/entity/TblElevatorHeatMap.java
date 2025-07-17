package com.shmashine.api.entity;

import lombok.Data;

@Data
public class TblElevatorHeatMap {
    private String id;

    private String elevatorCode;

    private Integer floorNumber;

    private Integer countStop;

    private String countDate;

    private String insertTime;

    private String modifyTime;

    private String startDate;

    private String endDate;
}
