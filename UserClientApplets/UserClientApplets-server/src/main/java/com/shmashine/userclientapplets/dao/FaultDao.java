package com.shmashine.userclientapplets.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.userclientapplets.entity.Fault;
import com.shmashine.userclientapplets.entity.SearchFaultModule;

/**
 * 故障表
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/10 11:00
 */
public interface FaultDao extends BaseMapper<Fault> {

    /**
     * 获取分页故障列表
     *
     * @param searchFaultModule 查询参数
     * @return 故障列表
     */
    List<Fault> queryFaultList(@Param("searchFaultModule") SearchFaultModule searchFaultModule);

    /**
     * 获取困人列表
     *
     * @param searchFaultModule 查询参数
     * @return 困人列表
     */
    List<Fault> queryTrappedPeopleByPage(@Param("searchFaultModule") SearchFaultModule searchFaultModule);

    /**
     * 获取故障总条数
     *
     * @param searchFaultModule 查询参数
     * @return 故障条数
     */
    Integer getFaultTotal(SearchFaultModule searchFaultModule);

    /**
     * 获取困人总条数
     *
     * @param searchFaultModule 查询参数
     * @return 困人条数
     */
    Integer getTrappedPeopleTotal(SearchFaultModule searchFaultModule);

    /**
     * 首页提示——获取当日故障列表
     *
     * @param userId 用户id
     * @param admin  是否管理员
     * @return 故障列表
     */
    List<String> getFaultByDay(@Param("userId") String userId, @Param("admin") boolean admin);

    /**
     * 救援时长统计麦信平台故障时长
     *
     * @param searchFaultModule 查询参数
     * @return 故障时长
     */
    Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule);

    /**
     * 获取故障列表
     *
     * @param elevators       电梯ID列表
     * @param selectStartTime 开始时间
     * @param selectEndTime   结束时间
     * @param faultType       故障类型
     * @return 故障列表
     */
    List<Fault> getFaultList(@Param("elevators") List<String> elevators,
                             @Param("selectStartTime") String selectStartTime,
                             @Param("selectEndTime") String selectEndTime,
                             @Param("faultType") String faultType);

}
