package com.shmashine.api.module.user.input;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @PackgeName: com.shmashine.api.module.user.input
 * @ClassName: UserElevatorPermissionModule
 * @Date: 2020/6/1915:59
 * @Author: LiuLiFu
 * @Description: 用户数据授权更新接口所需参数
 */
@Data
public class UserElevatorPermissionUpdateModule implements Serializable {

    @NotNull(message = "请输入授权的用户")
    private String userId;

    // 新增授权的数据
    private AddedData addedData;

    //删除授权的数据
    private RemovedData removedData;

    @Data
    public static class AddedData implements Serializable {

        //项目列表
        private List<String> projectIds;

        //小区列表
        private List<String> villageIds;

        //电梯列表
        private List<String> elevatorIds;

    }

    @Data
    public static class RemovedData implements Serializable {

        //项目列表
        private List<String> projectIds;

        //小区列表
        private List<String> villageIds;

        //电梯列表
        private List<String> elevatorIds;

    }


}
