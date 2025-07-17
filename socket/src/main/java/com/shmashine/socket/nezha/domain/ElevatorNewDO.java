package com.shmashine.socket.nezha.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * ElevatorNewDO
 *
 * @author chglee
 * @version 2018-06-11 17:59:18
 */
@SuppressWarnings("checkstyle:MemberName")
@Data
public class ElevatorNewDO implements Serializable {

    private static final long serialVersionUID = 1L;

    //
    private Integer id;                    //thirdElevatorId
    //注册编号
    private String code;
    //识别码
    private String pinCode;                //elevatorWorkNo
    //电梯名称
    private String name;
    //省id
    private Integer provinceId;            //province
    //市id
    private Integer cityId;                //city
    //区县id
    private Integer areaId;                //county
    //使用地址
    private String useAddr;                //detailedAddress
    //电梯类型
    private Integer type;
    //电梯用处类型
    private Integer useType;
    //电梯使用地方类型
    private Integer placeType;            //usePlace
    //维保间隔（天）
    private Integer maintainDays;
    //最近维保时间
    private Date lastmaintainDate;
    //下次维保时间
    private Date nextmaintainDate;
    //电梯开始运行时间
    private Date startRunningTime;            //inUsedYear
    //下次年检时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date nextInspectDate;            //nextMaintDt

    //sensorTypes(1代表old，2代表sensor + 树莓派，3代表sensor + sensor, 4代表NB设备)
    private Integer sensorTypes;
    //电梯位置码
    private String elevatorSglnCode;
    //维保负责人
    private Integer maintenanceChargeUser;
    //物业负责人
    private Integer propertyChargeUser;

    private Date nowDate;
    //上次年检时间
    private Date lastInspectDate;
    //维保公司id
    private Integer maintainCompanyId;            //thirdMaintId-maintCompanyName
    //物业公司id
    private Integer propertyCompanyId;            //thirdPropertyId-propertyCompanyName
    //质检局id
    private Integer governmentId;                //thirdRegulatorId-regulatorCompanyName //inspectionInstitution
    //电梯品牌id
    private Integer brandId;                    //thirdManufactureId-manufactureCompanyName
    //维保人姓名
    private String maintainPersonName;            //maintContacts1
    //维保人手机号
    private String maintainPersonTel;            //maintContacts1Tel
    //负责人姓名
    private String chargePersonName;
    //负责人手机号
    private String chargePersonTel;
    //检修人员姓名
    private String checkPersonName;
    //检修人员电话
    private String checkPersonTel;
    //视频模式
    private String videoModel;
    //设备id
    private String deviceId;
    //摄像头id
    private String cameraId;
    //通道号
    private String channelCode;
    //经度
    private String longitude;            //longitude
    //纬度
    private String latitude;            //latitude
    //故障通知接口
    private String bugNoticeInterface;
    //短信通知号码
    private String bugNoticeTel;
    //需要短信通知的故障
    private String needNoticeBug;
    //创建时间
    private Date createDate;
    //创建人
    private String createUser;
    //更新时间
    private Date updateDate;
    //更新人
    private String updateUser;
    //删除标记
    private Integer delFlag;
    //在线状态（0未在线  1已在线）
    private Integer onLine;
    //轿顶在线状态（0未在线  1已在线）
    private Integer carRoofOnLine;
    //机房在线状态（0未在线  1已在线）
    private Integer machineyRoomOnLine;
    //平台code
    private String ptCodes;
    //消息类型
    private String messageType;
    //小区编号
    private String villageNum;
    //上线时间
    private Date onlineDate;
    //离线时间
    private Date offlineDate;
    //机房上线时间
    private Date machineyRoomOnlineDate;
    //机房离线时间
    private Date machineyRoomOfflineDate;
    //设备是否已存在151平台，0不存在，1存在，2已注销
    private Integer exist151;
    //邮箱
    private String chargePersonEmail;
    //街道
    private String street;
    //品牌型号
    private Integer brandVersionId;

    private String ccid;
    private String imei;    //设备号
    private String hw_version;  //硬件版本
    private String sw_version;  //软件版本
    private String updatemethod;    //终端升级方式

    // 网络（0：宽带 1：定向卡  2：非定向卡）
    private Integer networkType;

    //检修状态1检修 0未检修
    private Integer reconditionStatus;
    //安装状态1安装 0未安装
    private Integer installStatus;
    //电话报警状态1忽略 0不忽略
    private Integer phoneAlarmStatus;
    //项目归属
    private Integer projectId;
    //项目归属
    private String projectName;
    //监控状态1有cping,monitor 0没cping,monitor 2有cping没monitor 3没cping有monitor
    private Integer monitorStatus;
    //短信通知状态 1通知 0不通知
    private Integer sendMessageStatus;
    //电话通知状态 1通知 0不通知
    private Integer sendPhoneStatus;

    private int maxFloor;
    private int minFloor;
    private String floorSettingStatus;
    private Integer coordinateType;
    private Integer voiceStatus;
    private double speed;
    private String toppicUrl;
    private String roompicUrl;

    //楼层详细信息
    private String floorDetail;
    //应急服务电话
    private String callCenter;                //alldayTel

    private String iotKey;
    private String iotSecret;
    private String debugInfo;
    private String masterVersion;
    private String slaveVersion;
    private String rpiVersion;  //树莓派版本号
    private String devNum;  //摄像头设备序列号
    private String devUsername; //设备用户名
    private String devPswd; //设备密码
    private int runCount;   //运行次数
    private int doorTimes;  //开关门次数
    private int levelOffTimes;  //平层触发次数
    private int bendCount;  //设备钢丝绳（带）折弯次数
    private Double runDistance; //累计运行距离

    private Integer openWatingTime;
    private Integer closeWatingTime;
    private Integer repeatCount;
    private String rpiLastTime;   //树莓派最近一次请求时间
    private boolean rpiActive;  //树莓派运行是否正常

    private Integer Dt;
    private Integer Dp1;
    private Integer Dp2;
    private Integer Nps;
    private Integer Npr;
    private Double Ndaj;
    private Integer angle;
    private Integer angleType;

    //复用状态 0：正常运行模式 1：停止模式  2：检修模式 3：未知模式
    private Integer multiplexingStatus;

    //**********************************************非持久化属性************************************************/

    private String caution;
    //电梯类型
    private String typeText;
    //电梯用处类型
    private String useTypeText;
    //电梯使用地方类型
    private String placeTypeText;
    //维保公司id
    private String maintainCompanyText;
    //物业公司id
    private String propertyCompanyText;
    //电梯品牌id
    private String brandText;
    //政府部门id
    private String governmentText;
    //省id
    private String provinceText;
    //市id
    private String cityText;
    //区县id
    private String areaText;
    //电梯监控视频流地址
    private String videoSource;
    private String status;
    //故障次数
    private int failureSum;
    //轿顶电量
    private int battery;
    //机房电量
    private int machineyRoomBattery;
    // 额定载重量
    private int ratedLoad;

    //关注电梯原因
    private String cause;

    //单位内编号
    private String unitCode;
    //电梯代码
    private String elevatorEquipmentCode;
    //登记机关
    private String registrationAuthority;
    //登记机构
    private String registrationMechanism;
    //登记证编号
    private String registrationCertificateNo;


}
