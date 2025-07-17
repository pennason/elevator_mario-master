// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.client.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 海康 云眸 消费者ID
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/16 18:01
 * @since v1.0
 */

@Data
@ToString
public class HikCloudConsumerIdResponseDTO
        extends HikCloudBaseResponseDTO<HikCloudConsumerIdResponseDTO.ConsumerInfo> {

    @Data
    @ToString
    public static class ConsumerInfo implements Serializable {
        /**
         * 海康 云眸 消费者ID
         */
        private String consumerId;
    }
}
