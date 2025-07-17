package com.shmashine.api.service.fault;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.fault.vo.ElevatorStopStatisticsVO;
import com.shmashine.api.controller.fault.vo.EventStatisticsReqVO;
import com.shmashine.api.controller.fault.vo.SearchFaultsReqVO;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.input.SearchSensorFaultModule;
import com.shmashine.api.module.fault.output.EventElevatorDownloadModuleMap;
import com.shmashine.api.module.fault.output.FaultDetailDownloadModuleMap;
import com.shmashine.api.module.fault.output.UncivilizedBehaviorDownloadModuleMap;
import com.shmashine.common.entity.TblFault;

/**
 * 故障接口
 */
public interface BizFaultService {

    /**
     * 获取故障列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchFaultSList(SearchFaultsReqVO searchFaultModule);


    List<Map> searchRealTimeFault(SearchFaultsReqVO searchFaultModule);

    /**
     * 获取故障详情
     *
     * @param faultId
     * @return
     */
    Map<String, Object> getFaultSDetail(String faultId);

    /**
     * 故障统计
     *
     * @param faultStatisticsModule
     * @return
     */
    Map<String, Object> getFaultStatistics(FaultStatisticsModule faultStatisticsModule);

    /**
     * 电梯上下线统计
     *
     * @param eventStatisticsReqVO 查询条件
     * @return 统计结果
     */
    ResponseResult getElevatorEventStatistics(EventStatisticsReqVO eventStatisticsReqVO);

    /**
     * 取消故障
     *
     * @param faultId
     */
    void cancelFault(String faultId);

    /**
     * 昨日故障统计
     *
     * @param isAdmin    是否是管理员
     * @param userId     用户id
     * @param projectIds 项目ids
     * @param villageIds 小区IDs
     * @return
     */
    HashMap<String, Object> getYesterdayFaultStatistics(boolean isAdmin, String userId, List<String> projectIds, List<String> villageIds);

    /**
     * 单梯历史故障占比统计
     *
     * @param isAdmin
     * @param userId
     * @return
     */
    List<Object> getHisFaultStatistics(boolean isAdmin, String userId, String elevatorId);

    /**
     * 单梯不文明历史统计
     *
     * @param isAdmin
     * @param userId
     * @param elevatorId
     * @return
     */
    List<Object> getHospitalizationIonFaultStatistics(boolean isAdmin, String userId, String elevatorId);

    List<FaultDetailDownloadModuleMap> searchFaultListDownload(SearchFaultsReqVO searchFaultModule);

    List<UncivilizedBehaviorDownloadModuleMap> searchUncivilizedBehaviorListDownload(SearchFaultsReqVO searchFaultModule);

    /**
     * 获取fault
     *
     * @param faultId
     * @return
     */
    TblFault getById(String faultId);

    /**
     * 更新fault
     *
     * @param fault
     */
    void update(TblFault fault);

    /**
     * 电动车识别再次识别
     */
    void taskReloadFaultConfirm();

    /**
     * 电动车二次识别，取消故障恢复
     */
    void taskReloadFaultDisappear();

    /**
     * 导出上下线记录
     *
     * @param
     * @return
     */
    List<EventElevatorDownloadModuleMap> searchEventElevatorDownload(@Param("module") FaultStatisticsModule module);

    /**
     * 获取传感器故障列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchSensorFaultSList(SearchSensorFaultModule searchFaultModule);

    /**
     * 获取传感器故障详情
     *
     * @param faultId 故障id
     * @return
     */
    Map<String, Object> getSensorFaultSDetail(String faultId);

    /**
     * 取消传感器故障
     *
     * @param faultId 故障id
     */
    void cancelSensorFault(String faultId);

    /**
     * 获取传感器故障统计
     *
     * @return
     */
    Map<String, Object> getSensorFaultRate(String projectId);

    /**
     * 获取周困人统计
     */
    Map<String, Object> tiringStatistics(String userId, boolean isAdmin, String projectId);

    /**
     * 周检修次数统计
     */
    Map<String, Object> maintenanceStatistics(String userId, boolean isAdmin, String projectId);

    /**
     * 周故障次数统计
     */
    Map<String, Object> faultStatistics(String userId, boolean isAdmin, String projectId);

    /**
     * 周电动车次数统计
     */
    Map<String, Object> bicycleStatistics(String userId, boolean isAdmin, String projectId);

    /**
     * 周反复阻挡门次数统计
     */
    Map<String, Object> stopTheDoorStatistics(String userId, boolean isAdmin, String projectId);

    Map<String, Object> runStatistics(String userId, boolean isAdmin, String projectId);

    PageListResultEntity searchTimeoutFaultList(SearchFaultModule searchFaultModule);

    /*恢复离线一小时告警*/
    void cancelTimeOutFault(String id);

    ResponseResult getFaultMediaFile(String faultId);

    /**
     * 根据电梯code删除该梯所有故障
     *
     * @param elevatorCode
     * @return
     */
    ResponseResult delFaultByElevatorCode(String elevatorCode);

    /**
     * 根据故障id删除故障
     *
     * @param faultId
     * @return
     */
    ResponseResult delFaultById(String faultId);

    /**
     * 每日凌晨清理故障中故障
     */
    void clearInFaultingFault();

    /**
     * 故障统计接口 或 不文明行为统计 导出
     *
     * @param faultStatisticsModule
     * @param response
     */
    void exportFaultStatistics(FaultStatisticsModule faultStatisticsModule, HttpServletResponse response);

    /**
     * 故障统计接口 或 不文明行为统计 导出
     *
     * @param searchSensorFaultModule
     * @param response
     */
    void exportSensorFaultList(SearchSensorFaultModule searchSensorFaultModule, HttpServletResponse response);

    /**
     * 获取
     * 停梯率=故障（包含困人）停梯时间/运行时间
     * 困人率=困人次数/运行次数
     * 故障率=故障次数/运行次数
     *
     * @param elevatorCode 电梯编号
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 统计结果
     */
    ElevatorStopStatisticsVO getElevatorStopStatistics(String elevatorCode, Date startTime, Date endTime);

    /**
     * 获取电梯被屏蔽的故障列表
     *
     * @param elevatorCode 电梯编号
     * @return 屏蔽的故障列表
     */
    ResponseResult searchShieldedFaultList(String elevatorCode);
}
