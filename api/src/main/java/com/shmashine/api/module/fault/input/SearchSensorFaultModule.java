package com.shmashine.api.module.fault.input;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/7/19 9:45
 */
public class SearchSensorFaultModule extends SearchFaultModule {

    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
