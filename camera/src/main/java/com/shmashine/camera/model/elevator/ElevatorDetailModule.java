package com.shmashine.camera.model.elevator;

import java.util.Date;

import lombok.Data;


/**
 * 电梯详情接口返回对象
 *
 * @author Dean Winchester
 */
@Data
public class ElevatorDetailModule {
    /**
     * 电梯唯一ID
     */
    private String elevatorId;
    /**
     * 电梯编号
     */
    private String elevatorCode;


    /**
     * 电梯地址
     */
    private String address;
    /**
     * 电梯类型
     */
    private Integer elevatorType;
    /**
     * 电梯类型
     */
    private String elevatorTypeName;


    /**
     * 经度
     */
    private String longitude;


    /**
     * 纬度
     */
    private String latitude;

    /**
     * 设备品种
     */
    private String elevatorUseType;

    /**
     * 设备代码
     */
    private String equipmentCode;

    /**
     * 位置码
     */
    private String saganCode;

    /**
     * 登记机关
     */
    private String registrationAuthority;

    /**
     * 检验机构
     */
    private String registrationMechanism;

    /**
     * 登记证编号
     */
    private String registrationCertificateNo;


    /**
     * 电梯品牌ID
     */
    private String elevatorBrandName;
    /**
     * 维保公司ID
     */
    private String maintainCompanyName;
    /**
     * 物业公司ID
     */
    private String propertyCompanyName;
    /**
     * 政府部门id
     */
    private String governmentName;
    /**
     * 维保人姓名
     */
    private String maintainPersonName;
    /**
     * 维保人手机号
     */
    private String maintainPersonTel;
    /**
     * 应急处理人
     */
    private String emergencyPersonName;
    /**
     * 应急服务电话
     */
    private String emergencyPersonTel;


    /**
     * 设备安装时间
     */
    private Date installTime;
    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private Integer onLine;


    /**
     * 运行次数
     */
    private Long runCount;
    /**
     * 开关门次数
     */
    private Long doorCount;
    /**
     * 钢丝绳折弯次数
     */
    private Long bendCount;
    /**
     * 平层触发次数
     */
    private Long levelTriggerCount;
    /**
     * 累计运行距离（米）
     */
    private Long runDistanceCount;


    /**
     * 上次维保日期
     */
    private Date lastMaintainDate;
    /**
     * 下次维保日期
     */
    private Date nextMaintainDate;
    /**
     * 上次年检时间
     */
    private Date lastInspectDate;
    /**
     * 下次年检时间
     */
    private Date nextInspectDate;


    /**
     * 摄像头类型 1：海康，2：雄迈
     */
    private Integer cameraType;

    /**
     * 摄像头hls流地址
     */
    private String hlsUrl;

    /**
     * rtmp流
     */
    private String rtmpUrl;
    /**
     * 私有流
     */
    private String privateUrl;

    /**
     * 摄像头序列号
     */
    private String serialNumber;

    /**
     * 摄像头云id - 雄迈
     */
    private String cloudNumber;

    /**
     * 流访问token
     */
    private String token;

}
