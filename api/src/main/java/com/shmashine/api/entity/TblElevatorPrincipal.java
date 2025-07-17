package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/5/10 10:47
 * <p>
 * 电梯——责任人表实体类
 */

public class TblElevatorPrincipal implements Serializable {

    private static final long serialVersionUID = -28465663804283519L;

    /**
     * 主键id
     */
    private String vPrincipalId;

    /**
     * 电梯id
     */
    private String vElevatorId;

    /**
     * 电梯负责人id
     */
    private String principalId;

    /**
     * 负责人标签
     */
    private Integer principalLabel;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getvPrincipalId() {
        return vPrincipalId;
    }

    public void setvPrincipalId(String vPrincipalId) {
        this.vPrincipalId = vPrincipalId;
    }

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public Integer getPrincipalLabel() {
        return principalLabel;
    }

    public void setPrincipalLabel(Integer principalLabel) {
        this.principalLabel = principalLabel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
