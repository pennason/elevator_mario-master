// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.kpi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.dao.BizCameraDao;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.KpiProjectIotMapper;
import com.shmashine.api.dao.KpiProjectNorthPushMapper;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblProjectDao;
import com.shmashine.api.dao.TblSysSysteminfoDao;
import com.shmashine.api.entity.KpiProjectElevatorCountEntity;
import com.shmashine.api.entity.ProjectNameEntity;
import com.shmashine.api.redis.RedisService;
import com.shmashine.api.service.kpi.KpiProjectServiceI;
import com.shmashine.api.service.system.TblSysUserResourceServiceI;
import com.shmashine.api.service.system.TblSysUserWecomServiceI;
import com.shmashine.common.dto.KpiProjectIotDTO;
import com.shmashine.common.dto.KpiProjectIotNotifyDTO;
import com.shmashine.common.dto.KpiProjectIotStatisticsDTO;
import com.shmashine.common.dto.KpiProjectNorthPushDTO;
import com.shmashine.common.dto.KpiProjectNorthPushNotifyDTO;
import com.shmashine.common.dto.KpiProjectNorthPushStatisticsDTO;
import com.shmashine.common.dto.KpiProjectStatisticsDTO;
import com.shmashine.common.dto.OfflineCountResponseDTO;
import com.shmashine.common.entity.KpiProjectIotEntity;
import com.shmashine.common.entity.KpiProjectNorthPushEntity;
import com.shmashine.common.entity.TblSysSysteminfo;
import com.shmashine.common.entity.TblSysUserWecomEntity;
import com.shmashine.common.enums.WecomNotifyTypeEnum;
import com.shmashine.wecom.api.servlet.gateway.WeComGateway;
import com.shmashine.wecom.components.convert.WeComMarkdownStringConvert;
import com.shmashine.wecom.components.dto.message.requests.WeComMessageMarkDownRequestDTO;
import com.shmashine.wecom.components.properties.WeComBaseProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目KPI与北向推送KPI
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/12 11:16
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KpiProjectServiceImpl implements KpiProjectServiceI {
    private final TblElevatorDao tblElevatorDao;
    private final BizElevatorDao bizElevatorDao;
    private final TblProjectDao tblProjectDao;
    private final KpiProjectIotMapper kpiProjectIotMapper;
    private final KpiProjectNorthPushMapper kpiProjectNorthPushMapper;
    private final BizCameraDao bizCameraDao;
    private final TblSysSysteminfoDao tblSysSysteminfoDao;
    private final TblSysUserResourceServiceI userResourceService;
    private final TblSysUserWecomServiceI tblSysUserWecomService;
    private final RedisService redisService;
    private final WeComGateway weComGateway;

    private final RestTemplate restTemplate;

    /**
     * 与上日比较统计数是否超过此值
     */
    public static final Integer CHANGE_STEP = 3;
    /**
     * 电梯离线，告警，摄像头离线达到指定最小数据时才发送
     */
    public static final Integer MIN_THRESHOLD_SEND_MESSAGE_COUNT = 3;

    /**
     * 离线率和故障率
     */
    public static final Double OFFLINE_OR_FAULT_RATE = 0.05;

    private static final String WECOM_USER_ID_KEY = "wecomUserId";
    private static final String WECOM_USER_NAME_KEY = "wecomUserName";

    /**
     * 当没有项目负责人时，则通知下面系统管理员
     */
    private static final List<Map<String, String>> WECOM_DEVELOP_USER_MAP_LIST = List.of(
            Map.of(WECOM_USER_ID_KEY, "WuZhe", WECOM_USER_NAME_KEY, "武者"),
            Map.of(WECOM_USER_ID_KEY, "NiuNaiKeLe", WECOM_USER_NAME_KEY, "小姜"),
            Map.of(WECOM_USER_ID_KEY, "GuXueMing", WECOM_USER_NAME_KEY, "顾总")
    );
    /**
     * 北向推送支持的平台，tbl_sys_systeminfo表中的system_id
     */
    private static final Integer NORTH_PUSH_SUPPORT_SYSTEM_ID = 10022;
    private static final String URL_NORTH_PUSH_STATISTICS_GET = "http://sender.service.shmashine.com/counter/group-offline-project/{groupId}";

    // 项目KPI相关

    @Override
    public List<KpiProjectIotEntity> listKpiProjectIotEntity() {
        // 按项目获取电梯总数
        var installedProjectElevatorTotalList = countInstalledElevatorGroupByProject();
        if (CollectionUtils.isEmpty(installedProjectElevatorTotalList)) {
            return null;
        }
        var offlineProjectElevatorTotalList = countOfflineElevatorGroupByProject()
                .stream()
                .collect(Collectors.toMap(KpiProjectElevatorCountEntity::getProjectId, KpiProjectElevatorCountEntity::getTotal));
        var faultProjectElevatorTotalList = countFaultElevatorGroupByProject()
                .stream()
                .collect(Collectors.toMap(KpiProjectElevatorCountEntity::getProjectId, KpiProjectElevatorCountEntity::getTotal));
        // 单独计算 实时告警总数，当日已恢复的告警也算一次
        var faultProjectElevatorMaxTotalList = countMaxFaultElevatorGroupByProject()
                .stream()
                .collect(Collectors.toMap(KpiProjectElevatorCountEntity::getProjectId, KpiProjectElevatorCountEntity::getTotal));
        var cameraProjectTotalList = countCameraGroupByProject()
                .stream()
                .collect(Collectors.toMap(KpiProjectElevatorCountEntity::getProjectId, KpiProjectElevatorCountEntity::getTotal));
        var cameraOfflineProjectTotalList = countCameraOfflineGroupByProject()
                .stream()
                .collect(Collectors.toMap(KpiProjectElevatorCountEntity::getProjectId, KpiProjectElevatorCountEntity::getTotal));

        var projectNameMap = tblProjectDao.listProjectName()
                .stream()
                .collect(Collectors.toMap(ProjectNameEntity::getProjectId, ProjectNameEntity::getProjectName));

        return installedProjectElevatorTotalList.stream()
                .map(installed -> KpiProjectIotEntity.builder()
                        .projectId(installed.getProjectId())
                        .projectName(projectNameMap.getOrDefault(installed.getProjectId(), ""))
                        .day(DateUtil.today())
                        .elevatorTotal(installed.getTotal())
                        .elevatorOfflineRealtime(offlineProjectElevatorTotalList.getOrDefault(installed.getProjectId(), 0))
                        .elevatorFaultRealtime(faultProjectElevatorTotalList.getOrDefault(installed.getProjectId(), 0))
                        .elevatorFaultMax(faultProjectElevatorMaxTotalList.getOrDefault(installed.getProjectId(), 0))
                        .cameraTotal(cameraProjectTotalList.getOrDefault(installed.getProjectId(), 0))
                        .cameraOfflineRealtime(cameraOfflineProjectTotalList.getOrDefault(installed.getProjectId(), 0))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, KpiProjectIotEntity> mapKpiProjectIotEntityByDate(String day) {
        return kpiProjectIotMapper.listKpiProjectIotEntityByDate(day)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotEntity::getProjectId, Function.identity()));
    }

    @Override
    public void saveKpiProjectIotEntity(KpiProjectIotEntity kpiProjectIotEntity) {
        var recordInDb = kpiProjectIotMapper.getKpiProjectIotEntityByProjectIdAndDate(
                kpiProjectIotEntity.getProjectId(), kpiProjectIotEntity.getDay());

        if (recordInDb == null) {
            kpiProjectIotEntity.setElevatorOfflineMax(kpiProjectIotEntity.getElevatorOfflineRealtime());
            kpiProjectIotEntity.setElevatorFaultMax(null == kpiProjectIotEntity.getElevatorFaultMax()
                    ? kpiProjectIotEntity.getElevatorFaultRealtime() : kpiProjectIotEntity.getElevatorFaultMax());
            kpiProjectIotEntity.setCameraOfflineMax(kpiProjectIotEntity.getCameraOfflineRealtime());
            kpiProjectIotMapper.insert(kpiProjectIotEntity);
        } else {
            kpiProjectIotEntity.setId(recordInDb.getId());
            kpiProjectIotEntity.setElevatorOfflineMax(Math.max(recordInDb.getElevatorOfflineMax(),
                    kpiProjectIotEntity.getElevatorOfflineRealtime()));
            kpiProjectIotEntity.setElevatorFaultMax(Math.max(recordInDb.getElevatorFaultMax(),
                    (null == kpiProjectIotEntity.getElevatorFaultMax()
                            ? kpiProjectIotEntity.getElevatorFaultRealtime()
                            : kpiProjectIotEntity.getElevatorFaultMax())));
            kpiProjectIotEntity.setCameraOfflineMax(Math.max(recordInDb.getCameraOfflineMax(),
                    kpiProjectIotEntity.getCameraOfflineRealtime()));
            kpiProjectIotMapper.updateById(kpiProjectIotEntity);
        }
    }

    @Override
    public KpiProjectIotNotifyDTO buildProjectNotifyInfo(KpiProjectIotEntity kpiProjectIotEntity) {
        // 获取昨日的项目KPI记录
        var yesterdayKpiProjectIotEntity = getKpiProjectIotEntityByProjectIdAndDate(
                kpiProjectIotEntity.getProjectId(), DateUtil.formatDate(DateUtil.yesterday()));

        var projectNotifyDTO = KpiProjectIotNotifyDTO.builder()
                .projectId(kpiProjectIotEntity.getProjectId())
                .projectName(kpiProjectIotEntity.getProjectName())
                .elevatorTotal(kpiProjectIotEntity.getElevatorTotal())
                .elevatorOfflineRealtime(kpiProjectIotEntity.getElevatorOfflineRealtime())
                .elevatorFaultRealtime(kpiProjectIotEntity.getElevatorFaultRealtime())
                .cameraTotal(kpiProjectIotEntity.getCameraTotal())
                .cameraOfflineRealtime(kpiProjectIotEntity.getCameraOfflineRealtime())
                .yesterdayElevatorOfflineMax(yesterdayKpiProjectIotEntity == null
                        ? 0 : yesterdayKpiProjectIotEntity.getElevatorOfflineMax())
                .yesterdayElevatorFaultMax(yesterdayKpiProjectIotEntity == null
                        ? 0 : yesterdayKpiProjectIotEntity.getElevatorFaultMax())
                .yesterdayCameraOfflineMax(yesterdayKpiProjectIotEntity == null
                        ? 0 : yesterdayKpiProjectIotEntity.getCameraOfflineMax())
                .build();

        // 1. 计算设备在线率是否低于x%
        buildElevatorOnlineRateNotifyMessage(kpiProjectIotEntity, projectNotifyDTO);
        // 2. 计算设备故障率是否高于x%
        buildElevatorFaultRateNotifyMessage(kpiProjectIotEntity, projectNotifyDTO);
        // 3. 计算摄像头在线率是否低于x%
        buildCameraOnlineRateNotifyMessage(kpiProjectIotEntity, projectNotifyDTO);
        // 4. 比较设备在线是否比上一日低x个
        buildElevatorChangeFromYesterdayNotifyMessage(kpiProjectIotEntity, yesterdayKpiProjectIotEntity,
                projectNotifyDTO);
        // 4. 比较设备故障是否比上一日高x个
        buildElevatorFaultChangeFromYesterdayNotifyMessage(kpiProjectIotEntity, yesterdayKpiProjectIotEntity,
                projectNotifyDTO);
        // 6. 比较摄像头在线是否比上一日低x个
        buildCameraChangeFromYesterdayNotifyMessage(kpiProjectIotEntity, yesterdayKpiProjectIotEntity,
                projectNotifyDTO);
        return projectNotifyDTO;
    }

    /**
     * 建议使用 listStatisticsByProjectIdsForIot
     *
     * @param projectIds 项目ID列表
     * @return 列表
     */
    @Deprecated
    @Override
    public List<KpiProjectIotStatisticsDTO> listIotStatisticsByProjectIds(List<String> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return Collections.emptyList();
        }
        var kpiToday = listKpiProjectIotEntityByProjectIdsAndDay(projectIds, DateUtil.today());
        if (CollectionUtils.isEmpty(kpiToday)) {
            return Collections.emptyList();
        }
        var kpiYesterday = listKpiProjectIotEntityByProjectIdsAndDay(projectIds,
                DateUtil.formatDate(DateUtil.yesterday()))
                .stream()
                .collect(Collectors.toMap(KpiProjectIotEntity::getProjectId, Function.identity()));
        var theWeek = listKpiProjectIotEntityByWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        var lastWeek = listKpiProjectIotEntityByLastWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        var theMonth = listKpiProjectIotEntityByMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        var lastMonth = listKpiProjectIotEntityByLastMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        // 按项目组装数据
        var res = new ArrayList<KpiProjectIotStatisticsDTO>(32);
        kpiToday.forEach(kpi -> {
            var statistics = KpiProjectIotStatisticsDTO.builder()
                    .projectId(kpi.getProjectId())
                    .projectName(StringUtils.hasText(kpi.getProjectName()) ? kpi.getProjectName() : kpi.getProjectId())
                    .elevatorTotal(kpi.getElevatorTotal())
                    .cameraTotal(null == kpi.getCameraTotal() ? 0 : kpi.getCameraTotal())
                    .elevatorOfflineRealtime(kpi.getElevatorOfflineRealtime())
                    .elevatorFaultRealtime(kpi.getElevatorFaultRealtime())
                    .cameraOfflineRealtime(kpi.getCameraOfflineRealtime())
                    .abnormalStatus(checkAbnormalStatus(kpi))
                    .dayCompare(KpiProjectIotStatisticsDTO.IotStatistics.builder()
                            .elevatorOfflineRealtime(kpi.getElevatorOfflineRealtime())
                            .elevatorOfflineLastTime(getElevatorOfflineLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .elevatorFaultRealtime(kpi.getElevatorFaultRealtime())
                            .elevatorFaultLastTime(getElevatorFaultLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .cameraOfflineRealtime(null == kpi.getCameraOfflineRealtime() ? 0 : kpi.getCameraOfflineRealtime())
                            .cameraOfflineLastTime(getCameraOfflineLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .build())
                    .weekCompare(KpiProjectIotStatisticsDTO.IotStatistics.builder()
                            .elevatorOfflineRealtime(getElevatorOfflineAvg(theWeek.get(kpi.getProjectId())))
                            .elevatorOfflineLastTime(getElevatorOfflineAvg(lastWeek.get(kpi.getProjectId())))
                            .elevatorFaultRealtime(getElevatorFaultAvg(theWeek.get(kpi.getProjectId())))
                            .elevatorFaultLastTime(getElevatorFaultAvg(lastWeek.get(kpi.getProjectId())))
                            .cameraOfflineRealtime(getCameraOfflineAvg(theWeek.get(kpi.getProjectId())))
                            .cameraOfflineLastTime(getCameraOfflineAvg(lastWeek.get(kpi.getProjectId())))
                            .build())
                    .monthCompare(KpiProjectIotStatisticsDTO.IotStatistics.builder()
                            .elevatorOfflineRealtime(getElevatorOfflineAvg(theMonth.get(kpi.getProjectId())))
                            .elevatorOfflineLastTime(getElevatorOfflineAvg(lastMonth.get(kpi.getProjectId())))
                            .elevatorFaultRealtime(getElevatorFaultLastTime(theMonth.get(kpi.getProjectId())))
                            .elevatorFaultLastTime(getElevatorFaultLastTime(lastMonth.get(kpi.getProjectId())))
                            .cameraOfflineRealtime(getCameraOfflineAvg(theMonth.get(kpi.getProjectId())))
                            .cameraOfflineLastTime(getCameraOfflineAvg(lastMonth.get(kpi.getProjectId())))
                            .build())
                    .build();
            res.add(statistics);
        });
        // 按是否正常排序
        return res.stream()
                .sorted(Comparator.comparing(KpiProjectIotStatisticsDTO::getAbnormalStatus, Comparator.reverseOrder())
                        .thenComparing(KpiProjectIotStatisticsDTO::getElevatorTotal, Comparator.reverseOrder()))
                .distinct()
                .toList();
    }

    @Override
    public List<KpiProjectStatisticsDTO> listStatisticsByProjectIdsForIot(List<String> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return Collections.emptyList();
        }
        var kpiToday = listKpiProjectIotEntityByProjectIdsAndDay(projectIds, DateUtil.today());
        if (CollectionUtils.isEmpty(kpiToday)) {
            return Collections.emptyList();
        }
        var kpiYesterday = listKpiProjectIotEntityByProjectIdsAndDay(projectIds,
                DateUtil.formatDate(DateUtil.yesterday()))
                .stream()
                .collect(Collectors.toMap(KpiProjectIotEntity::getProjectId, Function.identity()));
        var theWeek = listKpiProjectIotEntityByWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        var lastWeek = listKpiProjectIotEntityByLastWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        var theMonth = listKpiProjectIotEntityByMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        var lastMonth = listKpiProjectIotEntityByLastMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectIotDTO::getProjectId, Function.identity()));
        // 按项目组装数据
        var res = new ArrayList<KpiProjectStatisticsDTO>(32);
        kpiToday.forEach(kpi -> {
            var statistics = KpiProjectStatisticsDTO.builder()
                    .projectId(kpi.getProjectId())
                    .projectName(StringUtils.hasText(kpi.getProjectName()) ? kpi.getProjectName() : kpi.getProjectId())
                    .elevatorTotal(kpi.getElevatorTotal())
                    .cameraTotal(null == kpi.getCameraTotal() ? 0 : kpi.getCameraTotal())
                    .abnormalStatus(checkAbnormalStatus(kpi))
                    .elevatorOfflineStatistics(KpiProjectStatisticsDTO.Statistics.builder()
                            .realtime(kpi.getElevatorOfflineRealtime())
                            .todayMax(kpi.getElevatorOfflineMax())
                            .yesterdayMax(getElevatorOfflineLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .weekAvg(getElevatorOfflineAvg(theWeek.get(kpi.getProjectId())))
                            .lastWeekAvg(getElevatorOfflineAvg(lastWeek.get(kpi.getProjectId())))
                            .monthAvg(getElevatorOfflineAvg(theMonth.get(kpi.getProjectId())))
                            .lastMonthAvg(getElevatorOfflineAvg(lastMonth.get(kpi.getProjectId())))
                            .build())
                    .elevatorFaultStatistics(KpiProjectStatisticsDTO.Statistics.builder()
                            .realtime(kpi.getElevatorFaultRealtime())
                            .todayMax(kpi.getElevatorFaultMax())
                            .yesterdayMax(getElevatorFaultLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .weekAvg(getElevatorFaultAvg(theWeek.get(kpi.getProjectId())))
                            .lastWeekAvg(getElevatorFaultAvg(lastWeek.get(kpi.getProjectId())))
                            .monthAvg(getElevatorFaultAvg(theMonth.get(kpi.getProjectId())))
                            .lastMonthAvg(getElevatorFaultAvg(lastMonth.get(kpi.getProjectId())))
                            .build())
                    .cameraOfflineStatistics(KpiProjectStatisticsDTO.Statistics.builder()
                            .realtime(null == kpi.getCameraOfflineRealtime() ? 0 : kpi.getCameraOfflineRealtime())
                            .todayMax(null == kpi.getCameraOfflineMax() ? 0 : kpi.getCameraOfflineMax())
                            .yesterdayMax(getCameraOfflineLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .weekAvg(getCameraOfflineAvg(theWeek.get(kpi.getProjectId())))
                            .lastWeekAvg(getCameraOfflineAvg(lastWeek.get(kpi.getProjectId())))
                            .monthAvg(getCameraOfflineAvg(theMonth.get(kpi.getProjectId())))
                            .lastMonthAvg(getCameraOfflineAvg(lastMonth.get(kpi.getProjectId())))
                            .build())
                    .build();
            res.add(statistics);
        });
        // 按是否正常排序
        return res.stream()
                .sorted(Comparator.comparing(KpiProjectStatisticsDTO::getAbnormalStatus, Comparator.reverseOrder())
                        .thenComparing(KpiProjectStatisticsDTO::getElevatorTotal, Comparator.reverseOrder()))
                .distinct()
                .toList();
    }

    @Override
    public List<KpiProjectIotEntity> listKpiProjectIotEntityByProjectIdsAndDay(List<String> projectIds, String day) {
        return kpiProjectIotMapper.listKpiProjectIotEntityByProjectIdsAndDay(projectIds, day);
    }

    @Override
    public List<KpiProjectIotDTO> listKpiProjectIotEntityByWeek(List<String> projectIds) {
        // 获取本周的第一天
        var firstDayOfWeek = DateUtil.formatDate(DateUtil.beginOfWeek(DateUtil.date()));
        var today = DateUtil.today();
        return kpiProjectIotMapper.getKpiProjectIotByDateRange(projectIds, firstDayOfWeek, today);
    }

    @Override
    public List<KpiProjectIotDTO> listKpiProjectIotEntityByLastWeek(List<String> projectIds) {
        // 获取上周的第一天
        var firstDayOfLastWeek = DateUtil.formatDate(DateUtil.beginOfWeek(DateUtil.lastWeek()));
        // 获取上周最后一天
        var lastDayOfLastWeek = DateUtil.formatDate(DateUtil.endOfWeek(DateUtil.lastWeek()));
        return kpiProjectIotMapper.getKpiProjectIotByDateRange(projectIds, firstDayOfLastWeek, lastDayOfLastWeek);
    }

    @Override
    public List<KpiProjectIotDTO> listKpiProjectIotEntityByMonth(List<String> projectIds) {
        // 获取本月的第一天
        var firstDayOfMonth = DateUtil.formatDate(DateUtil.beginOfMonth(DateUtil.date()));
        var today = DateUtil.today();
        return kpiProjectIotMapper.getKpiProjectIotByDateRange(projectIds, firstDayOfMonth, today);
    }

    @Override
    public List<KpiProjectIotDTO> listKpiProjectIotEntityByLastMonth(List<String> projectIds) {
        // 获取上月的第一天
        var firstDayOfLastMonth = DateUtil.formatDate(DateUtil.beginOfMonth(DateUtil.lastMonth()));
        // 获取上月最后一天
        var lastDayOfLastMonth = DateUtil.formatDate(DateUtil.endOfMonth(DateUtil.lastMonth()));
        return kpiProjectIotMapper.getKpiProjectIotByDateRange(projectIds, firstDayOfLastMonth, lastDayOfLastMonth);
    }

    @Override
    public List<KpiProjectIotDTO> listKpiProjectIotEntityByDay(List<String> projectIds, Integer days) {
        // 获取days天前的日期
        var daysAgo = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -days));
        var today = DateUtil.today();
        return kpiProjectIotMapper.getKpiProjectIotByDateRange(projectIds, daysAgo, today);
    }

    @Override
    public List<KpiProjectIotEntity> listKpiProjectIotDetailByProjectIdsAndDateRange(List<String> projectIds,
                                                                                     String startDate,
                                                                                     String endDate) {
        return kpiProjectIotMapper.listKpiProjectIotDetailByDateRange(projectIds, startDate, endDate);
    }


    // 北向推送KPI相关

    @Override
    public List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntity() {
        // 北向推送支持的推送平台
        var northPushList = listNorthPushSupportPlatform();
        if (CollectionUtils.isEmpty(northPushList)) {
            return null;
        }
        var projectCountMap = new HashMap<String, OfflineCountResponseDTO.TotalAndOffline>(64);
        // 根据北向推送支持的平台获取相关的项目总数和离线个数
        northPushList.forEach(info -> {
            if (!StringUtils.hasText(info.getVSysvalue5()) || "null".equals(info.getVSysvalue5())) {
                return;
            }
            var groupId = info.getVSysvalue5();
            var projectStatistics = getNorthPushOfflineByGroup(groupId);
            projectCountMap.putAll(projectStatistics);
        });
        if (projectCountMap.isEmpty()) {
            return null;
        }
        var projectNameMap = tblProjectDao.listProjectName()
                .stream()
                .collect(Collectors.toMap(ProjectNameEntity::getProjectId, ProjectNameEntity::getProjectName));
        var res = new ArrayList<KpiProjectNorthPushEntity>();
        var day = DateUtil.today();
        for (var entry : projectCountMap.entrySet()) {
            var projectId = entry.getKey();
            res.add(KpiProjectNorthPushEntity.builder()
                    .projectId(projectId)
                    .projectName(projectNameMap.getOrDefault(projectId, ""))
                    .day(day)
                    .elevatorTotal(entry.getValue().getTotal())
                    .elevatorOfflineRealtime(entry.getValue().getOffline())
                    .build());

        }
        return res;
    }

    @Override
    public void saveKpiProjectNorthPushEntity(KpiProjectNorthPushEntity kpiProjectNorthPushEntity) {
        var recordInDb = kpiProjectNorthPushMapper.getKpiProjectNorthPushEntityByProjectIdAndDate(
                kpiProjectNorthPushEntity.getProjectId(), kpiProjectNorthPushEntity.getDay());
        if (recordInDb == null) {
            kpiProjectNorthPushEntity.setElevatorOfflineMax(kpiProjectNorthPushEntity.getElevatorOfflineRealtime());
            kpiProjectNorthPushMapper.insert(kpiProjectNorthPushEntity);
        } else {
            kpiProjectNorthPushEntity.setId(recordInDb.getId());
            kpiProjectNorthPushEntity.setElevatorOfflineMax(Math.max(recordInDb.getElevatorOfflineMax(),
                    kpiProjectNorthPushEntity.getElevatorOfflineRealtime()));
            kpiProjectNorthPushMapper.updateById(kpiProjectNorthPushEntity);
        }
    }

    @Override
    public KpiProjectNorthPushNotifyDTO buildProjectNorthPushNotifyInfo(KpiProjectNorthPushEntity kpiProjectNorthPushEntity) {
        // 获取昨日的项目KPI记录
        var yesterdayKpiProjectNorthPushEntity = getKpiProjectNorthPushEntityByProjectIdAndDate(
                kpiProjectNorthPushEntity.getProjectId(), DateUtil.formatDate(DateUtil.yesterday()));

        var projectNotifyDTO = KpiProjectNorthPushNotifyDTO.builder()
                .projectId(kpiProjectNorthPushEntity.getProjectId())
                .projectName(kpiProjectNorthPushEntity.getProjectName())
                .elevatorTotal(kpiProjectNorthPushEntity.getElevatorTotal())
                .elevatorOfflineRealtime(kpiProjectNorthPushEntity.getElevatorOfflineRealtime())
                .yesterdayElevatorOfflineMax(yesterdayKpiProjectNorthPushEntity == null
                        ? 0 : yesterdayKpiProjectNorthPushEntity.getElevatorOfflineMax())
                .build();

        // 1. 计算设备在线率是否低于x%
        buildElevatorOnlineRateNotifyMessage(kpiProjectNorthPushEntity, projectNotifyDTO);
        // 2. 比较设备在线是否比上一日低x个
        buildElevatorChangeFromYesterdayNotifyMessage(kpiProjectNorthPushEntity, yesterdayKpiProjectNorthPushEntity,
                projectNotifyDTO);
        return projectNotifyDTO;
    }

    /**
     * 建议使用 listStatisticsByProjectIdsForNorthPush
     *
     * @param projectIds 项目ID列表
     * @return
     */
    @Deprecated
    @Override
    public List<KpiProjectNorthPushStatisticsDTO> listNorthPushStatisticsByProjectIds(List<String> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return Collections.emptyList();
        }
        var kpiToday = listKpiProjectNorthPushEntityByProjectIdsAndDay(projectIds, DateUtil.today());
        if (CollectionUtils.isEmpty(kpiToday)) {
            return Collections.emptyList();
        }
        var kpiYesterday = listKpiProjectNorthPushEntityByProjectIdsAndDay(projectIds,
                DateUtil.formatDate(DateUtil.yesterday()))
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushEntity::getProjectId, Function.identity()));
        var theWeek = listKpiProjectNorthPushEntityByWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        var lastWeek = listKpiProjectNorthPushEntityByLastWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        var theMonth = listKpiProjectNorthPushEntityByMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        var lastMonth = listKpiProjectNorthPushEntityByLastMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        // 按项目组装数据
        var res = new ArrayList<KpiProjectNorthPushStatisticsDTO>(32);
        kpiToday.forEach(kpi -> {
            var statistics = KpiProjectNorthPushStatisticsDTO.builder()
                    .projectId(kpi.getProjectId())
                    .projectName(StringUtils.hasText(kpi.getProjectName()) ? kpi.getProjectName() : kpi.getProjectId())
                    .elevatorTotal(kpi.getElevatorTotal())
                    .elevatorOfflineRealtime(kpi.getElevatorOfflineRealtime())
                    .abnormalStatus(checkAbnormalStatus(kpi))
                    .dayCompare(KpiProjectNorthPushStatisticsDTO.NorthPushStatistics.builder()
                            .elevatorOfflineRealtime(kpi.getElevatorOfflineRealtime())
                            .elevatorOfflineLastTime(getElevatorOfflineLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .build())
                    .weekCompare(KpiProjectNorthPushStatisticsDTO.NorthPushStatistics.builder()
                            .elevatorOfflineRealtime(getElevatorOfflineAvg(theWeek.get(kpi.getProjectId())))
                            .elevatorOfflineLastTime(getElevatorOfflineAvg(lastWeek.get(kpi.getProjectId())))
                            .build())
                    .monthCompare(KpiProjectNorthPushStatisticsDTO.NorthPushStatistics.builder()
                            .elevatorOfflineRealtime(getElevatorOfflineAvg(theMonth.get(kpi.getProjectId())))
                            .elevatorOfflineLastTime(getElevatorOfflineAvg(lastMonth.get(kpi.getProjectId())))
                            .build())
                    .build();
            res.add(statistics);
        });
        // 按是否正常排序
        return res.stream()
                .sorted(Comparator.comparing(KpiProjectNorthPushStatisticsDTO::getAbnormalStatus, Comparator.reverseOrder())
                        .thenComparing(KpiProjectNorthPushStatisticsDTO::getElevatorTotal, Comparator.reverseOrder()))
                .distinct()
                .toList();
    }

    @Override
    public List<KpiProjectStatisticsDTO> listStatisticsByProjectIdsForNorthPush(List<String> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return Collections.emptyList();
        }
        var kpiToday = listKpiProjectNorthPushEntityByProjectIdsAndDay(projectIds, DateUtil.today());
        if (CollectionUtils.isEmpty(kpiToday)) {
            return Collections.emptyList();
        }
        var kpiYesterday = listKpiProjectNorthPushEntityByProjectIdsAndDay(projectIds,
                DateUtil.formatDate(DateUtil.yesterday()))
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushEntity::getProjectId, Function.identity()));
        var theWeek = listKpiProjectNorthPushEntityByWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        var lastWeek = listKpiProjectNorthPushEntityByLastWeek(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        var theMonth = listKpiProjectNorthPushEntityByMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        var lastMonth = listKpiProjectNorthPushEntityByLastMonth(projectIds)
                .stream()
                .collect(Collectors.toMap(KpiProjectNorthPushDTO::getProjectId, Function.identity()));
        // 按项目组装数据
        var res = new ArrayList<KpiProjectStatisticsDTO>(32);
        kpiToday.forEach(kpi -> {
            var statistics = KpiProjectStatisticsDTO.builder()
                    .projectId(kpi.getProjectId())
                    .projectName(StringUtils.hasText(kpi.getProjectName()) ? kpi.getProjectName() : kpi.getProjectId())
                    .elevatorTotal(kpi.getElevatorTotal())
                    .abnormalStatus(checkAbnormalStatus(kpi))
                    .elevatorOfflineStatistics(KpiProjectStatisticsDTO.Statistics.builder()
                            .realtime(kpi.getElevatorOfflineRealtime())
                            .todayMax(kpi.getElevatorOfflineMax())
                            .yesterdayMax(getElevatorOfflineLastTime(kpiYesterday.get(kpi.getProjectId())))
                            .weekAvg(getElevatorOfflineAvg(theWeek.get(kpi.getProjectId())))
                            .lastWeekAvg(getElevatorOfflineAvg(lastWeek.get(kpi.getProjectId())))
                            .monthAvg(getElevatorOfflineAvg(theMonth.get(kpi.getProjectId())))
                            .lastMonthAvg(getElevatorOfflineAvg(lastMonth.get(kpi.getProjectId())))
                            .build())
                    .build();
            res.add(statistics);
        });
        // 按是否正常排序
        return res.stream()
                .sorted(Comparator.comparing(KpiProjectStatisticsDTO::getAbnormalStatus, Comparator.reverseOrder())
                        .thenComparing(KpiProjectStatisticsDTO::getElevatorTotal, Comparator.reverseOrder()))
                .distinct()
                .toList();
    }

    @Override
    public List<KpiProjectNorthPushEntity> listKpiProjectNorthPushEntityByProjectIdsAndDay(List<String> projectIds, String day) {
        return kpiProjectNorthPushMapper.listKpiProjectNorthPushEntityByProjectIdsAndDay(projectIds, day);
    }

    @Override
    public List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByWeek(List<String> projectIds) {
        // 获取本周的第一天
        var firstDayOfWeek = DateUtil.formatDate(DateUtil.beginOfWeek(DateUtil.date()));
        var today = DateUtil.today();
        return kpiProjectNorthPushMapper.getKpiProjectNorthPushByDateRange(projectIds, firstDayOfWeek, today);
    }

    @Override
    public List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByLastWeek(List<String> projectIds) {
        // 获取上周的第一天
        var firstDayOfLastWeek = DateUtil.formatDate(DateUtil.beginOfWeek(DateUtil.lastWeek()));
        // 获取上周最后一天
        var lastDayOfLastWeek = DateUtil.formatDate(DateUtil.endOfWeek(DateUtil.lastWeek()));
        return kpiProjectNorthPushMapper.getKpiProjectNorthPushByDateRange(projectIds, firstDayOfLastWeek, lastDayOfLastWeek);
    }

    @Override
    public List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByMonth(List<String> projectIds) {
        // 获取本月的第一天
        var firstDayOfMonth = DateUtil.formatDate(DateUtil.beginOfMonth(DateUtil.date()));
        var today = DateUtil.today();
        return kpiProjectNorthPushMapper.getKpiProjectNorthPushByDateRange(projectIds, firstDayOfMonth, today);
    }

    @Override
    public List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByLastMonth(List<String> projectIds) {
        // 获取上月的第一天
        var firstDayOfLastMonth = DateUtil.formatDate(DateUtil.beginOfMonth(DateUtil.lastMonth()));
        // 获取上月最后一天
        var lastDayOfLastMonth = DateUtil.formatDate(DateUtil.endOfMonth(DateUtil.lastMonth()));
        return kpiProjectNorthPushMapper.getKpiProjectNorthPushByDateRange(projectIds, firstDayOfLastMonth, lastDayOfLastMonth);
    }

    @Override
    public List<KpiProjectNorthPushDTO> listKpiProjectNorthPushEntityByDay(List<String> projectIds, int days) {
        // 获取days天前的日期
        var daysAgo = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -days));
        var today = DateUtil.today();
        return kpiProjectNorthPushMapper.getKpiProjectNorthPushByDateRange(projectIds, daysAgo, today);
    }

    @Override
    public List<KpiProjectNorthPushEntity> listKpiProjectNorthPushDetailByProjectIdsAndDateRange(
            List<String> projectIds, String startDate, String endDate) {
        return kpiProjectNorthPushMapper.listKpiProjectNorthPushDetailByDateRange(projectIds, startDate, endDate);
    }

    // 单步查询逻辑

    @Override
    public List<KpiProjectElevatorCountEntity> countInstalledElevatorGroupByProject() {
        return tblElevatorDao.countInstalledElevatorGroupByProject();
    }

    @Override
    public List<KpiProjectElevatorCountEntity> countOfflineElevatorGroupByProject() {
        return tblElevatorDao.countOfflineElevatorGroupByProject();
    }

    @Override
    public List<KpiProjectElevatorCountEntity> countFaultElevatorGroupByProject() {
        // 故障不可直接在 tbl_elevator 表中统计，而是通过 tbl_fault 表关联来统计的
        return bizElevatorDao.countFaultElevatorGroupByProject();
    }

    @Override
    public List<KpiProjectElevatorCountEntity> countCameraGroupByProject() {
        return bizCameraDao.countCameraGroupByProject();
    }

    @Override
    public List<KpiProjectElevatorCountEntity> countCameraOfflineGroupByProject() {
        return bizCameraDao.countOfflineCameraGroupByProject();
    }

    @Override
    public KpiProjectIotEntity getKpiProjectIotEntityByProjectIdAndDate(String projectId, String day) {
        return kpiProjectIotMapper.getKpiProjectIotEntityByProjectIdAndDate(projectId, day);
    }

    @Override
    public KpiProjectNorthPushEntity getKpiProjectNorthPushEntityByProjectIdAndDate(String projectId, String day) {
        return kpiProjectNorthPushMapper.getKpiProjectNorthPushEntityByProjectIdAndDate(projectId, day);
    }


    // 用户相关


    @Override
    public List<TblSysUserWecomEntity> listWecomUserByProjectId(String projectId) {
        var json = redisService.getWecomUserList(projectId);
        if (StringUtils.hasText(json)) {
            return JSON.parseArray(json, TblSysUserWecomEntity.class);
        }
        var elevatorIds = tblElevatorDao.getElevatorIdsByProjectId(projectId);
        if (CollectionUtils.isEmpty(elevatorIds)) {
            return null;
        }
        var userIds = userResourceService.getUserIdListByResourceIdList(elevatorIds);
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        var res = tblSysUserWecomService.listByUserIds(userIds)
                .stream()
                // 过滤掉删除的记录
                .filter(item -> item.getDeleted() == 0)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(res)) {
            redisService.setWecomUserList(projectId, JSON.toJSONString(res));
        }
        return res;
    }

    @Override
    public List<TblSysUserWecomEntity> listWecomUserAdmin() {
        var res = new ArrayList<TblSysUserWecomEntity>();
        WECOM_DEVELOP_USER_MAP_LIST.forEach(map -> {
            res.add(TblSysUserWecomEntity.builder()
                    .userId("shmashine")
                    .wecomCorpId(WeComBaseProperties.CORP_ID)
                    .wecomCorpName(WeComBaseProperties.CORP_NAME)
                    .wecomAgentId(String.valueOf(WeComBaseProperties.MONITOR_APP_AGENT_ID))
                    .wecomUserId(map.get(WECOM_USER_ID_KEY))
                    .wecomUserName(map.get(WECOM_USER_NAME_KEY))
                    .build());
        });
        return res;
    }

    @Override
    public void notifyWecomUsers(List<TblSysUserWecomEntity> wecomUsers, KpiProjectIotNotifyDTO notifyDTO) {
        // 发送消息
        log.info("KpiProjectTask-notifyProjectPeople 发送消息给项目负责人 {}， 项目：{} （{}）",
                wecomUsers.stream().map(TblSysUserWecomEntity::getWecomUserName).collect(Collectors.joining(",")),
                notifyDTO.getProjectName(), notifyDTO.getProjectId());
        var message = buildMarkDownContentAndCheckRedis(notifyDTO);
        if (!StringUtils.hasText(message)) {
            return;
        }
        sendMarkDownMessage(wecomUsers, message);
    }

    @Override
    public void notifyWecomUsers(List<TblSysUserWecomEntity> wecomUsers, KpiProjectNorthPushNotifyDTO notifyDTO) {
        // 发送消息
        log.info("KpiProjectTask-notifyProjectPeople 发送消息给项目负责人 {}, 项目：{} （{}）",
                wecomUsers.stream().map(TblSysUserWecomEntity::getWecomUserName).collect(Collectors.joining(",")),
                notifyDTO.getProjectName(), notifyDTO.getProjectId());
        var message = buildMarkDownContentAndCheckRedis(notifyDTO);
        if (!StringUtils.hasText(message)) {
            return;
        }
        sendMarkDownMessage(wecomUsers, message);
    }

    // 内部方法

    /**
     * 获取当前最大离线数量： 当前离线数量+今天出故障但也已经恢复的
     *
     * @return 最大离线数量
     */
    public List<KpiProjectElevatorCountEntity> countMaxFaultElevatorGroupByProject() {
        // 故障不可直接在 tbl_elevator 表中统计，而是通过 tbl_fault 表关联来统计的
        return bizElevatorDao.countMaxFaultElevatorGroupByProject();
    }

    /**
     * 计算设备在线率是否低于x%
     *
     * @param kpiProjectIotEntity 实时记录
     * @param projectNotifyDTO    通知消息
     */
    private void buildElevatorOnlineRateNotifyMessage(KpiProjectIotEntity kpiProjectIotEntity,
                                                      KpiProjectIotNotifyDTO projectNotifyDTO) {
        var rateInteger = Double.valueOf(OFFLINE_OR_FAULT_RATE * 100).intValue();
        var elevatorOfflineRate = kpiProjectIotEntity.getElevatorOfflineRealtime()
                / Double.valueOf(kpiProjectIotEntity.getElevatorTotal());
        if (elevatorOfflineRate > OFFLINE_OR_FAULT_RATE) {
            var message = "当前设备在线率低于" + (100 - rateInteger) + "%，"
                    + "为" + Double.valueOf((1 - elevatorOfflineRate) * 100).intValue() + "%，"
                    + "离线数/总数：" + kpiProjectIotEntity.getElevatorOfflineRealtime() + "/"
                    + kpiProjectIotEntity.getElevatorTotal();
            projectNotifyDTO.setElevatorOnlineRateNotify(message);
        }
    }

    /**
     * 计算设备在线率是否低于x%
     *
     * @param kpiProjectNorthPushEntity 实时记录
     * @param projectNotifyDTO          通知消息
     */
    private void buildElevatorOnlineRateNotifyMessage(KpiProjectNorthPushEntity kpiProjectNorthPushEntity,
                                                      KpiProjectNorthPushNotifyDTO projectNotifyDTO) {
        var rateInteger = Double.valueOf(OFFLINE_OR_FAULT_RATE * 100).intValue();
        var elevatorOfflineRate = kpiProjectNorthPushEntity.getElevatorOfflineRealtime()
                / Double.valueOf(kpiProjectNorthPushEntity.getElevatorTotal());
        if (elevatorOfflineRate > OFFLINE_OR_FAULT_RATE) {
            var message = "当前北向推送在线率低于" + (100 - rateInteger) + "%，"
                    + "为" + Double.valueOf((1 - elevatorOfflineRate) * 100).intValue() + "%，"
                    + "离线数/总数：" + kpiProjectNorthPushEntity.getElevatorOfflineRealtime() + "/"
                    + kpiProjectNorthPushEntity.getElevatorTotal();
            projectNotifyDTO.setElevatorOnlineRateNotify(message);
        }
    }

    /**
     * 计算设备故障率是否高于x%
     *
     * @param kpiProjectIotEntity 实时记录
     * @param projectNotifyDTO    通知消息
     */
    private void buildElevatorFaultRateNotifyMessage(KpiProjectIotEntity kpiProjectIotEntity,
                                                     KpiProjectIotNotifyDTO projectNotifyDTO) {
        var rateInteger = Double.valueOf(OFFLINE_OR_FAULT_RATE * 100).intValue();
        var elevatorFaultRate = kpiProjectIotEntity.getElevatorFaultRealtime()
                / Double.valueOf(kpiProjectIotEntity.getElevatorTotal());
        if (elevatorFaultRate > OFFLINE_OR_FAULT_RATE) {
            var message = "当前设备故障率高于" + rateInteger + "%，"
                    + "为" + Double.valueOf(elevatorFaultRate * 100).intValue() + "%，"
                    + "故障数/总数：" + kpiProjectIotEntity.getElevatorFaultRealtime() + "/"
                    + kpiProjectIotEntity.getElevatorTotal();
            projectNotifyDTO.setElevatorFaultRateNotify(message);
        }
    }

    /**
     * 计算摄像头在线率是否低于x%
     *
     * @param kpiProjectIotEntity 实时记录
     * @param projectNotifyDTO    通知消息
     */
    private void buildCameraOnlineRateNotifyMessage(KpiProjectIotEntity kpiProjectIotEntity,
                                                    KpiProjectIotNotifyDTO projectNotifyDTO) {
        var rateInteger = Double.valueOf(OFFLINE_OR_FAULT_RATE * 100).intValue();
        var cameraOfflineRate = kpiProjectIotEntity.getCameraOfflineRealtime()
                / Double.valueOf(kpiProjectIotEntity.getCameraTotal());
        if (cameraOfflineRate > OFFLINE_OR_FAULT_RATE) {
            var message = "当前摄像头在线率低于" + (100 - rateInteger) + "%，"
                    + "为" + Double.valueOf((1 - cameraOfflineRate) * 100).intValue() + "%，"
                    + "离线数/总数：" + kpiProjectIotEntity.getCameraOfflineRealtime() + "/"
                    + kpiProjectIotEntity.getCameraTotal();
            projectNotifyDTO.setCameraOnlineRateNotify(message);
        }
    }

    /**
     * 比较设备在线是否比上一日低x个
     *
     * @param kpiProjectIotEntity          实时记录
     * @param yesterdayKpiProjectIotEntity 昨日记录
     * @param projectNotifyDTO             通知消息
     */
    private void buildElevatorChangeFromYesterdayNotifyMessage(KpiProjectIotEntity kpiProjectIotEntity,
                                                               KpiProjectIotEntity yesterdayKpiProjectIotEntity,
                                                               KpiProjectIotNotifyDTO projectNotifyDTO) {
        if (yesterdayKpiProjectIotEntity == null) {
            return;
        }
        var elevatorOfflineChange = kpiProjectIotEntity.getElevatorOfflineRealtime()
                - yesterdayKpiProjectIotEntity.getElevatorOfflineMax();
        if (elevatorOfflineChange > CHANGE_STEP) {
            var message = "当前设备在线数比上一日低" + elevatorOfflineChange + "个，"
                    + "为" + (kpiProjectIotEntity.getElevatorTotal() - kpiProjectIotEntity.getElevatorOfflineRealtime())
                    + "个";
            projectNotifyDTO.setElevatorOnlineLowerNotify(message);
        }
    }

    /**
     * 比较设备在线是否比上一日低x个
     *
     * @param kpiProjectNorthPushEntity          实时记录
     * @param yesterdayKpiProjectNorthPushEntity 昨日记录
     * @param projectNotifyDTO                   通知消息
     */
    private void buildElevatorChangeFromYesterdayNotifyMessage(KpiProjectNorthPushEntity kpiProjectNorthPushEntity,
                                                               KpiProjectNorthPushEntity yesterdayKpiProjectNorthPushEntity,
                                                               KpiProjectNorthPushNotifyDTO projectNotifyDTO) {
        if (yesterdayKpiProjectNorthPushEntity == null) {
            return;
        }
        var elevatorOfflineChange = kpiProjectNorthPushEntity.getElevatorOfflineRealtime()
                - yesterdayKpiProjectNorthPushEntity.getElevatorOfflineMax();
        if (elevatorOfflineChange > CHANGE_STEP) {
            var message = "当前设备在线数比上一日低" + elevatorOfflineChange + "个，"
                    + "为" + (kpiProjectNorthPushEntity.getElevatorTotal() - kpiProjectNorthPushEntity.getElevatorOfflineRealtime())
                    + "个";
            projectNotifyDTO.setElevatorOnlineLowerNotify(message);
        }
    }

    /**
     * 比较设备故障是否比上一日高x个
     *
     * @param kpiProjectIotEntity          实时记录
     * @param yesterdayKpiProjectIotEntity 昨日记录
     * @param projectNotifyDTO             通知消息
     */
    private void buildElevatorFaultChangeFromYesterdayNotifyMessage(KpiProjectIotEntity kpiProjectIotEntity,
                                                                    KpiProjectIotEntity yesterdayKpiProjectIotEntity,
                                                                    KpiProjectIotNotifyDTO projectNotifyDTO) {
        if (yesterdayKpiProjectIotEntity == null) {
            return;
        }
        var elevatorFaultChange = kpiProjectIotEntity.getElevatorFaultRealtime()
                - yesterdayKpiProjectIotEntity.getElevatorFaultMax();
        if (elevatorFaultChange > CHANGE_STEP) {
            var message = "当前设备故障数比上一日高" + elevatorFaultChange + "个，"
                    + "为" + kpiProjectIotEntity.getElevatorFaultRealtime() + "个";
            projectNotifyDTO.setElevatorFaultHigherNotify(message);
        }
    }

    /**
     * 比较摄像头在线是否比上一日低x个
     *
     * @param kpiProjectIotEntity          实时记录
     * @param yesterdayKpiProjectIotEntity 昨日记录
     * @param projectNotifyDTO             通知消息
     */
    private void buildCameraChangeFromYesterdayNotifyMessage(KpiProjectIotEntity kpiProjectIotEntity,
                                                             KpiProjectIotEntity yesterdayKpiProjectIotEntity,
                                                             KpiProjectIotNotifyDTO projectNotifyDTO) {
        if (yesterdayKpiProjectIotEntity == null) {
            return;
        }
        var cameraOfflineChange = kpiProjectIotEntity.getCameraOfflineRealtime()
                - yesterdayKpiProjectIotEntity.getCameraOfflineMax();
        if (cameraOfflineChange > CHANGE_STEP) {
            var message = "当前摄像头在线数比上一日低" + cameraOfflineChange + "个，"
                    + "为" + (kpiProjectIotEntity.getCameraTotal() - kpiProjectIotEntity.getCameraOfflineRealtime())
                    + "个";
            projectNotifyDTO.setCameraOnlineLowerNotify(message);
        }
    }


    /**
     * 生成MarkDown文本内容, IOT
     *
     * @param notifyDTO 通知消息
     */
    private String buildMarkDownContentAndCheckRedis(KpiProjectIotNotifyDTO notifyDTO) {
        var notifyContentMap = new HashMap<String, String>();
        var projectId = notifyDTO.getProjectId();
        checkAndSetNotifyContent(notifyDTO.getElevatorOnlineRateNotify(), projectId,
                WecomNotifyTypeEnum.ELEVATOR_ONLINE_RATE, notifyContentMap, notifyDTO.getElevatorOfflineRealtime());
        checkAndSetNotifyContent(notifyDTO.getElevatorFaultRateNotify(), projectId,
                WecomNotifyTypeEnum.ELEVATOR_FAULT_RATE, notifyContentMap, notifyDTO.getElevatorFaultRealtime());
        checkAndSetNotifyContent(notifyDTO.getCameraOnlineRateNotify(), projectId,
                WecomNotifyTypeEnum.CAMERA_ONLINE_RATE, notifyContentMap, notifyDTO.getCameraOfflineRealtime());
        checkAndSetNotifyContent(notifyDTO.getElevatorOnlineLowerNotify(), projectId,
                WecomNotifyTypeEnum.ELEVATOR_ONLINE_LOWER, notifyContentMap, notifyDTO.getElevatorOfflineRealtime());
        checkAndSetNotifyContent(notifyDTO.getElevatorFaultHigherNotify(), projectId,
                WecomNotifyTypeEnum.ELEVATOR_FAULT_HIGHER, notifyContentMap, notifyDTO.getElevatorFaultRealtime());
        checkAndSetNotifyContent(notifyDTO.getCameraOnlineLowerNotify(), projectId,
                WecomNotifyTypeEnum.CAMERA_ONLINE_LOWER, notifyContentMap, notifyDTO.getCameraOfflineRealtime());
        if (CollectionUtils.isEmpty(notifyContentMap)) {
            return null;
        }
        var title = "项目【" + notifyDTO.getProjectName() + "】的KPI消息通知-告警";
        var content = new StringBuilder(WeComMarkdownStringConvert.buildTitle(title, 5))
                .append(WeComMarkdownStringConvert.NEW_LINE)
                .append("---")
                .append(WeComMarkdownStringConvert.NEW_LINE);
        var i = 1;
        for (var notifyContent : notifyContentMap.entrySet()) {
            content.append(WeComMarkdownStringConvert.boldStringInline("通知" + i + "："))
                    .append(WecomNotifyTypeEnum.getDescriptionBySlug(notifyContent.getKey()))
                    .append(WeComMarkdownStringConvert.NEW_LINE)
                    .append(WeComMarkdownStringConvert.boldStringInline("描述："))
                    .append(WeComMarkdownStringConvert.warningData(notifyContent.getValue()))
                    .append(WeComMarkdownStringConvert.NEW_LINE)
                    .append(WeComMarkdownStringConvert.NEW_LINE)
                    .append("---")
                    .append(WeComMarkdownStringConvert.NEW_LINE);
            i++;
        }
        content.append(WeComMarkdownStringConvert.boldString("备注："))
                .append(WeComMarkdownStringConvert.quoteData("项目ID", projectId))
                .append(WeComMarkdownStringConvert.quoteData("项目名称", notifyDTO.getProjectName()))
                .append(WeComMarkdownStringConvert.quoteData("电梯总数", String.valueOf(notifyDTO.getElevatorTotal())))
                .append(WeComMarkdownStringConvert.quoteData("电梯实时离线数",
                        String.valueOf(notifyDTO.getElevatorOfflineRealtime())))
                .append(WeComMarkdownStringConvert.quoteData("电梯实时故障数",
                        String.valueOf(notifyDTO.getElevatorFaultRealtime())))
                .append(WeComMarkdownStringConvert.quoteData("摄像头总数",
                        String.valueOf(notifyDTO.getCameraTotal())))
                .append(WeComMarkdownStringConvert.quoteData("摄像头实时离线数",
                        String.valueOf(notifyDTO.getCameraOfflineRealtime())))
                .append(WeComMarkdownStringConvert.quoteData("昨日电梯最大离线数",
                        String.valueOf(notifyDTO.getYesterdayElevatorOfflineMax())))
                .append(WeComMarkdownStringConvert.quoteData("昨日电梯最大告警线数",
                        String.valueOf(notifyDTO.getYesterdayElevatorFaultMax())))
                .append(WeComMarkdownStringConvert.quoteData("昨日摄像头最大离线数",
                        String.valueOf(notifyDTO.getYesterdayCameraOfflineMax())));

        return content.toString();
    }

    /**
     * 生成MarkDown文本内容, 北向推送
     *
     * @param notifyDTO 通知消息
     */
    private String buildMarkDownContentAndCheckRedis(KpiProjectNorthPushNotifyDTO notifyDTO) {
        var notifyContentMap = new HashMap<String, String>();
        var projectId = notifyDTO.getProjectId();
        checkAndSetNotifyContent(notifyDTO.getElevatorOnlineRateNotify(), projectId,
                WecomNotifyTypeEnum.NORTH_PUSH_ONLINE_RATE, notifyContentMap, notifyDTO.getElevatorOfflineRealtime());
        checkAndSetNotifyContent(notifyDTO.getElevatorOnlineLowerNotify(), projectId,
                WecomNotifyTypeEnum.NORTH_PUSH_ONLINE_LOWER, notifyContentMap, notifyDTO.getElevatorOfflineRealtime());
        if (CollectionUtils.isEmpty(notifyContentMap)) {
            return null;
        }
        var title = "项目【" + notifyDTO.getProjectName() + "】的北向推送KPI消息通知-告警";
        var content = new StringBuilder(WeComMarkdownStringConvert.buildTitle(title, 5))
                .append(WeComMarkdownStringConvert.NEW_LINE)
                .append("---")
                .append(WeComMarkdownStringConvert.NEW_LINE);
        var i = 1;
        for (var notifyContent : notifyContentMap.entrySet()) {
            content.append(WeComMarkdownStringConvert.boldStringInline("通知" + i + "："))
                    .append(WecomNotifyTypeEnum.getDescriptionBySlug(notifyContent.getKey()))
                    .append(WeComMarkdownStringConvert.NEW_LINE)
                    .append(WeComMarkdownStringConvert.boldStringInline("描述："))
                    .append(WeComMarkdownStringConvert.warningData(notifyContent.getValue()))
                    .append(WeComMarkdownStringConvert.NEW_LINE)
                    .append(WeComMarkdownStringConvert.NEW_LINE)
                    .append("---")
                    .append(WeComMarkdownStringConvert.NEW_LINE);
            i++;
        }
        content.append(WeComMarkdownStringConvert.boldString("备注："))
                .append(WeComMarkdownStringConvert.quoteData("项目ID", projectId))
                .append(WeComMarkdownStringConvert.quoteData("项目名称", notifyDTO.getProjectName()))
                .append(WeComMarkdownStringConvert.quoteData("电梯总数", String.valueOf(notifyDTO.getElevatorTotal())))
                .append(WeComMarkdownStringConvert.quoteData("电梯实时离线数",
                        String.valueOf(notifyDTO.getElevatorOfflineRealtime())))
                .append(WeComMarkdownStringConvert.quoteData("昨日电梯最大离线数",
                        String.valueOf(notifyDTO.getYesterdayElevatorOfflineMax())));

        return content.toString();
    }


    /**
     * 检查通知消息是否为空，不为空则添加到通知内容列表中
     *
     * @param notify             通知消息
     * @param notifyType         类型
     * @param notifyContentMap   通知内容
     * @param offlineFaultNumber 大于 最小发送数量 时才发送
     */
    private void checkAndSetNotifyContent(String notify, String projectId, WecomNotifyTypeEnum notifyType,
                                          Map<String, String> notifyContentMap, Integer offlineFaultNumber) {
        if (!StringUtils.hasText(notify)) {
            return;
        }
        // 低于阈值的不发
        if (offlineFaultNumber < MIN_THRESHOLD_SEND_MESSAGE_COUNT) {
            return;
        }
        // 当日已发送过的不发
        var hasNotify = redisService.getWecomMessageLogKey(projectId, notifyType.getSlug());
        if (StringUtils.hasText(hasNotify)) {
            return;
        }
        // 设置缓存
        redisService.setWecomMessageLogKey(projectId, notifyType.getSlug(), notify);

        notifyContentMap.put(notifyType.getSlug(), notify);
    }

    /**
     * 发送MarkDown消息
     *
     * @param wecomUsers 企业微信用户
     * @param message    消息内容
     */
    private void sendMarkDownMessage(List<TblSysUserWecomEntity> wecomUsers, String message) {
        // 按应用ID分组发送应用消息
        wecomUsers.stream()
                .collect(Collectors.groupingBy(TblSysUserWecomEntity::getWecomAgentId))
                .forEach((k, v) -> {
                    var agentId = Integer.parseInt(k);
                    var token = weComGateway.getToken(agentId);
                    var wecomUserIds = v.stream().map(TblSysUserWecomEntity::getWecomUserId)
                            .collect(Collectors.joining("|"));
                    var request = JSON.toJSONString(WeComMessageMarkDownRequestDTO.builder()
                            .toUser(wecomUserIds)
                            .agentId(agentId)
                            .messageType("markdown")
                            .markdown(WeComMessageMarkDownRequestDTO.MarkDownInfo.builder()
                                    .content(message)
                                    .build())
                            .enableDuplicateCheck(0)
                            .duplicateCheckInterval(1800)
                            .build());

                    var res = weComGateway.sendMessage(token, request);
                    log.info("发送企业微信消息，应用ID：{}，企业用户ID：{}，通知内容：{}，返回结果：{}", agentId, wecomUserIds, request, res);
                });
    }

    private List<TblSysSysteminfo> listNorthPushSupportPlatform() {
        return tblSysSysteminfoDao.getBySysId(NORTH_PUSH_SUPPORT_SYSTEM_ID);
    }

    private Map<String, OfflineCountResponseDTO.TotalAndOffline> getNorthPushOfflineByGroup(String groupId) {
        var res = restTemplate.getForObject(URL_NORTH_PUSH_STATISTICS_GET, OfflineCountResponseDTO.class, groupId);
        if (Objects.requireNonNull(res).getCode() == 0) {
            return res.getData();
        } else {
            throw new RuntimeException("获取北向推送离线项目统计失败，地址："
                    + URL_NORTH_PUSH_STATISTICS_GET.replace("{groupId}", groupId) + "，返回结果：" + res);
        }
    }


    private Integer getElevatorOfflineLastTime(KpiProjectIotEntity target) {
        return null == target ? 0
                : (null == target.getElevatorOfflineMax() ? 0 : target.getElevatorOfflineMax());
    }

    private Integer getElevatorOfflineLastTime(KpiProjectNorthPushEntity target) {
        return null == target ? 0
                : (null == target.getElevatorOfflineMax() ? 0 : target.getElevatorOfflineMax());
    }

    private Integer getElevatorFaultLastTime(KpiProjectIotEntity target) {
        return null == target ? 0
                : (null == target.getElevatorFaultMax() ? 0 : target.getElevatorFaultMax());
    }

    private Integer getElevatorFaultLastTime(KpiProjectIotDTO target) {
        return null == target ? 0
                : (null == target.getElevatorFaultMax() ? 0 : target.getElevatorFaultMax());
    }

    private Integer getCameraOfflineLastTime(KpiProjectIotEntity target) {
        return null == target ? 0
                : (null == target.getCameraOfflineMax() ? 0 : target.getCameraOfflineMax());
    }

    private Integer getElevatorOfflineAvg(KpiProjectNorthPushDTO target) {
        return null == target ? 0
                : (null == target.getElevatorOfflineAvg() ? 0 : target.getElevatorOfflineAvg());
    }

    private Integer getElevatorOfflineAvg(KpiProjectIotDTO target) {
        return null == target ? 0
                : (null == target.getElevatorOfflineAvg() ? 0 : target.getElevatorOfflineAvg());
    }

    private Integer getElevatorFaultAvg(KpiProjectIotDTO target) {
        return null == target ? 0
                : (null == target.getElevatorFaultAvg() ? 0 : target.getElevatorFaultAvg());
    }

    private Integer getCameraOfflineAvg(KpiProjectIotDTO target) {
        return null == target ? 0
                : (null == target.getCameraOfflineAvg() ? 0 : target.getCameraOfflineAvg());
    }


    private Boolean checkAbnormalStatus(KpiProjectIotEntity kpi) {
        return MIN_THRESHOLD_SEND_MESSAGE_COUNT <= kpi.getElevatorOfflineRealtime()
                || MIN_THRESHOLD_SEND_MESSAGE_COUNT <= kpi.getElevatorFaultRealtime()
                || MIN_THRESHOLD_SEND_MESSAGE_COUNT <= kpi.getCameraOfflineRealtime();
    }

    private Boolean checkAbnormalStatus(KpiProjectNorthPushEntity kpi) {
        return MIN_THRESHOLD_SEND_MESSAGE_COUNT <= kpi.getElevatorOfflineRealtime();
    }
}
