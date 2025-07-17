package com.shmashine.fault.message.handle;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.fault.camera.service.TblCameraServiceI;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;
import com.shmashine.fault.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.kafka.KafkaProducer;
import com.shmashine.fault.redis.RedisService;
import com.shmashine.fault.user.entity.TblSysUser;
import com.shmashine.fault.user.service.TblSysUserServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * 西子扶梯故障落库处理
 *
 * @author jiangheng
 */

@Slf4j
@Component
public class EscalatorFaultHandle {


    private final TblFaultServiceI faultService;

    private final TblFaultDefinitionServiceI faultDefinitionService;

    private final TblElevatorService elevatorService;

    private final TblCameraServiceI cameraService;

    private final RedisService redisService;

    private final KafkaProducer kafkaProducer;

    private final TblSysUserServiceI userService;

    @Autowired
    public EscalatorFaultHandle(TblFaultServiceI faultService, TblFaultDefinitionServiceI faultDefinitionService,
                                TblElevatorService elevatorService, TblCameraServiceI cameraService,
                                RedisService redisService, KafkaProducer kafkaProducer,
                                TblSysUserServiceI userService) {

        this.faultService = faultService;
        this.faultDefinitionService = faultDefinitionService;
        this.elevatorService = elevatorService;
        this.cameraService = cameraService;
        this.redisService = redisService;
        this.kafkaProducer = kafkaProducer;
        this.userService = userService;
    }

    /**
     * 新增故障
     */
    public void addFault(JSONObject messageJson) {

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultSecondType = messageJson.getString("fault_stype");

        // 获取故障业务id（全系统唯一标识）
        String faultId = messageJson.getString("faultId");

        // 判断改故障是否已经记录（幂等判断）
        TblFault fault = faultService.getById(faultId);

        // 未记录情况判断 该电梯是否处于该类故障中
        if (fault == null) {
            fault = faultService.getInFaultByFaultType(elevatorCode, faultType);
        }

        // 不存在故障（走新增故障逻辑）
        if (fault == null) {
            // redis中记录电梯为【故障中】
            redisService.updateFaultStatus(elevatorCode, 1);

            // 获取故障类型的详细信息
            TblFaultDefinition0902 faultDefinition;

            //拿到西子扶梯故障标准
            faultDefinition = faultDefinitionService.getByFaultType(faultType, 5);

            // 新增故障记录
            faultService.addEscalatorFaultByMessage(messageJson, elevatorCode, faultType,
                    faultSecondType, faultDefinition);

            // 数据库中更新电梯为 【故障中】
            elevatorService.updateFaultStatus(elevatorCode, 1);

            //如果该故障为发送短信，发送短信
            if (faultDefinition.getSendSMS() == 1) {
                //短信通知
                faultNotify(faultId, faultType, faultDefinition.getFaultName(), elevatorCode,
                        messageJson.getString("time"), "SMS_217406109");
            }

        } else {
            // 故障次数 + 1
            fault.setIFaultNum(fault.getIFaultNum() + 1);
            faultService.update(fault);
        }
    }


    /**
     * 消除故障
     */
    public void disappearFault(JSONObject messageJson) {
        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");

        // 获取故障业务id（全系统唯一标识）
        String faultId = messageJson.getString("faultId");

        // 获取故障中的故障记录
        TblFault fault = faultService.getInFaultByFaultType(elevatorCode, faultType);
        // 获取故障类型的详细信息
        TblFaultDefinition0902 faultDefinition = faultDefinitionService.getByFaultType(faultType, 5);
        // 存在故障中的故障
        if (fault != null) {

            // 消除故障表中该故障状态为 已恢复
            faultService.disappearFaultByMessage(messageJson, fault);

            // 更新电梯表状态：通过判断故障表中当前电梯处于【故障中】的故障
            List<TblFault> faultList = faultService.getInFault(elevatorCode);
            if (CollectionUtils.isEmpty(faultList)) {
                // 数据库中更新电梯为无故障
                elevatorService.updateFaultStatus(elevatorCode, 0);
                // redis中记录电梯为无故障
                redisService.updateFaultStatus(elevatorCode, 0);
            }

            //如果该故障为发送短信，发送短信
            if (faultDefinition.getSendSMS() == 1) {
                //短信通知
                faultNotify(faultId, faultType, faultDefinition.getFaultName(), elevatorCode,
                        messageJson.getString("time"), "SMS_217406108");
            }

        }

    }


    /**
     * 故障时 打电话、发短信
     *
     * @param faultId      故障表id
     * @param faultName    故障类型名称
     * @param elevatorCode 电梯编号
     */
    private void faultNotify(String faultId, String faultType, String faultName, String elevatorCode,
                             String time, String templateCode) {

        TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return;
        }

        // 获取该电梯的负责人
        List<TblSysUser> userList = userService.queryElevatorPrincipal(elevator.getVElevatorId());

        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        for (TblSysUser user : userList) {
            String telStr = user.getVMobile();
            if (StringUtils.isNotBlank(telStr)) {
                String[] tels = telStr.split(",");
                for (String tel : tels) {
                    // 是否接收故障短信 0：接收，1：不接收
                    if (user.getISendMessageStatus() == 0) {
                        //获取该电梯客户
                        String client = elevatorService.getClient(elevator.getVElevatorId());
                        // 多长时间内发送同一手机号不允许超过多少次（边界情况：传感器坏了，开关一直处于active）
                        SendMessageUtil.sendEscalatorFaultMessage(tel, faultId, elevatorCode, elevator.getVAddress(),
                                faultType, faultName, time, templateCode, client);
                    }
                }

            }
        }

    }

}
