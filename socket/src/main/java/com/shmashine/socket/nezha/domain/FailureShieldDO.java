package com.shmashine.socket.nezha.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 故障上报规则表
 *
 * @author little.li
 */
@Data
public class FailureShieldDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //电梯ID
    private Long elevatorId;
    // 电梯编号
    private String elevatorCode;
    //故障ID
    private Integer failureId;
    //故障名称
    private String failureName;
    // 对用户是否可见，0 不落库，1 落库且可见，2 落库且不可见
    private Integer isUserVisible;
    //数据是否推送到其他平台，0 不上报，1 上报
    private Integer isReport;

    //创建用户
    private Integer createUser;
    //创建时间
    private Date createTime;
    //更新用户
    private Integer updateUser;
    //更新时间
    private Date updateTime;
    //删除标记，0 未删除，1 已删除
    private Integer delFlag;

}
