package com.shmashine.hkcamerabyys.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.OssInternalUtils;
import com.shmashine.hkcamerabyys.dao.HKCameraByYSDao;
import com.shmashine.hkcamerabyys.dao.TblCameraMapper;
import com.shmashine.hkcamerabyys.dao.TblFaultMapper;
import com.shmashine.hkcamerabyys.entity.HikCameraAlarmConfig;
import com.shmashine.hkcamerabyys.entity.UncivilizedBehavior37Fault;
import com.shmashine.hkcamerabyys.service.FaultService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 故障service实现类
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/2/28 14:42
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FaultServiceImpl implements FaultService {
    private final TblCameraMapper cameraMapper;
    private final TblFaultMapper faultMapper;
    private final HKCameraByYSDao hkCameraByYSDao;
    private final AliOssProperties aliOssProperties;


    @Override
    public void batteryCarFault(String elevatorId, String elevatorCode, String faultType,
                                Integer faultStatus, Date alarmTime, String url) {

        log.info("摄像头告警-电动车乘梯告警-elevatorId[{}]-elevatorCode[{}]-faultType[{}]-faultStatus[{}]-alarmTime[{}]",
                elevatorId, elevatorCode, faultType, faultStatus, alarmTime);
        //新增故障
        if (faultStatus == 1) {
            String elevatorAddress =null;
            if(elevatorCode!=null) {
                elevatorAddress=faultMapper.getElevatorAddress(elevatorCode);
            }
            //电动车乘梯告警-新增tbl_fault_uncivilized_behavior37表
            String id = IdUtil.getSnowflakeNextIdStr();
            /*String faultMessage = JSONUtil.createObj().set("elevatorCode", elevatorCode)
                    .set("ST", "add").set("uncivilizedBehaviorFlag", 1)
                    .set("TY", "Fault").set("sensorType", "SINGLEBOX").set("fault_type", 37).set("faultId", id)
                    .set("time", DateUtil.formatDateTime(alarmTime)).set("faultName", "电动车乘梯").toString();*/
            var faultMessage = Map.of("elevatorCode", elevatorCode,
                    "ST", "add",
                    "uncivilizedBehaviorFlag", 1,
                    "TY", "Fault",
                    "sensorType", "SINGLEBOX",
                    "fault_type", 37,
                    "faultId", id,
                    "time", DateUtil.formatDateTime(alarmTime),
                    "faultName", "电动车乘梯");

            UncivilizedBehavior37Fault fault = UncivilizedBehavior37Fault.builder()
                    .faultId(id).elevatorId(elevatorId).elevatorCode(elevatorCode)
                    .faultType(faultType).reportTime(alarmTime).faultMessage(JSON.toJSONString(faultMessage))
                    .vAddress(elevatorAddress)
                    .vFaultName("电动车乘梯")
                    .build();
            //落临时表
            faultMapper.insertUncivilizedBehavior37Fault(fault);

            //上传文件
            String ossUrl = "Oreo_Project/"
                    + DateUtil.today().replace("-", "/") + "/" + id + ".jpg";

            OssInternalUtils.setOSS(FileUtil.getBytesByRemotePath(url), ossUrl, aliOssProperties.getUseInternal());

            log.info("电动车乘梯告警-文件上传阿里云成功，elevatorCode[{}]-faultType[{}]-faultStatus[{}]-alarmTime[{}]",
                    elevatorCode, faultType, faultStatus, alarmTime);

            //调用图像识别
            // restTemplateSendMessage(id, OssInternalUtils.OSS_URL + ossUrl, "motorcycle");
            ImageIdentifyUtils.identifyImage(id, OssInternalUtils.OSS_URL + ossUrl,
                    ImageIdentifyUtils.IDENTIFY_TYPE_MOTOR_CYCLE, aliOssProperties.getUseInternal());
            log.info("电动车乘梯告警-调用图像识别成功，url[{}]-elevatorCode[{}]-faultType[{}]-faultStatus[{}]-alarmTime[{}]",
                    OssInternalUtils.OSS_URL, elevatorCode, faultType, faultStatus, alarmTime);

            //文件存sys_file表
            hkCameraByYSDao.addTblSysFile(IdUtil.getSnowflakeNextIdStr(), id, OssInternalUtils.OSS_URL + ossUrl, null,
                    2, new Date(), "0");

        } else if (faultStatus == 0) {
            //恢复故障
        }

    }

    @Override
    public void trappedPeopleFault(String elevatorId, String elevatorCode, String faultType,
                                   Integer faultStatus, Date alarmTime, String url) {

        log.info("摄像头告警-困人告警-elevatorId[{}]-elevatorCode[{}]-faultType[{}]-faultStatus[{}]-alarmTime[{}]",
                elevatorId, elevatorCode, faultType, faultStatus, alarmTime);
        //困人告警-新增tbl_fault_temp 表

        //调用图像识别

    }

    @Override
    public void defaultFault(String elevatorId, String elevatorCode, String faultType,
                             Integer faultStatus, Date alarmTime, String url) {

        log.info("摄像头告警-默认告警-elevatorId[{}]-elevatorCode[{}]-faultType[{}]-faultStatus[{}]-alarmTime[{}]",
                elevatorId, elevatorCode, faultType, faultStatus, alarmTime);
        // 默认告警

    }

    @Override
    public HikCameraAlarmConfig getCameraAlarmConfig(String devSerial, String alarmType) {
        return cameraMapper.getCameraAlarmConfig(devSerial, alarmType);
    }

    /**
     * 调用二次识别服务
     *
     * @param workOrderId 故障id
     * @param fileUrl     待识别图片url
     * @param type        识别类型
     */
    /*private void restTemplateSendMessage(String workOrderId, String fileUrl, String type) {
        //拼接请求参数
        String url = "http://47.105.214.0:10089/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;

        //异步请求
        try {
            HttpRequest.get(url).timeout(500).executeAsync();
        } catch (Exception e) {
            //调用超时
        }
    }*/
}
