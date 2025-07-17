package com.shmashine.api.task;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.shmashine.api.entity.TblElevatorHeatMap;
import com.shmashine.api.mongo.entity.FloorStopCountInfo;
import com.shmashine.api.mongo.utils.MongoTemplateUtil;
import com.shmashine.api.service.elevator.TblElevatorHeatMapService;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"prod"})
@Component
// @EnableScheduling
@EnableAsync
public class SaveElevatorFloorNumberCountNewTask {

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            com.shmashine.common.thread.PersistentRejectedExecutionHandler.of("executorService"),
            "SaveElevatorFloorNumberCountNewTask");

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private TblElevatorHeatMapService tblElevatorHeatMapService;
    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    /**
     * 定时扫描redis中的楼层统计数据持久化到mysql
     */
    @Scheduled(cron = "0 5 0/1 * * ? ")
    public void saveElevatorFloorNumberCount() {

        String countDate = DateUtil.format(DateTime.now(), DatePattern.NORM_DATE_PATTERN);
        // Set<String> elevatorCodeSet =(Set<String>) redisTemplate.opsForSet().members(elevatorInfo);
        var elevatorCodeSet = (Set<String>) redisTemplate.opsForSet().members(RedisKeyUtils.getElevatorMembers());
        if (null == elevatorCodeSet && elevatorCodeSet.size() == 0) {
            return;
        }

        for (String elevatorCode : elevatorCodeSet) {

            executorService.submit(() -> {

                String floorCountInfoKey = RedisConstants.ELEVATOR_COUNT_HEARTMAP + elevatorCode;
                Map<String, Integer> floorCountInfo = redisTemplate.opsForHash().entries(floorCountInfoKey);

                for (Map.Entry<String, Integer> entry : floorCountInfo.entrySet()) {

                    String floor = entry.getKey();
                    Integer count = entry.getValue();

                    TblElevatorHeatMap tblElevatorHeatMap = new TblElevatorHeatMap();
                    tblElevatorHeatMap.setElevatorCode(elevatorCode);
                    tblElevatorHeatMap.setFloorNumber(Integer.valueOf(floor));
                    tblElevatorHeatMap.setCountDate(countDate);

                    List<TblElevatorHeatMap> elevatorHeatMapList = tblElevatorHeatMapService.getTblElevatorHeatMap(tblElevatorHeatMap);

                    if (elevatorHeatMapList != null && elevatorHeatMapList.size() > 0) {
                        TblElevatorHeatMap elevatorHeatMap = elevatorHeatMapList.get(0);
                        elevatorHeatMap.setCountStop(count);
                        elevatorHeatMap.setModifyTime(DateUtil.format(DateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
                        tblElevatorHeatMapService.modifyElevatorFloorNumberCountInfo(elevatorHeatMap);

                    } else {
                        insertElevatorHearMapInfo(elevatorCode, floor, count, countDate);
                    }
                }

            });
        }
    }


    /**
     * 每5分钟统计一次楼层停靠数据（用来计算预停靠楼层
     */
    @Scheduled(cron = "1 0/5 * * * ? ")
    public void countFloorStop() {

        log.info("统计楼层停靠数据开始");

        //获取需要统计的电梯
        String preKey = RedisConstants.ELEVATOR_FLOOR_COUNT_MARK + "*";
        Set<String> elevatorCodes = redisTemplate.keys(preKey);

        //获取停靠统计
        for (String elevatorCode : elevatorCodes.stream()
                .map(it -> it.substring(RedisConstants.ELEVATOR_FLOOR_COUNT_MARK.length()))
                .collect(Collectors.toList())) {

            executorService.submit(() -> {

                String floorCountInfoKey = RedisConstants.ELEVATOR_COUNT_HEARTMAP + elevatorCode;
                Map<String, Integer> floorCountInfo = redisTemplate.opsForHash().entries(floorCountInfoKey);
                DateTime now = DateTime.now();
                DateTime beginTime = DateUtil.offsetHour(now, -1);

                for (Map.Entry<String, Integer> entry : floorCountInfo.entrySet()) {

                    Integer floor = Integer.valueOf(entry.getKey());
                    Integer count = entry.getValue();

                    //上次记录楼层停靠次数
                    List<FloorStopCountInfo> floorStopCountInfos = mongoTemplateUtil.getFloorStopCountInfo(elevatorCode, floor, beginTime);

                    Integer lastFloorCount = count;
                    if (floorStopCountInfos != null && floorStopCountInfos.size() > 0) {
                        lastFloorCount = floorStopCountInfos.get(0).getLastFloorCount();
                    }

                    //落表(mongodb - 过期时间为1个月)
                    FloorStopCountInfo countInfo = FloorStopCountInfo.builder()
                            .id(IdUtil.getSnowflakeNextIdStr())
                            .elevatorCode(elevatorCode)
                            .floor(floor)
                            .floorCount(count - lastFloorCount)
                            .lastFloorCount(count)
                            .countTime(now).build();

                    mongoTemplateUtil.insert(countInfo);

                    log.info("统计楼层停靠数据完成-info:{}", countInfo);
                }
            });
        }

        log.info("统计楼层停靠数据结束");

    }

    /**
     * 楼层统计信息落库
     *
     * @param elevatorCode 电梯编号
     * @param floorNumber  楼层编号
     * @param countNumber  楼层统计次数
     * @param countDate    统计时间
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
