package com.shmashine.api.service.map.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shmashine.api.dao.BizMapDao;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.service.map.BizMapService;

import lombok.RequiredArgsConstructor;

/**
 * @PackgeName: com.shmashine.api.service.map.impl
 * @ClassName: BizMapServiceImpl
 * @Date: 2020/7/714:19
 * @Author: LiuLiFu
 * @Description: 地图接口
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizMapServiceImpl implements BizMapService {

    private final BizMapDao bizMapDao;
    private final TblElevatorDao elevatorDao;

    /**
     * 加载地图第一层
     *
     * @param userId      用户ID
     * @param isAdminFlag 是否管理员
     * @param projectIds  项目IDs
     * @param villageIds  小区IDs
     * @return 列表
     */
    @Override
    public List<Map> searchElevatorCountInfo(String userId, boolean isAdminFlag, List<String> projectIds, List<String> villageIds) {
        List<String> elevatorIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(villageIds)) {
            elevatorIds = elevatorDao.getElevatorIdsByVillageIds(villageIds);
        } else if (!CollectionUtils.isEmpty(projectIds)) {
            elevatorIds = elevatorDao.getElevatorIdsByProjectIds(projectIds);
        }
        // 加载出
        List<Map> maps = bizMapDao.searchMapElevatorList(userId, isAdminFlag, elevatorIds);
        return maps;
    }

    /**
     * 通过小区编号获取电梯列表
     *
     * @param villageId
     * @return
     */
    @Override
    public List<Map> getVillageElevatorList(String villageId, String userId, boolean isAdminFlag) {
        return bizMapDao.getElevatorInfo(villageId, userId, isAdminFlag);
    }

    /**
     * 获取电梯经纬度信息
     *
     * @param elevatorId
     * @return
     */
    @Override
    public Map getElevatorPosition(String elevatorId) {
        return bizMapDao.getElevatorPosition(elevatorId);
    }
}
