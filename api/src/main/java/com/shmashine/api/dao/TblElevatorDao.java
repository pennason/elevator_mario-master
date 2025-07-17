package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.fault.vo.EventStatisticsReqVO;
import com.shmashine.api.entity.KpiProjectElevatorCountEntity;
import com.shmashine.common.entity.TblElevator;

@Mapper
public interface TblElevatorDao {

    TblElevator getById(@NotNull String vElevatorId);

    TblElevator getByElevatorCode(@NotNull String elevatorCode);

    TblElevator getByEquipmentCode(@NotNull String equipmentCode);


    TblElevator getByOneOfChoose(@Param("elevatorId") String elevatorId, @Param("elevatorCode") String elevatorCode,
                                 @Param("equipmentCode") String equipmentCode);

    List<TblElevator> listByEntity(TblElevator tblElevator);

    List<TblElevator> getByEntity(TblElevator tblElevator);

    List<TblElevator> listByIds(@NotEmpty List<String> list);

    List<TblElevator> listByCodes(@NotEmpty List<String> list);

    int insert(@NotNull TblElevator tblElevator);

    int insertIsNotEmpty(@NotNull TblElevator tblElevator);

    int insertBatch(@NotEmpty List<TblElevator> list);

    int update(@NotNull TblElevator tblElevator);

    int updateByField(@NotNull @Param("where") TblElevator where, @NotNull @Param("set") TblElevator set);

    int updateBatch(@NotEmpty List<TblElevator> list);

    int deleteById(@NotNull String vElevatorId);

    int deleteByEntity(@NotNull TblElevator tblElevator);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevator tblElevator);

    List<String> checkExistsByCodes(@NotEmpty List<String> list);


    // 统计KPI相关

    /**
     * 按项目统计电梯安装总数
     *
     * @return 结果
     */
    List<KpiProjectElevatorCountEntity> countInstalledElevatorGroupByProject();

    /**
     * 按项目统计电梯离线总数
     *
     * @return 结果
     */
    List<KpiProjectElevatorCountEntity> countOfflineElevatorGroupByProject();
    /**
     * 按项目统计电梯故障总数， 废弃，需要连接 fault表，这里不能使用
     * @return 结果
     */
    //List<KpiProjectElevatorCountEntity> countFaultElevatorGroupByProject();

    /**
     * 按项目ID获取电梯ID列表
     *
     * @param projectId 项目ID
     * @return 电梯ID列表
     */
    List<String> getElevatorIdsByProjectId(@NotNull String projectId);


    List<String> getElevatorIdsByVillageIds(@NotEmpty List<String> villageIds);

    List<String> getElevatorIdsByProjectIds(@NotEmpty List<String> projectIds);

    /**
     * 根据项目id统计电梯数
     *
     * @param projectId 项目id
     * @return
     */
    Integer countByProjectId(String projectId);

    /**
     * 根据小区id统计电梯数
     *
     * @param villageId 项目id
     * @return
     */
    Integer countByVillageId(String villageId);

    /**
     * 查询用户有权限的电梯的项目id
     *
     * @param userId 用户id
     * @return 项目id列表
     */
    List<String> searchUserElevatorProjects(@Param("userId") String userId, @Param("isAdmin") boolean isAdmin);

    /**
     * 查询用户权限的电梯code列表
     *
     * @param eventStatisticsReqVO 查询条件
     * @return 电梯code列表
     */
    List<String> searchEleCodes(EventStatisticsReqVO eventStatisticsReqVO);

    void clearInFaultingStatus();

}