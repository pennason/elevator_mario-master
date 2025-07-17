package com.shmashine.sender.server.elevator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblProject;
import com.shmashine.sender.entity.SendToUser;
import com.shmashine.sender.entity.TblHaierLiftInfo;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface BizElevatorService {

    List<TblElevator> listOfAll();

    TblElevator getByElevatorCode(String elevatorCode);

    List<TblElevator> getByPtCode(String ptCode);

    /**
     * 根据维保工单更新维保时间
     */
    void updateMaintainDate(String registerNumber, Date completeTime);

    /**
     * 更新年检时间
     */
    void updateInspectDate(String registerNumber, Date lastDate, Date nextDate);

    /**
     * 根据项目id获取电梯注册码
     */
    ArrayList<String> getRegNumberByProjectId(String projectId);

    List<TblElevator> getByPtCodeAndProjectId(String groupId, String projectId);

    //查找更新推送用户缓存
    List<SendToUser> taskReloadSendToUser();

    //根据唯一注册号获取海尔电梯信息
    TblHaierLiftInfo getLiftInfoCache(String equipmentCode);

    List<String> getElevatorRegisterNumerIsNotNull();

    List<TblProject> listProjectByIds(Collection<String> projectIds);


}
