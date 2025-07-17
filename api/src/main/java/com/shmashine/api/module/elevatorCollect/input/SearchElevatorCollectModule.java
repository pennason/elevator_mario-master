package com.shmashine.api.module.elevatorCollect.input;

import java.util.List;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @PackgeName: com.shmashine.api.module.elevatorCollect.input
 * @ClassName: SearchElevatorCollectModule
 * @Date: 2020/7/114:43
 * @Author: LiuLiFu
 * @Description: 收藏电梯
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchElevatorCollectModule extends PageListParams {
    private String elevatorId;

    private List<String> projectIds;
}
