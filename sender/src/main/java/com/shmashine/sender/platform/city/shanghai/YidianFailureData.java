package com.shmashine.sender.platform.city.shanghai;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Data
public class YidianFailureData implements Serializable {

    private static final long serialVersionUID = -483331393819263437L;

    /**
     * 供应商代码+'-'+供应商报警id，保证唯一， 例如 shmx--0000001, wxcl-a000111
     */
    private String alarmId;

    /**
     * 事件来源: 110电话：S01，物业：S02,维保：S03，物联网：S04，小程序：S05
     */
    private String alarmChannel;

    /**
     * 电梯统一注册码（对标麦信：vEquipmentCode）
     */
    private String registerNumber;

    /**
     * 故障上报时间
     */
    private String occurTime;

    private String failureCode;

    private String eventDesc;

    private String statusCollectTime;

    private List<Map<String, String>> statusDetails;

    public List<Map<String, String>> getStatusDetails() {
        return statusDetails;
    }

}
