package com.shmashine.api.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/27 10:00
 * @info 海尔摄像头推送松江电信电梯信息
 */
@Data
public class TblHaierLiftInfo {

    /**
     * 电梯ID
     */
    private Integer id;

    /**
     * 设备注册代码
     */
    private String registrationCode;

    /**
     * 电梯编号
     */
    private String liftNo;

    /**
     * 电梯所属项目id
     */
    private String projectId;

    /**
     * 电梯项目名
     */
    private String projectName;

    /**
     * 电梯门
     */
    private String liftGate;

    /**
     * 曳引比
     */
    private String traction;

    /**
     * 应急救援负责人
     */
    private String principalMan;

    /**
     * 电梯制造商
     */
    private String manufacturer;

    /**
     * 电梯额定速度
     */
    private String liftSpeed;

    /**
     * 摄像头编号
     */
    private String cameraNo;

    /**
     * 登记机关
     */
    private String registrationAuthority;

    /**
     * 是否购买电梯保险(0 否  1 是)
     */
    private Integer isInsure;

    /**
     * 有无机房(0 无1 有)
     */
    private Integer haveHouse;

    /**
     * 使用单位梯号
     */
    private String useEntitiesNum;

    /**
     * 电梯品牌
     */
    private String brandName;

    /**
     * 设备案安装地址
     */
    private String address;

    /**
     * 电梯站
     */
    private String liftStand;

    /**
     * 电梯显示楼层
     */
    private String showFloor;

    /**
     * 年检检验日期
     */
    private String examineDate;

    /**
     * 电梯安装日期
     */
    private String installationDate;

    /**
     * 监测终端设备编号
     */
    private String deviceNo;

    /**
     * 基站层
     */
    private String baseFloor;

    /**
     * 额定载重
     */
    private String liftWeight;

    /**
     * 电梯型号
     */
    private String modelName;

    /**
     * 楼宇类型（1 住宅；2 办公楼宇 ；3 商场超市 ；4 宾馆饭店 ；5 交通场所；6 医院 ；7 学校  ；8 文体娱场所；9 其他）
     */
    private String placeType;

    /**
     * 电梯层
     */
    private String liftFloor;

    /**
     * 楼栋号
     */
    private String buildingNum;

    /**
     * 年检检验单位
     */
    private String examineEntities;

    /**
     * 经纬度
     */
    private String position;

    /**
     * 应急电话
     */
    private String emergencyPhone;

    /**
     * 应急救援负责人电话
     */
    private String principalPhone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;
}
