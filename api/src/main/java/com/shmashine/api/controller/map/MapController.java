package com.shmashine.api.controller.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.map.BizMapService;
import com.shmashine.api.service.user.BizUserService;

/**
 * 分布地图接口
 */
@RestController
@RequestMapping("/map")
public class MapController extends BaseRequestEntity {

    private BizMapService bizMapService;
    private BizUserService bizUserService;

    @Autowired
    public MapController(BizMapService bizMapService, BizUserService bizUserService) {
        this.bizMapService = bizMapService;
        this.bizUserService = bizUserService;
    }

    /**
     * 加载地图第一层标记 小区
     *
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/searchVillageMap")
    public Object searchVillageMap() {
        String userId = getUserId();
        List<Map> maps = bizMapService.searchElevatorCountInfo(userId, bizUserService.isAdmin(userId), null, null);
        return ResponseResult.successObj(maps);
    }

    @GetMapping("/searchVillageMap")
    public Object searchVillageMap(@RequestParam(value = "projectIds", required = false) String projectIdsString,
                                   @RequestParam(value = "villageIds", required = false) String villageIdsString) {
        // 不可使用 stream 的 collect, jdk 不兼容
        ArrayList<String> projectIds = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(projectIdsString)) {
            Arrays.stream(projectIdsString.split(",")).map(String::trim)
                    .forEach(projectIds::add);
        }
        ArrayList<String> villageIds = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(villageIdsString)) {
            Arrays.stream(villageIdsString.split(",")).map(String::trim)
                    .forEach(villageIds::add);
        }

        String userId = getUserId();
        List<Map> maps = bizMapService.searchElevatorCountInfo(userId, bizUserService.isAdmin(userId),
                projectIds, villageIds);
        return ResponseResult.successObj(maps);
    }

    /**
     * 通过小区编号获取电梯List
     *
     * @param villageId 小区ID
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getVillageElevatorList")
    public Object getVillageElevatorList(@RequestBody @Valid @NotNull(message = "请输入小区Id") String villageId) {
        String userId = getUserId();
        List<Map> maps = bizMapService.getVillageElevatorList(villageId, userId, bizUserService.isAdmin(userId));
        return ResponseResult.successObj(maps);
    }

    /**
     * 通过电梯编号获取位置信息
     *
     * @param elevatorId 电梯ID
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getElevatorPosition")
    public Object getElevatorPosition(@RequestBody @Valid @NotNull(message = "请输入电梯Id") String elevatorId) {
        Map elevatorPosition = bizMapService.getElevatorPosition(elevatorId);
        return ResponseResult.successObj(elevatorPosition);
    }


}
