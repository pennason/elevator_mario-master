package com.shmashine.sender.platform.city.shanghai;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Data
@ToString
public class PostResponse implements Serializable {
    int status;
    String message;
    PostResponse.PostResult result;

    /**
     * TokenResult
     */
    @Data
    @ToString
    public static class PostResult implements Serializable {
        String username;
        long expirationTime;
        String token;
    }
}