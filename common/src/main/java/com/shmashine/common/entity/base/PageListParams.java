package com.shmashine.common.entity.base;

import java.io.Serializable;

import lombok.Data;

/**
 * 参数中的分页参数 权限参数，用户参数
 *
 * @author chenx
 */

@Data
public class PageListParams implements Serializable {
    public Integer pageSize = 10;
    public Integer pageIndex = 1;
    public boolean isAdminFlag;
    public String userId;

    public boolean isAdminFlag() {
        return isAdminFlag;
    }

    public void setAdminFlag(boolean adminFlag) {
        isAdminFlag = adminFlag;
    }
}
