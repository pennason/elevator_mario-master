package com.shmashine.api.task;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.shmashine.api.entity.TblElevatorHeatMap;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.elevator.TblElevatorHeatMapService;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile({"prod"})
//@EnableScheduling
@EnableAsync
public class SaveElevatorFloorNumberCountTask {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private TblElevatorHeatMapService tblElevatorHeatMapService;
    @Resource
    private BizElevatorService elevatorService;

    /**
     * 定时扫描redis中的楼层统计数据持久化到mysql(旧的逻辑，暂时废弃)
     */
    @Async
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void saveElevatorFloorNumberCount() {
        String countDate = DateUtil.format(DateTime.now(), DatePattern.NORM_DATE_PATTERN);
        TblElevatorHeatMap tblElevatorHeatMap = new TblElevatorHeatMap();
        //获取所有在线的电梯编号
        List<String> elevatorCodes = (List<String>) elevatorService.getAllOnlineElevatorCode();
        for (String elevatorCode : elevatorCodes) {
            String elevatorKey = "countHeatMap:" + elevatorCode;
            boolean isExist = redisTemplate.hasKey(elevatorKey);
            if (isExist) {
                Map<String, Object> redisValueMap = (Map<String, Object>) redisTemplate.boundValueOps(elevatorKey).get();

                Map<String, Object> floorInfo = (Map<String, Object>) redisValueMap.get("countElevatorFloorInfo");

                if (floorInfo.size() <= 0 || null == floorInfo) {
                    continue;
                }

                for (Map.Entry<String, Object> entry : floorInfo.entrySet()) {
                    //楼层
                    String floorNumber = entry.getKey();
                    //楼层停留次数
                    int countNumber = (Integer) entry.getValue();

                    tblElevatorHeatMap.setElevatorCode(elevatorCode);
                    tblElevatorHeatMap.setFloorNumber(Integer.valueOf(floorNumber));
                    tblElevatorHeatMap.setCountDate(countDate);

                    List<TblElevatorHeatMap> elevatorHeatMapList = tblElevatorHeatMapService.getTblElevatorHeatMap(tblElevatorHeatMap);

                    if (elevatorHeatMapList != null && elevatorHeatMapList.size() > 0) {
                        TblElevatorHeatMap elevatorHeatMap = elevatorHeatMapList.get(0);
                        int searchCountStop = elevatorHeatMap.getCountStop();
                        if (searchCountStop == countNumber) {
                            continue;
                        }
                        elevatorHeatMap.setCountStop(countNumber);
                        elevatorHeatMap.setModifyTime(DateUtil.format(DateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
                        tblElevatorHeatMapService.modifyElevatorFloorNumberCountInfo(elevatorHeatMap);

                        //System.out.println(DateUtil.format(DateTime.now(),DatePattern.NORM_DATETIME_PATTERN)+"修改>>>>>"+elevatorCode);

                    } else {
                        insertElevatorHearMapInfo(elevatorCode, floorNumber, countNumber, countDate);

                        //System.out.println(DateUtil.format(DateTime.now(),DatePattern.NORM_DATETIME_PATTERN)+"新增>>>>>"+elevatorCode);
                    }
                }

            }
        }
    }

    /**
     * 楼层统计信息落库
     *
     * @param elevatorCode
     * @param floorNumber
     * @param countNumber
     * @param countDate
     */
    private void insertElevatorHearMapInfo(String elevatorCode, String floorNumber, Integer countNumber, String countDate) {
        TblElevatorHeatMap tblElevatorHeatMap = new TblElevatorHeatMap();
        String id = SnowFlakeUtils.nextStrId();
        tblElevatorHeatMap.setId(id);
        tblElevatorHeatMap.setElevatorCode(elevatorCode);
        tblElevatorHeatMap.setFloorNumber(Integer.valueOf(floorNumber));
        tblElevatorHeatMap.setCountStop(countNumber);
        tblElevatorHeatMap.setCountDate(countDate);
        tblElevatorHeatMap.setInsertTime(DateUtil.format(DateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
        tblElevatorHeatMapService.insertElevatorFloorNumberCountInfo(tblElevatorHeatMap);
    }

}
