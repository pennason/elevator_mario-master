// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.platform.city.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblSenderFaultEntity;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.FaultWithVideoPhoto;
import com.shmashine.sender.dao.TblSenderFaultMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 16:06
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FaultResendTool {
    private final TblSenderFaultMapper senderFaultMapper;
    private final CityTool cityTool;

    /**
     * 记录需要补充视频，照片的告警
     *
     * @param message 告警消息
     * @param ptCode  推送平台
     * @param entrap  是否困人
     */
    public void saveSenderFault(FaultMessage message, String ptCode, Boolean entrap) {
        if (null == message) {
            return;
        }
        if (!"add".equals(message.getST())) {
            return;
        }
        var senderFaultEntity = TblSenderFaultEntity.builder()
                .faultId(message.getFaultId())
                .elevatorCode(message.getElevatorCode())
                .pushGovern(ptCode)
                .entrap(entrap ? 1 : 0)
                .faultMessage(JSON.toJSONString(message))
                .finished(0)
                .build();
        senderFaultMapper.insert(senderFaultEntity);
    }

    /**
     * 重新发送故障信息，补充视频，照片
     *
     * @param senderFaultEntity 故障信息
     * @return 响应
     */
    public SingleResponse<String> sendFault(TblSenderFaultEntity senderFaultEntity) {
        var message = senderFaultEntity.getFaultMessage();
        var faultMessage = JSON.parseObject(message, FaultWithVideoPhoto.class);
        faultMessage.setFaultImageUrl(senderFaultEntity.getUrlPhoto());
        faultMessage.setFaultVideoUrl(senderFaultEntity.getUrlVideo());
        var ptCode = senderFaultEntity.getPushGovern();

        var entrap = senderFaultEntity.getEntrap();
        if (null != entrap && 1 == entrap) {
            log.info("checkAndSendFault sendFault {} step 3-1", JSON.toJSONString(faultMessage));
            return cityTool.sendEntrapDataToGovern(faultMessage, ptCode);
        }
        log.info("checkAndSendFault sendFault {} step 3-2", JSON.toJSONString(faultMessage));
        return cityTool.sendFaultDataToGovern(faultMessage, ptCode);
    }
}
