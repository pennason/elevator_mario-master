package com.shmashine.sender.server.dataAccount;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface DataAccountService {

    void addData(String accountCode, String key, String jsonData, long expireTime);

    Object getData(String key);

    void bizSetData(String number, String jsonData);

    void bizPushData();

    /**
     * 统计数据推送临港平台
     */
    void taskPushStatistics2linGang();

}
