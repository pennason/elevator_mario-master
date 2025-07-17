package com.shmashine.hkcamerabyys.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 萤石云查询任务状态返回实体
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/1/23 11:05
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskStatusRespVO {

    /**
     * 任务 id
     */
    private String taskId;

    /**
     * 项目 id
     */
    private String projectId;

    /**
     * 设备序列号
     */
    private String deviceSerial;

    /**
     * 通道号
     */
    private Integer channelNo;

    /**
     * 任务类型 1-视频抽帧 2-预览录制 3-回放录制 4-即时录制 5-全景图片采集 6-抓拍
     */
    private Integer taskType;

    /**
     * 任务子类型 10–按时间间隔抽帧 11–按时间点列表抽帧 60-按时间间隔抓拍 61-按时间点列表抓拍
     */
    private Integer taskSubType;

    /**
     * 任务状态 COMPLETE：0-已完成；WAITING：1-排队中；PROCESSING：2-进行中；
     * FINISHED：3-已结束；EXCEPTION_FAILED：4-异常结束；
     * EXCEPTION_PAUSE：5-暂停中；CANCEL：6-已取消； NOT_START：7-未开始
     */
    private Integer taskStatus;

    /**
     * 任务详情
     */
    private TaskDetail taskDetail;

    /**
     * 文件个数
     */
    private Integer fileNum;

    /**
     * 任务文件总大小
     */
    private Integer totalSize;

    /**
     * 任务开始时间
     */
    private String startTime;

    /**
     * 任务结束时间
     */
    private String endTime;

    /**
     * 时间点列表
     */
    private String timingPoints;

    /**
     * 任务进度，仅当录制任务有效
     */
    private Integer progressRate;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 任务创建时间
     */
    private String createTime;

    /**
     * 任务详情
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class TaskDetail {

        /**
         * 录像类型 local-本地录像 cloud-云存储录像 live-实时 实时录像生效时间
         */
        private String recType;

        /**
         * 设备协议
         */
        private String devProto;

        /**
         * 抽帧间隔 普通模式和抽I帧模式模式，单位：秒；如果选择抽 I 帧模式，此处传 GOP 间隔倍数，单位：倍数
         */
        private Integer frameInterval;

        /**
         * 抽帧模式 抽帧模式 0：普通模式 1：错峰抽帧模式* 2：抽 I 帧模式*；不填默认:0
         */
        private Integer frameModel;

        /**
         * 码流类型 码流类型，实时抽帧可以选择，默认为 1 1：高清（主码流） 2：标清（子码流） 非实时帧建议选择，1：高清（主码流）
         */
        private String streamType;

        /**
         * 录制视频声音开关，0.关 1.开 2.自动 默认 2，如果音频不是AAC，则自动关闭视频声音
         */
        private Integer voiceSwitch;

        /**
         * 是否需要录制Ai框，0关 1开，默认关闭
         */
        private Integer aiBox;

        /**
         * 视频封装类型，默认format=mp4，即以MP4格式进行录制；当format=ps 时，会直接录制ps流
         */
        private String format;

    }
}
