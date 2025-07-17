package com.shmashine.sender.server.dataAccount.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.sender.config.MessageTopics;
import com.shmashine.sender.log.SenderCounter;
import com.shmashine.sender.log.SenderCounterServer;
import com.shmashine.sender.message.kafka.KafkaProducer;
import com.shmashine.sender.platform.city.shanghai.LinGangHttpUtil;
import com.shmashine.sender.redis.utils.RedisUtils;
import com.shmashine.sender.server.dataAccount.BizDataAccountService;
import com.shmashine.sender.server.dataAccount.DataAccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Slf4j
@Service
public class DataAccountServiceImpl implements DataAccountService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private BizDataAccountService bizDataAccountService;

    @Autowired
    LinGangHttpUtil linGangHttpUtil;

    @Resource
    private KafkaProducer kafkaProducer;

    private final int intervalTime = 5; //单位：秒

    private ExecutorService threadPool = new ShmashineThreadPoolExecutor(20, 50,
            30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("threadPool"), "DataAccountService");

    @Override
    public void addData(String accountCode, String number, String jsonData, long expireTime) {
        String key = RedisKeyUtils.getDataAccountKey(accountCode, number);
        redisUtils.set(key, jsonData, expireTime);
    }

    @Override
    public JSONObject getData(String key) {
        String text = redisUtils.get(key);
        log.info("data account getData => key: {} ---- value: {}", key, text);
        if (!StringUtils.hasText(text)) {
            return null;
        }
        String replaceStr = text.replaceAll("\\\\", "");
        String subStr = replaceStr.substring(1, replaceStr.length() - 1);
        try {
            return JSON.parseObject(subStr);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("data-account-getData-error key:{} value:{}", key, subStr);
        }
        return null;
    }

    @Override
    public void bizSetData(String number, String jsonData) {
        String accountCode = "yidian";
        addData(accountCode, number, jsonData, 3600);
    }

    @Override
    public void bizPushData() {
        List<Map> list = bizDataAccountService.list();

        ArrayList<String> offlineList = new ArrayList<>();

        for (Map account : list) {
            threadPool.execute(() -> {

                String accountCode = (String) account.get("v_account_code");
                Map map = SenderCounterServer.count(accountCode);
                var senderCounters = ((List<SenderCounter>) map.get("senderCounters")).stream()
                        .filter(SenderCounter::isOnline)
                        .map(SenderCounter::getRegisterNumber).toList();
                offlineList.addAll(senderCounters);
                String key = "DATAACCOUNT:" + accountCode + ":";
                Set<String> redisKeys = redisUtils.findKeysByPrex(key);
                if (!redisKeys.isEmpty()) {
                    for (String k : redisKeys) {
                        JSONObject data = getData(k);
                        if (data == null) {
                            continue;
                        }
                        if (!offlineList.contains(data.getString("registerNumber"))) {
                            continue;
                        }

                        log.info("push data: {}", data);
                        var lastPushTime = data.getString("samplingTime");
                        log.info("push lastPushTime: {}", lastPushTime);
                        if (StringUtils.hasText(lastPushTime)) {
                            if ((System.currentTimeMillis() - DateUtil.parse(lastPushTime).getTime())
                                    >= 5 * 60 * 1000) {
                                log.info("push accountData: {}", data);
                                data.put("samplingTime", DateUtil.now());
                                kafkaProducer.sendMessageToKafka(MessageTopics.CUBE_DATA_ACCOUNT,
                                        JSONObject.toJSONString(data));
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void taskPushStatistics2linGang() {

        //获取临港电梯
        List<TblElevator> elevators = bizDataAccountService.getElevatorsByLingang();

        //格式转换
        for (TblElevator elevator : elevators) {

            Date date1 = DateUtil.parse(((LocalDateTime) elevator.getDtInstallTime())
                    .format(DateTimeFormatter.ISO_LOCAL_DATE));
            Date date2 = DateUtil.date();

            //累计运行时间
            Long totalRunningTime = DateUtil.between(date1, date2, DateUnit.HOUR);

            String registerNumber = elevator.getVEquipmentCode();

            var content = Map.of("elevatorCode", elevator.getVElevatorCode(),
                    "registrtionCode", registerNumber,
                    "doorOpenCount", elevator.getBiDoorCount(),
                    "presentCounterValue", elevator.getBiRunCount(),
                    "liftMileage", elevator.getBiRunDistanceCount(),
                    "totalRunningTime", totalRunningTime,
                    "ropeBendCount", elevator.getBiBendCount());

            var params = Map.of("runStatistic", "runStatistic",
                    "description", "运行统计数据",
                    "content", content);


            String url = StrUtil.format("{}/{}/user/data", "005b3548375b17c6", registerNumber);

            //推送数据
            linGangHttpUtil.send(registerNumber, "statistics", url, JSON.toJSONString(params));

        }

    }
}
