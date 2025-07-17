// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/21 11:37
 * @since v1.0
 */

@Data
@ToString
public class ResponseDeviceEntityHaikang implements Serializable {
    /**
     * 200 操作成功 请求成功
     * 10001 参数错误 参数为空或格式不正确
     * 10002 accessToken异常或过期 重新获取accessToken
     * 10005 appKey异常appKey被冻结
     * 20002 设备不存在
     * 20014 deviceSerial不合法
     * 20018 该用户不拥有该设备 检查设备是否属于当前账户
     * 49999 数据异常 接口调用异常
     */
    private String code;
    private String msg;
    private DeviceInfoHaikang data;

    /**
     * 设备信息
     */
    @Data
    @ToString
    public static class DeviceInfoHaikang implements Serializable {
        /**
         * 设备序列号
         */
        private String deviceSerial;
        /**
         * 设备名称
         */
        private String deviceName;
        /**
         * 设备上报名称
         */
        private String localName;
        /**
         * 设备型号，如CS-C2S-21WPFR-WX
         */
        private String model;
        /**
         * 在线状态：0-不在线，1-在线
         */
        private Integer status;
        /**
         * 具有防护能力的设备布撤防状态：0-睡眠，8-在家，16-外出，普通IPC布撤防状态：0-撤防，1-布防
         */
        private Integer defence;
        /**
         * 是否加密：0-不加密，1-加密
         */
        private Integer isEncrypt;
        /**
         * 告警声音模式：0-短叫，1-长叫，2-静音
         */
        private Integer alarmSoundMode;
        /**
         * 设备下线是否通知：0-不通知 1-通知
         */
        private Integer offlineNotify;
        /**
         * 设备大类
         */
        private String category;
        /**
         * 设备二级类目
         */
        private String parentCategory;
        /**
         * 修改时间
         */
        private Long updateTime;
        /**
         * 网络类型，如有线连接wire
         */
        private String netType;
        /**
         * 信号强度(%)
         */
        private String signal;
        /**
         * 设备风险安全等级，0-安全，大于零，有风险，风险越高，值越大
         */
        private Integer riskLevel;
        /**
         * 设备IP地址
         */
        private String netAddress;
    }

}
