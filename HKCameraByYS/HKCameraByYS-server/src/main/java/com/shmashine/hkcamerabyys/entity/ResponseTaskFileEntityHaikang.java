// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 根据任务ID 获取返回结果
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 14:10
 * @since v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResponseTaskFileEntityHaikang extends
        ResponseEntityHaikang<List<ResponseTaskFileEntityHaikang.TaskFileInfo>> {

    /**
     * 文件信息
     */
    @Data
    public static class TaskFileInfo {
        /**
         * 文件所属项目ID
         */
        private String projectId;
        /**
         * 文件ID
         */
        private String fileId;
        /**
         * 文件类型0-.jpg 1-.mp4
         */
        private Integer fileType;
        /**
         * 文件状态. 0-正常, z 1-正在上传, 2-上传失败, 3-已过期, 4-已删除
         */
        private Integer status;
        /**
         * 文件实际个数, 视频文件可能过大被拆分成多个小文件
         */
        private Integer fileCount;
        /**
         * 文件大小, 单位: 字节
         */
        private Long fileSize;
        /**
         * 转码时长，单位：秒
         */
        private Integer duration;
        /**
         * 文件上传错误码
         */
        private String errorCode;
        /**
         * 文件过期时间
         */
        private String expireTime;
        /**
         * 项目存储类型 1-标准存储 2-存档存储
         */
        private Integer storageType;
        /**
         * 文件存储类型最后一次转换时间
         */
        private String lastTransferTime;
        /**
         * 文件解冻有效期
         */
        private String unfreezeTime;
        /**
         * 文件记录创建时间
         */
        private String createTime;
        private String updateTime;
        /**
         * 任务编号
         */
        private String taskId;
        /**
         * 下载地址
         */
        private List<String> downloadUrls;
        /**
         * 文件是否为回放录制产生的文件，false-否，true-是
         */
        private boolean replayRecord;


    }
}
