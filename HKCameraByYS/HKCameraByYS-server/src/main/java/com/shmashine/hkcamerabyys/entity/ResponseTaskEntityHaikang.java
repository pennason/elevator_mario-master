// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 发起请求，返回任务ID
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 14:10
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResponseTaskEntityHaikang extends ResponseEntityHaikang<ResponseTaskEntityHaikang.TaskInfo> {

    /**
     * 任务信息
     */
    @Data
    public static class TaskInfo {
        private String taskId;
    }
}
