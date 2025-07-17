package com.shmashine.api.test;

import javax.annotation.Resource;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.shmashine.api.ApiApplication;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.elevator.TblElevatorHeatMapService;

/**
 * @PackgeName: PACKAGE_NAME
 * @ClassName: Test
 * @Date: 2020/6/2816:52
 * @Author: LiuLiFu
 * @Description: TODO
 */

@SpringBootTest(classes = ApiApplication.class)
public class Test {

/*    @Autowired
    private BizElevatorService elevatorService;

    @org.junit.jupiter.api.Test
    public void updateInstallTime() {
        // 获取所有安装时间为空的电梯编号
        List<String> elevatorCodeList = elevatorService.getAllElevatorCodeByInstallTimeIsNull();

        // 根据第一次上线时间 更新电梯设备安装时间
        elevatorCodeList.forEach(elevatorCode -> {
            Date installTime = elevatorService.getDeviceEventRecordByFirstRecord(elevatorCode);
            System.out.printf("%s : %s\n", elevatorCode, installTime);
            elevatorService.updateInstallTime(elevatorCode, installTime);
        });
    }*/

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TblElevatorHeatMapService tblElevatorHeatMapService;
    @Resource
    private BizElevatorService elevatorService;

    @org.junit.jupiter.api.Test
    void test1() {
/*        List<String> elevatorCodes=elevatorService.getAllOnlineElevatorCode();
        String elevatorCodesStr=JSONObject.toJSONString(elevatorCodes);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+elevatorCodesStr);*/
    }

    @org.junit.jupiter.api.Test
    void test2() {
/*        TblElevatorHeatMap tblElevatorHeatMap = new TblElevatorHeatMap();
        String id= SnowFlakeUtils.nextStrId();
        tblElevatorHeatMap.setId(id);
        tblElevatorHeatMap.setElevatorCode("elevator");
        tblElevatorHeatMap.setFloorNumber(Integer.valueOf("1"));
        tblElevatorHeatMap.setCountStop(2);
        tblElevatorHeatMap.setCountDate("2021-01-27");
        tblElevatorHeatMap.setInsertTime(DateUtil.format(DateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
        tblElevatorHeatMapService.insertElevatorFloorNumberCountInfo(tblElevatorHeatMap);
        System.out.println("=================================");
        System.out.println("=================================");*/
    }

    @org.junit.jupiter.api.Test
    void test3() {
/*        TblElevatorHeatMap tblElevatorHeatMap = new TblElevatorHeatMap();
        tblElevatorHeatMap.setElevatorCode("elevator");
        tblElevatorHeatMap.setFloorNumber(Integer.valueOf("1"));
        tblElevatorHeatMap.setCountDate("2021-01-27");

        List<TblElevatorHeatMap> elevatorHeatMapList=tblElevatorHeatMapService.getTblElevatorHeatMap(tblElevatorHeatMap);

        TblElevatorHeatMap elevatorHeatMap=elevatorHeatMapList.get(0);
        elevatorHeatMap.setCountStop(333);
        elevatorHeatMap.setModifyTime(DateUtil.format(DateTime.now(),DatePattern.NORM_DATETIME_PATTERN));
        tblElevatorHeatMapService.modifyElevatorFloorNumberCountInfo(elevatorHeatMap);

        System.out.println("-----------------------------------------------------");*/
    }


    @org.junit.jupiter.api.Test
    void timeDiff() {

/*        String currDateTimeStr= DateUtil.format(DateTime.now(),DatePattern.NORM_DATETIME_PATTERN);
        Date currDateTime = DateUtil.parse(currDateTimeStr);

        String endDateTimeStr= DateUtil.format(DateTime.now(),"yyyy-MM-dd 23:59:59");
        Date endDateTime = DateUtil.parse(endDateTimeStr);

        long betweenHour = DateUtil.between(currDateTime, endDateTime, DateUnit.SECOND);

        System.out.println("betweenDay>>>>>>>>>>"+betweenHour);*/

/*        String endDateTime= DateUtil.format(DateTime.now(),"yyyy-MM-dd 23:59:59");

        System.out.println("endDateTime>>>>>>>>>>"+endDateTime);*/

/*        String dateStr1 = "2021-01-28 15:12:00";
        Date date1 = DateUtil.parse(dateStr1);

        String dateStr2 = "2021-01-28 15:15:00";
        Date date2 = DateUtil.parse(dateStr2);

        long timeDiff = DateUtil.between(date1, date2, DateUnit.MINUTE);

        redisTemplate.opsForValue().set("test:123",789,timeDiff, TimeUnit.MINUTES);*/
    }

    @org.junit.jupiter.api.Test
    void redis() {
/*        Map<String, Object> countElevatorFloorInfo = new HashMap<>();

        Map<String, Object> FloorInfo = new HashMap<>();
        countElevatorFloorInfo.put("droopClose", 0);
        //FloorInfo.put(String.valueOf(floorNumber),0);
        countElevatorFloorInfo.put("countElevatorFloorInfo", FloorInfo);
        redisTemplate.opsForValue().set("test:MX789", countElevatorFloorInfo);*/
    }
}
