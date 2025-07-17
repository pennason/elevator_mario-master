package com.shmashine.api.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.device.vo.SearchDeviceConfPageReqVO;
import com.shmashine.api.controller.elevator.VO.ElevatorRunDataStatisticsReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingRespVO;
import com.shmashine.api.entity.ElevatorForExcel;
import com.shmashine.api.entity.ElevatorRunCount;
import com.shmashine.api.entity.IotCard;
import com.shmashine.api.entity.KpiProjectElevatorCountEntity;
import com.shmashine.api.entity.TblElevatorConf;
import com.shmashine.api.entity.TblElevatorPrincipal;
import com.shmashine.api.module.elevator.ElevatorDetailDownloadModuleMap;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.module.elevator.ElevatorDetailResModule;
import com.shmashine.api.module.elevator.ElevatorRunDataStatisticsDownload;
import com.shmashine.api.module.elevator.ElevatorScreenModule;
import com.shmashine.api.module.elevator.ElevatorThirdPartyDetail;
import com.shmashine.api.module.elevator.ElevatorThirdPartyList;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.service.elevator.DO.ImageRecognitionMattingConfigDO;
import com.shmashine.api.service.elevator.DO.UserElevatorsDO;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblProject;
import com.shmashine.common.entity.TblSysDept;

public interface BizElevatorDao {

    /**
     * 通过电梯注册码或出厂编号获取电梯信息
     */

    ElevatorDetailResModule getElevatorStatisticsInfo(@Param("leaveFactoryNumber") String leaveFactoryNumber, @Param("equipmentCode") String equipmentCode);

    /**
     * 获取全部在线的电梯编码
     *
     * @return
     */
    List<String> getAllOnlineElevatorCode();

    /**
     * 获取当前用户的部门的id
     *
     * @param vUserId
     * @return
     */
    TblSysDept getDeptByUserId(@Param("vUserId") String vUserId);

    /**
     * 获取当前用户的部门的项目列表
     *
     * @param vDeptId
     * @return
     */
    List<TblProject> getProdectByDeptId(@Param("vDeptId") String vDeptId);

    List<TblSysDept> getDeptidList(@Param("vDeptId") String vDeptId);

    /**
     * 查询电梯列表根据产品id查询
     */
    List<Map> searchElevatorListByProjectId(@Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);


    /**
     * 查询电梯列表
     */
    List<Map> searchElevatorList(@Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);

    /**
     * 导出Excel
     *
     * @param searchElevatorModule
     * @return
     */
    List<ElevatorDetailDownloadModuleMap> searchElevatorListDownload(@Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);

    /**
     * 给别字段详情查询
     *
     * @param elevatorId
     * @return
     */
    ElevatorDetailModule getElevatorByElevatorId(@Param("elevatorId") String elevatorId);

    /**
     * 通过注册编号查询详情
     *
     * @param equipmentCode
     * @return
     */
    ElevatorDetailModule getElevatorByEquipmentCode(String equipmentCode);

    /**
     * 通过注册编号查询详情(推送瑞金大屏——只查瑞金梯数据)
     *
     * @param equipmentCode
     * @return
     */
    ElevatorDetailModule getElevatorByEquipmentCodeS(String equipmentCode);

    /**
     * 全字段详情查询
     *
     * @param elevatorId
     * @return
     */
    ElevatorDetailResModule getElevatorDetail(@Param("elevatorId") String elevatorId);

    /**
     * 对外列表查询
     *
     * @return
     */
    List<ElevatorThirdPartyList> searchElevatorListThirdParty(@Param("searchElevatorModule") SearchElevatorModule searchElevatorModule);

    /**
     * 对外详情查询
     *
     * @param equipmentCode
     * @return
     */
    ElevatorThirdPartyDetail getElevatorByElevatorIdThirdParty(@Param("equipmentCode") String equipmentCode);

    /***
     * 获取电梯每个状态的数量
     * @param userId 用户id
     * @param isAdminFlag 是否是管理员
     * @param projectIds 项目id集合 可以未空
     * @return
     */
    List<Map> countElevator(@Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag,
                            @Param("projectIds") List<String> projectIds,
                            @Param("villageIds") List<String> villageIds);


    /**
     * 根据电梯编号获取梯内小屏展示详情
     *
     * @param elevatorCode 电梯编号
     * @return ElevatorScreenModule
     */
    ElevatorScreenModule getElevatorScreenInfo(@Param("elevatorCode") String elevatorCode);


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
    Date getDeviceEventRecordByFirstRecord(@Param("elevatorCode") String elevatorCode);


    /**
     * 根据第一次上线时间 更新电梯设备安装时间
     *
     * @param elevatorCode 电梯编号
     */
    void updateInstallTime(@Param("elevatorCode") String elevatorCode, @Param("installTime") Date installTime);

    /**
     * 根据下次检验时间
     *
     * @param elevatorCode    电梯编号
     * @param nextInspectDate 下次检验时间
     */
    int updateNextInspectDate(@Param("elevatorCode") String elevatorCode, @Param("nextInspectDate") Date nextInspectDate);

    /**
     * 通过Code 获取电梯信息
     *
     * @return 电梯列表
     */
    TblElevator getElevatorIdByCode(@Param("elevatorCode") String elevatorCode);


    /**
     * 根据电梯id 消除故障状态
     *
     * @param elevatorId 电梯id
     */
    void cancelFaultByElevatorId(@Param("elevatorId") String elevatorId);

    Integer getElevatorTypeByElevatorCode(@Param("elevatorCode") String elevatorCode);

    int update(TblElevator tblElevator);

    List<TblDevice> getElevatorDeviceDetailList(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯安装时间
     *
     * @param elevatorId 电梯id
     */
    void initInstallTime(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator1(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator2(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator3(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator4(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator5(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator6(@Param("elevatorId") String elevatorId);

    /**
     * 初始化电梯关联数据
     *
     * @param elevatorId 电梯id
     */
    void initElevator7(@Param("elevatorId") String elevatorId);

    void initElevator8(@Param("elevatorId") String elevatorId);

    void initElevator9(@Param("elevatorId") String elevatorId);

    void initElevator10(@Param("elevatorId") String elevatorId);

    /**
     * 根据电梯ID删除电梯信息
     *
     * @param elevatorId
     */
    void deleteElevatorInfoByElevatorCode(@Param("elevatorCode") String elevatorId);

    /**
     * 查询电梯表，获取所有的电梯code
     *
     * @return 电梯code
     */
    List<String> searchAllElevatorList();

    /**
     * excel批量导入电梯
     *
     * @param elevatorList
     */
    void addElevatorByExcel(@Param("elevatorList") List<ElevatorForExcel> elevatorList);

    /**
     * 查询可批量升级的设备
     *
     * @return
     */
    List<Map<String, Object>> searchDeviceByHWVersion(@Param("searchElevatorModule") SearchElevatorModule searchElevatorModule,
                                                      @Param("projectIdList") List<String> projectIdList);

    /**
     * 添加电梯责任人
     *
     * @param elevatorPrincipal
     */
    void addElevatorPrincipal(@Param("elevatorPrincipal") TblElevatorPrincipal elevatorPrincipal);

    /**
     * 删除电梯负责人
     *
     * @param elevatorPrincipal
     */
    void delElevatorPrincipal(@Param("elevatorPrincipal") TblElevatorPrincipal elevatorPrincipal);

    HashMap<String, String> getHKCameraByTYInfo(String elevatorId);

    List<Map<String, Object>> getElevatorOnlineRate(@Param("projectId") String projectId);

    /**
     * 查询梯对应的流量卡信息
     *
     * @param iotCard
     * @return
     */
    List<Map> searchElevatorIotCardByUserId(@Param("iotCard") IotCard iotCard);

    /**
     * 批量添加电梯电话卡
     *
     * @param iotCardList
     */
    void save(@Param("iotCardList") List<IotCard> iotCardList);

    /**
     * 更新流量卡信息
     *
     * @param iotCards
     */
    void upDateIotInfo(@Param("iotCards") List<IotCard> iotCards);

    /**
     * 获取物联网卡iccid
     *
     * @return
     */
    List<String> searchAllIotCardIccid();

    /**
     * 获取流量卡预警统计
     *
     * @param projectId
     * @return
     */
    List<IotCard> getIotCardRate(@Param("projectId") String projectId);

    List<TblElevatorConf> searchAllRunCount();

    void addRunCount(@Param("list") List<ElevatorRunCount> list);

    void updateRunCount(@Param("list") List<ElevatorRunCount> list);

    List<ElevatorRunCount> searchElevatorRunCountY();

    List<ElevatorRunCount> searchElevatorRunCount();

    /**
     * 设备在线率
     */
    Map<String, Object> online(@Param("projectId") String projectId);

    /**
     * 设备完好率
     */
    Map<String, Object> device(@Param("projectId") String projectId);

    /**
     * 流量卡正常率,流量不超80%
     */
    Map<String, Object> iotCard(@Param("projectId") String projectId);

    /**
     * 电梯正常率
     */
    Map<String, Object> normal(@Param("projectId") String projectId, @Param("reportDate") Date reportDate);

    /**
     * 电梯日运行数据统计-按日期统计
     *
     * @param faultStatisticsModule 查询条件
     */
    List<Map<Object, Object>> getRunDataStatisticsByDateDimension(FaultStatisticsModule faultStatisticsModule);

    /**
     * 电梯日运行数据统计-按电梯统计
     *
     * @param statisticsModule 查询条件
     */
    List<Map<Object, Object>> getRunDataStatisticsByElevatorDimension(ElevatorRunDataStatisticsReqVO statisticsModule);

    /*删除流量卡*/
    int delIotCardInfoById(String iccid);

    /*手动更新流量卡信息*/
    int iotCardBind(@Param("iotCard") IotCard iotCard);

    /*获取用户电梯*/
    List<Map<String, Object>> searchElevatorByUserId(@Param("isAdminFlag") boolean isAdminFlag, @Param("userId") String userId);

    /**
     * 电梯每日运行数据统计导出
     *
     * @param faultStatisticsModule
     * @return
     */
    List<ElevatorRunDataStatisticsDownload> exportElevatorRunDataStatistics(FaultStatisticsModule faultStatisticsModule);

    /*统计电梯每小时系数表*/
    void updateElevatorConf(@Param("elevatorId") String elevatorId, @Param("elevatorCode") String elevatorCode, @Param("loadFactor") Double loadFactor);

    /**
     * 获取电梯客流量
     *
     * @param elevatorCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<ElevatorRunCount> getElevatorPassengerFlow(@Param("elevatorCode") String elevatorCode,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime);

    /**
     * 按项目统计电梯故障总数
     *
     * @return 结果
     */
    List<KpiProjectElevatorCountEntity> countFaultElevatorGroupByProject();

    /**
     * 按项目统计电梯故障总数, 当天出故障已经恢复的也算
     *
     * @return 结果
     */
    List<KpiProjectElevatorCountEntity> countMaxFaultElevatorGroupByProject();

    /**
     * 获取用户授权电梯列表
     *
     * @param admin
     * @param adminUserId
     * @param userId
     * @return
     */
    List<UserElevatorsDO> searchUserElevators(@Param("admin") boolean admin,
                                              @Param("adminUserId") String adminUserId,
                                              @Param("userId") String userId);

    /**
     * 获取用户授权的电梯对应项目列表
     *
     * @param isAdmin 是否管理员
     * @param userId  用户id
     * @return 项目ID列表
     */
    List<String> getProjectIdsByUserId(@Param("isAdmin") boolean isAdmin, @Param("userId") String userId);

    /**
     * 获取用户对应小区授权的电梯列表
     *
     * @param admin       是否管理员
     * @param adminUserId 登录用户id
     * @param userId      用户id
     * @param villageId   小区id
     * @return 项目ID列表
     */
    List<UserElevatorsDO> searchVillageUserElevators(@Param("admin") boolean admin,
                                                     @Param("adminUserId") String adminUserId,
                                                     @Param("userId") String userId,
                                                     @Param("villageId") String villageId);


    /**
     * 创建配置电梯图像识别抠图区域
     *
     * @param configDO
     */
    int insertImageRecognitionMattingConfig(ImageRecognitionMattingConfigDO configDO);

    /**
     * 获取电梯图像识别抠图区域配置
     *
     * @param elevatorId
     * @param faultTypes
     * @return
     */
    ImageRecognitionMattingConfigDO getImageRecognitionMattingConfigByEleIdAndFault(@Param("elevatorId") String elevatorId,
                                                                                    @Param("faultTypes") String faultTypes);

    /**
     * 更新电梯图像识别抠图区域配置
     *
     * @param configDO
     * @return
     */
    int updateImageRecognitionMattingConfig(ImageRecognitionMattingConfigDO configDO);

    /**
     * 获取配置电梯图像识别抠图区域配置
     *
     * @param reqVO 请求参数
     * @return 配置列表
     */
    List<ImageRecognitionMattingRespVO> getImageRecognitionMattingConfigPage(ImageRecognitionMattingReqVO reqVO);

    TblElevator getElevatorByElevatorIdOrCode(String elevatorIdOrCode);

    /**
     * 删除电梯统计数据
     *
     * @param elevatorCode
     * @Return
     */
    Integer delStatisticsByElevatorCode(String elevatorCode);

    /**
     * 获取该电梯当日运行次数
     *
     * @param elevatorCode
     * @return
     */
    Integer getTodayRunCount(String elevatorCode);

    /**
     * 获取该电梯安装状态
     *
     * @param elevatorId
     * @return
     */
    Integer getElevatorInstallStatusById(String elevatorId);

    /**
     * 获取指定时间内电梯运行次数
     *
     * @param elevatorCode 故障编号
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 运行次数
     */
    Long getRunCountByElevatorCodeAndTime(@Param("elevatorCode") String elevatorCode,
                                          @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据电梯编号获取电梯信息
     *
     * @param elevatorCode 电梯编号
     * @return 电梯信息
     */
    TblElevator getByCode(String elevatorCode);

    /**
     * 更新电梯最低最高楼层
     *
     * @param elevatorCode 电梯编号
     * @param minFloor     最低楼层
     * @param maxFloor     最高楼层
     */
    void updateFloorByElevatorCode(@Param("elevatorCode") String elevatorCode,
                                   @Param("minFloor") Integer minFloor,
                                   @Param("maxFloor") Integer maxFloor);

    /**
     * 更新电梯表设备配置状态
     *
     * @param elevatorCode     电梯编号
     * @param deviceConfStatus 设备配置状态0：未配置 1：已下发 2：已配置
     */
    void updateDeviceConfStatusByCode(@Param("elevatorCode") String elevatorCode,
                                      @Param("status") Integer deviceConfStatus);

    /**
     * 查询电梯配置列表包含配置状态
     *
     * @param searchElevatorModule 查询参数
     * @return 列表
     */
    List<Map> searchElevatorListWithConfStatus(@Param("searchElevatorModule") SearchDeviceConfPageReqVO searchElevatorModule);

    /**
     * 获取已安装且在线的电梯
     *
     * @param userId      用户id
     * @param isAdminFlag 是否管理员
     * @return 电梯id列表
     */
    List<String> getFaultElevatorList(String userId, boolean isAdminFlag);

}
