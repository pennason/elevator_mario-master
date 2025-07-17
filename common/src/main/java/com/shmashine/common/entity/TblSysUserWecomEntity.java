// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/15 11:45
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblSysUserWecomEntity implements Serializable {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 企业微信CORP ID
     */
    private String wecomCorpId;
    /**
     * 企业名称
     */
    private String wecomCorpName;
    /**
     * 企业应用ID
     */
    private String wecomAgentId;
    /**
     * 企业微信成员userid
     */
    private String wecomUserId;
    /**
     * 企业微信成员姓名
     */
    private String wecomUserName;

    /**
     * 记录生成时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 记录修改时间
     */
    private LocalDateTime modifyTime;
    /**
     * 修改人
     */
    private String modifyBy;
    /**
     * 是否删除 1:已删除，0:未删除
     */
    private Integer deleted;
}
