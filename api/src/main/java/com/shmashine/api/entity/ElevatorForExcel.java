package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

/**
 * @AUTHOR jiangheng
 * @DATA 2021/3/4 - 14:16
 * excel批量导入对应实体类
 */
@Builder
public class ElevatorForExcel implements Serializable {

    private static final long serialVersionUID = -28465663812342357L;
    /**
     * 电梯唯一ID
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 省ID
     */
    private Integer iProvinceId;
    /**
     * 市ID
     */
    private Integer iCityId;
    /**
     * 区ID
     */
    private Integer iAreaId;
    /**
     * 电梯地址
     */
    private String vAddress;
    /**
     * 电梯类型
     */
    private Integer iElevatorType;
    /**
     * 电梯使用类型
     */
    private Integer iElevatorUseType;
    /**
     * 电梯安装地类型
     */
    private Integer iElevatorPlaceType;
    /**
     * 电梯品牌ID
     */
    private String vElevatorBrandId;
    /**
     * 维保间隔（天）
     */
    private Integer iMaintainDays;
    /**
     * 上次维保日期
     */
    private Object dLastMaintainDate;
    /**
     * 下次维保日期
     */
    private Object dNextMaintainDate;
    /**
     * 上次年检时间
     */
    private Object dLastInspectDate;
    /**
     * 下次年检时间
     */
    private Object dNextInspectDate;
    /**
     * 维保公司ID
     */
    private String vMaintainCompanyId;
    /**
     * 物业公司ID
     */
    private String vPropertyCompanyId;
    /**
     * 政府部门id
     */
    private String iGovernmentId;
    /**
     * 维保人姓名
     */
    private String vMaintainPersonName;
    /**
     * 维保人手机号
     */
    private String vMaintainPersonTel;
    /**
     * 应急处理人
     */
    private String vEmergencyPersonName;
    /**
     * 应急服务电话
     */
    private String vEmergencyPersonTel;
    /**
     * http推送平台code
     */
    private String vHttpPtCodes;
    /**
     * 经度
     */
    private String vLongitude;
    /**
     * 纬度
     */
    private String vLatitude;
    /**
     * 使用地图类型 1 GPS，2 百度地图，3 高德地图
     */
    private Integer iCoordinateType;
    /**
     * 项目ID
     */
    private String vProjectId;
    /**
     * 小区ID
     */
    private String vVillageId;
    /**
     * 楼宇id
     */
    private String vBuildingId;
    /**
     * 设备安装状态 0未安装 1安装
     */
    private Integer iInstallStatus;
    /**
     * 设备安装时间
     */
    private Object dtInstallTime;
    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private Integer iOnLine;
    /**
     * 电梯服务模式 0 正常运行，1 检修模式，2 停止服务
     */
    private Integer iModeStatus;
    /**
     * 故障状态 0 未故障，1 故障
     */
    private Integer iFaultStatus;
    /**
     * 最高楼层
     */
    private Integer iMaxFloor;
    /**
     * 最低楼层
     */
    private Integer iMinFloor;
    /**
     * 楼层详细信息
     */
    private String vFloorDetail;
    /**
     * 楼层设置状态
     */
    private String vFloorSettingStatus;
    /**
     * 额定限速
     */
    private Double dcSpeed;
    /**
     * 运行次数
     */
    private Long biRunCount;
    /**
     * 开关门次数
     */
    private Long biDoorCount;
    /**
     * 钢丝绳折弯次数
     */
    private Long biBendCount;
    /**
     * 平层触发次数
     */
    private Long biLevelTriggerCount;
    /**
     * 累计运行距离（米）
     */
    private Long biRunDistanceCount;
    /**
     * 曳引轮直径Dtmm
     */
    private Integer iDt;
    /**
     * 导向轮直径Dp1mm
     */
    private Integer iDt1;
    /**
     * 导向轮直径Dp2mm
     */
    private Integer iDt2;
    /**
     * 导向轮数量
     */
    private Integer iNps;
    /**
     * 反向弯折导向轮数量
     */
    private Integer iNpr;
    /**
     * 调整系数
     */
    private Double dcNdaj;
    /**
     * 角度
     */
    private Integer iAngle;
    /**
     * 1γ,2β
     */
    private Integer iAngleType;
    /**
     * 电梯位置码
     */
    private String vSglnCode;
    /**
     * 单位内编号
     */
    private String vUnitCode;
    /**
     * 出厂编号
     * 新装电梯
     * （尚未取得
     * 电梯注册
     * 码）必填
     */
    private String vLeaveFactoryNumber;
    /**
     * 制造单位统一社会信
     * 用代码 新装电梯
     * （尚未取得
     * 电梯注册
     * 码）必填
     */
    private String vManufacturerCode;
    /**
     * 电梯统一的注册编号
     */
    private String vEquipmentCode;
    /**
     * 登记机关
     */
    private String vRegistrationAuthority;
    /**
     * 登记机构
     */
    private String vRegistrationMechanism;
    /**
     * 登记证编号
     */
    private String vRegistrationCertificateNo;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

}
