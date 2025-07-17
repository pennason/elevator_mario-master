// Copyright (C) 2025 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dataobject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2025/5/21 13:38
 * @since v1.0
 */


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseDO implements Serializable {

    /**
     * 创建时间
     */
    @TableField("dt_create_time")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField("dt_modify_time")
    private Date modifyTime;
    /**
     * 创建记录用户
     */
    @TableField("v_create_user_id")
    private String createUserId;
    /**
     * 修改记录用户
     */
    @TableField("v_modify_user_id")
    private String modifyUserId;
}
