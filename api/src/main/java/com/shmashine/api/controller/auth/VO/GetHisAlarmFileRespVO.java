package com.shmashine.api.controller.auth.VO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 获取历史告警文件响应体
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2025/2/7 14:15
 * @Since: 1.0.0
 */
@Data
@AllArgsConstructor
public class GetHisAlarmFileRespVO {

    /**
     * 0 代表成功，其它代表错误，由result 返回详细信息。
     * 22000002：token 过期。
     * 22100002：图片/视频不存在
     */
    private Integer code;
    /**
     * 返回结果信息 true/false
     */
    private String result;
    /**
     * url、imageUrls 字段不能同时为空，至少1个字段有值
     */
    private VideoPicUrls data;

    /**
     * 视频图片url
     */
    @Data
    public static class VideoPicUrls {
        /**
         * 视频文件URL
         */
        private String url;
        /**
         * 图片文件URL数组
         */
        private List<String> imageUrls;
        /**
         * URL 过期时间 ,Unix 时间戳(秒), 无过期时间为-1
         */
        private Integer expiration;
    }
}
