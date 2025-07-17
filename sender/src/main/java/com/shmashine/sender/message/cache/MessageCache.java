package com.shmashine.sender.message.cache;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;

import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.utils.DateUtils;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Component
public class MessageCache {

    @Resource
    private CacheManager cacheManager;

    private Cache faultCache;

    @PostConstruct
    public void init() {
        faultCache = cacheManager.getCache("fault_cache");
    }

    /**
     * NOT EXIST情况缓存故障报文，存在情况直接返回缓存中的old报文
     */
    public FaultMessage cacheFaultNX(FaultMessage faultMessage) {

        // 电梯code
        String elevatorCode = faultMessage.getElevatorCode();
        // add/disapper
        String stype = faultMessage.getST();
        // 获取消息中的 故障类型
        String faultType = faultMessage.getFault_type();
        // 日期 faultMessage.getTime()
        String day = DateUtils.format(new Date());

        // 缓存故障发生时的报文  key = MX000488:day:faultType:[disappear/add]
        String faultKey = String.format("%s:%s:%s:%s", elevatorCode, day, faultType, stype);
        SimpleValueWrapper obj = (SimpleValueWrapper) faultCache.get(faultKey);

        if (obj != null) {
            return (FaultMessage) obj.get();
        } else {
            faultCache.put(faultKey, faultMessage);
            return null;
        }
    }
}
