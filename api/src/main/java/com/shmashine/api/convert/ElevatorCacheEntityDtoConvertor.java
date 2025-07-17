// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.convert;

import org.springframework.stereotype.Component;

import com.shmashine.common.convert.BaseEntityDtoConvertor;
import com.shmashine.common.dto.ElevatorCacheDTO;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/7 18:11
 * @since v1.0
 */

@Component
public class ElevatorCacheEntityDtoConvertor extends BaseEntityDtoConvertor<TblElevator, ElevatorCacheDTO> {

    @Override
    public TblElevator dto2Entity(ElevatorCacheDTO source) {
        return null;
    }

    /**
     * 主要含夜间守护模式， 将电梯信息转到缓存中
     *
     * @param source entity 实体
     * @return 模型
     */
    @Override
    public ElevatorCacheDTO entity2Dto(TblElevator source) {
        return ElevatorCacheDTO.builder()
                .elevatorId(source.getVElevatorId())
                .elevatorCode(source.getVElevatorCode())
                .elevatorType(source.getIElevatorType())
                .projectId(source.getVProjectId())
                .villageId(source.getVVillageId())
                .installStatus(source.getIInstallStatus())
                .maxFloor(source.getIMaxFloor())
                .minFloor(source.getIMinFloor())
                .floorDetail(source.getVFloorDetail())
                .ratedSpeed(source.getDcSpeed())
                .leaveFactoryNumber(source.getVLeaveFactoryNumber())
                .delFlag(source.getIDelFlag())
                .nightWatchStatus(source.getNightWatchStatus())
                .nightWatchStartTime(source.getNightWatchStartTime() == null ? null
                        : source.getNightWatchStartTime().toString())
                .nightWatchEndTime(source.getNightWatchEndTime() == null ? null
                        : source.getNightWatchEndTime().toString())
                .build();
    }

    public ElevatorCacheDTO entity2Dto(TblElevator source, TblVillage village) {
        var target = entity2Dto(source);
        if (village == null) {
            return target;
        }
        // 扩展小区信息
        target.setVillageName(village.getVVillageName());
        // 扩展群租信息
        target.setGroupLeasingStatus(village.getGroupLeasingStatus());
        target.setGroupLeasingTimeCoefficient(village.getGroupLeasingTimeCoefficient());
        target.setGroupLeasingStepRange(village.getGroupLeasingStepRange());
        target.setGroupLeasingStartDate(village.getGroupLeasingStartDate());
        target.setGroupLeasingEndDate(village.getGroupLeasingEndDate());
        target.setGroupLeasingStartTime(village.getGroupLeasingStartTime() == null ? null :
                village.getGroupLeasingStartTime().toString());
        target.setGroupLeasingEndTime(village.getGroupLeasingEndTime() == null ? null :
                village.getGroupLeasingEndTime().toString());
        target.setStreet(village.getStreet());
        return target;
    }
}
