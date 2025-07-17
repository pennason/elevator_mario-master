package com.shmashine.api.service.wuye.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.dao.MaiXinWuyeElevatorDao;
import com.shmashine.api.module.elevator.ElevatorScreenModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.wuye.MaiXinElevatorService;
import com.shmashine.common.constants.ServiceConstants;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.common.utils.RequestUtil;
import com.shmashine.common.utils.TimeUtils;

@Service
public class MaiXinElevatorServiceImpl implements MaiXinElevatorService {

    @Autowired
    private BizUserService bizUserService;

    @Autowired
    private MaiXinWuyeElevatorDao baseMapper;

    /**
     * 获取电梯基本信息
     *
     * @return
     */
    @Override
    public Map getElevatorInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        Map elevatorInfo = baseMapper.getElevatorInfo(faultStatisticalQuantitySearchModule.getRegister_number());

        return getelevatorInfo(elevatorInfo);
    }

    @Override
    public Map<String, List> getElevatorHeatMapNew(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        HashMap<String, List> resMap = new HashMap<>();
        List<HashMap<String, Object>> datas = baseMapper.getElevatorHeatMapNew(faultStatisticalQuantitySearchModule);

        List<HashMap<String, Object>> series = new ArrayList<>();
        HashMap<String, Object> totalSeries = new HashMap<>();

        List<Integer> floors = datas.stream().map(data -> (Integer) data.get("floor_number")).distinct().collect(Collectors.toList());

        resMap.put("categories", floors);

        totalSeries.put("data", datas.stream().map(item -> ((Number) item.get("count_stop")).intValue()).collect(Collectors.toList()));
        totalSeries.put("name", "电梯停靠热力");
        totalSeries.put("type", "column");
        series.add(totalSeries);

        resMap.put("series", series);
        return resMap;
    }

    @Override
    public ElevatorScreenModule getWeatherInfo(String location) {
        ElevatorScreenModule elevatorScreenInfo = new ElevatorScreenModule();

        if (location == null) {
            location = "nanjing";
        }

        try {
            // 调用心知天气api
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ServiceConstants.WEATHER_API_URL)
                    .queryParam("key", ServiceConstants.WEATHER_API_KEY)
                    .queryParam("location", location)
                    .queryParam("language", "zh-Hans")
                    .queryParam("unit", "c");
            ResponseEntity<String> map = RequestUtil.sendGet(builder);

            JSONObject jsonObject = JSONObject.parseObject(map.getBody());
            System.out.printf("%s --- 调用心知天气api --- %s\n", TimeUtils.nowTime(), jsonObject);
            elevatorScreenInfo.setWeather(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("now"));
        } catch (Exception e) {
            e.printStackTrace();
            elevatorScreenInfo.setWeather(JSONObject.parseObject("{\"code\":\"4\",\"temperature\":\"21\",\"text\":\"晴\"}"));
        }

        return elevatorScreenInfo;
    }

    @Override
    public List<TblElevator> getElevatorListByUser(String userId, boolean isAdmin) {
        return baseMapper.getElevatorListByUser(userId, isAdmin);
    }

    @Override
    public List<TblElevator> getElevatorListByVillage(String userId, boolean isAdmin, String villageId) {
        return baseMapper.getElevatorListByVillage(userId, isAdmin, villageId);
    }

    @Override
    public List<TblElevator> getElevatorListByIds(String userId, boolean isAdmin, List<String> elevatorIds) {
        return baseMapper.searchByElevatorIds(userId, isAdmin, elevatorIds);
    }

    private Map getelevatorInfo(Map elevatorInfo) {
        if (elevatorInfo == null) {
            return null;
        }

        LocalDateTime dt_install_time = (LocalDateTime) elevatorInfo.get("dt_install_time");

        if (dt_install_time != null) {
            // 获取日期
            String now = DateUtils.dateFormate(new Date(), "yyyy-MM-dd HH:mm:ss");
            Date date1 = DateUtils.parseDate(dt_install_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "yyyy-MM-dd HH:mm:ss");
            Date date2 = DateUtils.parseDate(now, "yyyy-MM-dd HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(date2);
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            elevatorInfo.put("cumulative_running_time", betweenDays * 24);
            elevatorInfo.put("cumulative_days", betweenDays);
        } else {
            elevatorInfo.put("cumulative_running_time", "--");
            elevatorInfo.put("cumulative_days", "--");
        }
        return elevatorInfo;
    }

}
