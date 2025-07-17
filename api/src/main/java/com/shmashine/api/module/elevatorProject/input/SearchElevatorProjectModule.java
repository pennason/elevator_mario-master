package com.shmashine.api.module.elevatorProject.input;

import java.util.List;

import lombok.Data;

/**
 * @PackgeName: com.shmashine.api.module.elevatorProject.input
 * @ClassName: SearchElevatorProjectModule
 * @Date: 2020/7/610:34
 * @Author: LiuLiFu
 * @Description: 获取项目列表参数
 */

@Data
public class SearchElevatorProjectModule {

    /**
     * 权限列表
     */
    private List<String> permissionDeptIds;

    /**
     * 项目IDS
     */
    private List<String> projectIds;
}
