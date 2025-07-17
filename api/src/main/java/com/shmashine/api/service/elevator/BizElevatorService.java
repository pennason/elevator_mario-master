package com.shmashine.api.service.elevator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import com.shmashine.api.controller.elevator.VO.ElevatorRunDataStatisticsReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingConfigReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingConfigRespVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingRespVO;
import com.shmashine.api.controller.elevator.VO.InsertImageRecognitionMattingConfigListReqVO;
import com.shmashine.api.controller.elevator.VO.UserElevatorsRespVO;
import com.shmashine.api.entity.ElevatorForExcel;
import com.shmashine.api.entity.ElevatorRunCount;
import com.shmashine.api.entity.IotCard;
import com.shmashine.api.entity.TblElevatorPrincipal;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.ElevatorDetailDownloadModuleMap;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.module.elevator.ElevatorDetailResModule;
import com.shmashine.api.module.elevator.ElevatorScreenModule;
import com.shmashine.api.module.elevator.ElevatorThirdPartyDetail;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.service.elevator.DO.UserElevatorsDO;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;

/**
 * elevator service
 */

public interface BizElevatorService {

    /**
     * 通过电梯注册码或出厂编号获取电梯信息
     */

    Map getElevatorStatisticsInfo(String leaveFactoryNumber, String equipmentCode);

    /**
     * 获取全部在线的电梯编码
     *
     * @return
     */
    List<String> getAllOnlineElevatorCode();

    /**
     * 根据电梯ID删除电梯信息
     *
     * @param elevatorId
     */
    void deleteElevatorInfoByElevatorCode(@Param("elevatorCode") String elevatorId);


    /**
     * 获取电梯列表
     */
    PageListResultEntity searchElevatorList(SearchElevatorModule searchElevatorModule);

    /**
     * 获取电梯列表(根据UserId筛选)
     */
    PageListResultEntity searchElevatorListByDeptId(SearchElevatorModule searchElevatorModule);

    /**
     * 获取电梯列表不分页
     */
    List<Map> searchElevatorListNoPage(SearchElevatorModule searchElevatorModule);

    /**
     * 导出Excel
     *
     * @param searchElevatorModule
     * @return
     */
    List<ElevatorDetailDownloadModuleMap> searchElevatorListDownload(SearchElevatorModule searchElevatorModule);

    /**
     * 个别获取电梯详情
     */
    ElevatorDetailModule getElevatorByElevatorId(String elevatorId);

    /**
     * 注册码获取电梯详情
     */
    ElevatorDetailModule getElevatorByEquipmentCode(String equipmentCode);

    ElevatorDetailModule getElevatorByEquipmentCodeS(String equipmentCode);

    /**
     * 获取电梯数量
     */
    Map countElevator(String userId, boolean isAdminFlag, List<String> projectIds, List<String> villageIds);

    /**
     * 全字段电梯详情
     *
     * @param elevatorId
     * @return
     */
    ElevatorDetailResModule getElevatorDetail(String elevatorId);


    /**
     * 根据电梯编号获取梯内小屏展示详情
     *
     * @param elevatorCode 电梯编号
     * @return ElevatorScreenModule
     */
    ElevatorScreenModule getElevatorScreenInfo(String elevatorCode);


    /**
     * 获取所有安装时间为空的电梯编号
     *
     * @return 电梯编号list
     */
    List<String> getAllElevatorCodeByInstallTimeIsNull();


    /**
     * 根据电梯编号，获取第一次上线时间
     *
     * @param elevatorCode 电梯编号
     */
    Date getDeviceEventRecordByFirstRecord(String elevatorCode);


    /**
     * 根据第一次上线时间 更新电梯设备安装时间
     *
     * @param elevatorCode 电梯编号
     */
    void updateInstallTime(String elevatorCode, Date installTime);


    int updateNextInspectDate(String elevatorCode, Date nextInspectDate);

    /**
     * 全字段电梯详情
     *
     * @param searchElevatorModule
     * @return
     */
    PageListResultEntity searchElevatorThirdPartyList(SearchElevatorModule searchElevatorModule);

    /**
     * 全字段电梯详情
     *
     * @param equipmentCode
     * @return
     */
    ElevatorThirdPartyDetail getElevatorThirdPartyDetail(String equipmentCode);


    List<String> getElevatorDeviceList(String elevatorId);


    /**
     * 根据电梯id 消除故障状态
     *
     * @param elevatorId 电梯id
     */
    void cancelFaultByElevatorId(String elevatorId);

    Integer getElevatorTypeByElevatorCode(String elevatorCode);

    Integer update(TblElevator tblElevator);

    List<TblDevice> getElevatorDeviceDetailList(String elevatorId);

    /**
     * 初始化电梯安装时间
     *
     * @param elevatorId 电梯id
     */
    void initInstallTime(String elevatorId);

    /**
     * 初始化电梯所有关联数据
     *
     * @param elevatorId
     */
    void initElevator(String elevatorId);

    /**
     * 查询所有的电梯code
     *
     * @return 电梯code
     */
    List<String> searchAllElevatorList();

    void addElevatorByExcel(List<ElevatorForExcel> elevatorList);

    /**
     * 查询可升级的设备
     *
     * @return
     */
    PageListResultEntity searchDeviceByHWVersion(SearchElevatorModule searchElevatorModule);

    /**
     * 添加电梯负责人
     *
     * @param elevatorPrincipals
     */
    void addElevatorPrincipal(List<TblElevatorPrincipal> elevatorPrincipals);

    /**
     * 获取在线电梯率
     *
     * @return
     */
    List<Map<String, Object>> getElevatorOnlineRate(String projectId);

    /**
     * 查询所有电梯对应的卡状态信息
     *
     * @param iotCard
     * @return
     */
    PageListResultEntity searchElevatorIotCardByUserId(IotCard iotCard);

    /**
     * 批量添加电梯流量卡
     *
     * @param iotCardList
     */
    void save(List<IotCard> iotCardList);

    /**
     * 更新流量卡信息
     *
     * @param iccids
     * @return
     */
    ResponseEntity updateIotCardInfo(List<String> iccids);

    /**
     * 获取需要同步的流量卡信息
     *
     * @return
     */
    List<String> searchAllIotCardIccid();

    /**
     * 手动更新流量卡信息
     *
     * @param iotCard
     * @return
     */
    int updateIotCard(IotCard iotCard);

    /**
     * 流量卡预警统计
     *
     * @param projectId 项目id
     * @return
     */
    Map<String, Object> getIotCardRate(String projectId);

    /**
     * 统计运行次数
     */
    void searchAllRunCount();

    Map<String, List<Map>> radarChart(String projectId);

    /**
     * 获取电梯每日运行数据统计-根据时间维度统计
     *
     * @param statisticsModule 查询条件
     */
    Map<String, Object> getRunDataStatisticsByDateDimension(ElevatorRunDataStatisticsReqVO statisticsModule);

    /**
     * 获取电梯每日运行数据统计-根据根据电梯维度统计
     *
     * @param statisticsModule 查询条件
     */
    Map<String, Object> getRunDataStatisticsByElevatorDimension(ElevatorRunDataStatisticsReqVO statisticsModule);

    /**
     * 手动更新流量卡信息
     *
     * @param iccid
     * @return
     */
    ResponseEntity updateIotCardInfoById(String iccid);

    /**
     * 删除流量卡
     *
     * @param iccid
     * @return
     */
    int delIotCardInfoById(String iccid);

    void updateIotCardPackage(String iccid);

    /**
     * 电梯每日运行数据统计-导出
     *
     * @param faultStatisticsModule
     * @return
     */
    void exportElevatorRunDataStatistics(FaultStatisticsModule faultStatisticsModule, HttpServletResponse response);

    /**
     * 获取电梯客流量
     *
     * @param elevatorCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<ElevatorRunCount> getElevatorPassengerFlow(String elevatorCode,
                                                    String startTime,
                                                    String endTime);

    /**
     * 获取用户授权电梯
     *
     * @param admin
     * @param userId
     * @return
     */
    List<UserElevatorsRespVO> searchUserElevators(boolean admin, String adminUserid, String userId);

    /**
     * 获取用户授权电梯对应的项目ID列表
     *
     * @param isAdmin 是否是管理员
     * @param userId  用户ID
     * @return
     */
    List<String> getProjectIdsByUserId(boolean isAdmin, String userId);

    /**
     * 获取小区下用户电梯授权情况
     *
     * @param admin       是否为管理员
     * @param adminUserId 登录用户id
     * @param userId      被授权用户id
     * @param villageId   小区id
     * @return
     */
    List<UserElevatorsDO> searchVillageUserElevators(boolean admin, String adminUserId, String userId, String villageId);

    /**
     * 配置电梯图像识别抠图区域
     *
     * @param imageRecognitionMattingConfig
     * @return
     */
    Boolean insertImageRecognitionMattingConfig(ImageRecognitionMattingConfigReqVO imageRecognitionMattingConfig, String userId);

    /**
     * 获取电梯图像识别抠图区域
     *
     * @param elevatorId
     * @return
     */
    ImageRecognitionMattingConfigRespVO getImageRecognitionMattingConfig(String elevatorId, String faultTypes);

    /**
     * 新增待标记记录
     *
     * @param reqVO  电梯列表
     * @param userId
     * @return
     */
    Boolean insertImageRecognitionMattingConfigList(InsertImageRecognitionMattingConfigListReqVO reqVO, String userId);

    /**
     * 分页获取配置电梯图像识别抠图区域配置
     *
     * @param reqVO 请求参数
     * @return 配置列表
     */
    PageListResultEntity<ImageRecognitionMattingRespVO>
    getImageRecognitionMattingConfigPage(ImageRecognitionMattingReqVO reqVO);

    /**
     * 删除电梯统计数据和故障数据
     *
     * @param elevatorCode
     * @return
     */
    ResponseResult delFaultAndStatisticsByElevatorCode(String elevatorCode);

    /**
     * 获取指定时间段内电梯运行次数
     *
     * @param elevatorCode 电梯编号
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 运行次数
     */
    int getRunNumByElevatorCodeAndTime(String elevatorCode, Date startTime, Date endTime);

    /**
     * 根据电梯编号获取电梯信息
     *
     * @param elevatorCode 电梯编号
     * @return 电梯
     */
    TblElevator getElevatorByCode(String elevatorCode);


}
