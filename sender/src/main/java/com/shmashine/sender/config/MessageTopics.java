package com.shmashine.sender.config;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface MessageTopics {

    /**
     * cube上报monitor实时监控信息
     */
    String CUBE_MONITOR = "cube_monitor";

    /**
     * cube上报阶段性累计运行数据
     */
    //String CUBE_TR = "cube_tr";
    String CUBE_TR = "pro_oreo_tr";

    /**
     * cube上报event事件信息
     */
    String CUBE_EVENT = "cube_event";

    /**
     * cube上报fault故障消息
     */
    String CUBE_FAULT = "cube_fault";

    /**
     * cube上报困人消息
     */
    String CUBE_TRAPPED = "cube_trapped";

    /**
     * 北向数据重复发送
     */
    String CUBE_DATA_ACCOUNT = "cube_data_account";
    /**
     * 在线离线
     */
    String CUBE_ONLINE_OFFLINE = "cube_online_offline";

    default void init() {

    }
}
