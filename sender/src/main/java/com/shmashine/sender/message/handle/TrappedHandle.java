package com.shmashine.sender.message.handle;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.sender.message.send.TrappedSend;
import com.shmashine.sender.server.elevator.BizElevatorService;


/**
 * 困人消息消息处理类
 *
 * @author yanjie.wu
 */
@Component
public class TrappedHandle {

    /**
     * 无效消息日志
     */
    private static Logger invalidMessageLogger = LoggerFactory.getLogger("invalidMessageLogger");

    /**
     * 失败的消息处理日志
     */
    private static Logger failHandlelogger = LoggerFactory.getLogger("faultSendLogger");

    /**
     * 第三方平台code ： 第三方平台推送处理类
     */
    private static ConcurrentHashMap<String, TrappedSend> senders = new ConcurrentHashMap<>();

    @Autowired
    private BizElevatorService bizElevatorService;

    @Autowired
    private SendToUserHandle sendToUserHandle;


    /**
     * 注册订阅
     */
    public static void register(TrappedSend sender) {
        senders.put(sender.getPtCode(), sender);
    }

    /**
     * 将kafka的故障消息转发到具体的第三方推送平台
     */
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
                invalidMessageLogger.info("invalid message : " + message);
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
                failHandlelogger.info("fault-info : {}", JSON.toJSONString(messageData));
            }

            // 推送消息到对接用户
            sendToUserHandle.faultMessage(messageData.getElevatorCode(), JSON.toJSONString(messageData));

            String stype = messageData.getST();
            // 新增困人及时推送
            dispatch(messageData);

            // 报文落日志
            failHandlelogger.info("send trapped : " + message);

        } catch (Exception e) {
            e.printStackTrace();
            invalidMessageLogger.error(e.getMessage());
        }
    }

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
            TrappedSend sender = senders.get(ptCode);
            if (null != sender) {
                try {
                    var tmpMessage = new FaultMessage();
                    BeanUtils.copyProperties(faultMessage, tmpMessage);
                    sender.handleTrapped(tmpMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    invalidMessageLogger.error(e.getMessage());
                }
            }
        }
    }

}
