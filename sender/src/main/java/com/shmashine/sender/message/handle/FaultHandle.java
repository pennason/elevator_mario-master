package com.shmashine.sender.message.handle;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.sender.entity.TblFaultShield;
import com.shmashine.sender.message.send.FaultSend;
import com.shmashine.sender.server.elevator.BizElevatorService;
import com.shmashine.sender.server.fault.TblFaultShieldService;

import lombok.extern.slf4j.Slf4j;


/**
 * 告警消息消息处理类
 *
 * @author yanjie.wu
 */
@Slf4j
@Component
public class FaultHandle {

    /**
     * 第三方平台code ： 第三方平台推送处理类
     */
    private static ConcurrentHashMap<String, FaultSend> senders = new ConcurrentHashMap<>();

    @Autowired
    private BizElevatorService bizElevatorService;

    @Autowired
    private SendToUserHandle sendToUserHandle;

    @Resource
    private TblFaultShieldService faultShieldService;

    /**
     * 需要监听故障消息的 推送到第三方平台的处理类
     */
    public static void register(FaultSend sender) {
        senders.put(sender.getPtCode(), sender);
    }

    /**
     * 将kafka的故障消息转发到具体的第三方推送平台
     */
    // CHECKSTYLE:OFF
    public void handle(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        try {
            // step1 : message转jsonObject（json格式校验）
            FaultMessage messageData = JSON.parseObject(message, FaultMessage.class);
            if (null == messageData || StringUtils.isBlank(messageData.getElevatorCode())
                    || !"Fault".equals(messageData.getTY())) {
                // 无效报文落日志
                log.info("invalid message : " + message);
                return;
            }
            if ("add".equals(messageData.getST())) {
                // 运行数据消息
                var messageJson = JSON.parseObject(message);
                if (messageJson.containsKey("D")) {
                    MonitorMessage monitorMessage = new MonitorMessage();
                    monitorMessage.setFromBase64(messageJson);
                    messageData.setMonitorMessage(monitorMessage);
                }
                log.info("fault-info : {}", JSON.toJSONString(messageData));
            }

            // 过滤平台屏蔽的北向推送故障
            String faultType = messageData.getFault_type();
            String elevatorCode = messageData.getElevatorCode();
            //查询该终端编号屏蔽的故障
            List<TblFaultShield> faultShieldList = faultShieldService.getFaultShieldByElevatorCode(elevatorCode);
            if (null != faultShieldList && faultShieldList.size() > 0) {
                for (TblFaultShield faultShield : faultShieldList) {
                    int shieldfaultType = faultShield.getiFaultType();
                    String shieldfaultTypeStr = String.valueOf(shieldfaultType);
                    if (faultType.equals(shieldfaultTypeStr)) {
                        log.info("filter push north faultShield {}", message);
                        return;
                    }
                }
            }
            // 推送消息到对接用户
            sendToUserHandle.faultMessage(messageData.getElevatorCode(), JSON.toJSONString(messageData));

            // step2 : 分发
            dispatch(messageData);
            // 报文落日志
            log.info("send fault : " + message);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    // CHECKSTYLE:ON

    /**
     * 故障消息分发到不同平台
     */
    public void dispatch(FaultMessage faultMessage) {
        String elevatorCode = faultMessage.getElevatorCode();
        // 获取电梯的推送平台code
        TblElevator elevator = bizElevatorService.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return;
        }
        String ptCodeStr = elevator.getVHttpPtCodes();
        if (StringUtils.isBlank(ptCodeStr)) {
            return;
        }
        String[] ptCodeList = ptCodeStr.split(",");
        for (String ptCode : ptCodeList) {
            FaultSend sender = senders.get(ptCode);
            if (null != sender) {
                try {
                    var tmpMessage = new FaultMessage();
                    BeanUtils.copyProperties(faultMessage, tmpMessage);
                    sender.handleFault(tmpMessage);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
