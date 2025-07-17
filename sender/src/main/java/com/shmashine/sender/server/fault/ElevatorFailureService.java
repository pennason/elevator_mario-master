package com.shmashine.sender.server.fault;


import java.util.List;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface ElevatorFailureService {
    public List<String> getFaultTypeInTimeByElevatorCode(String elevatorCode);
}
