package com.shmashine.userclientapplets.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.userclientapplets.entity.Village;

/**
 * 小区DO
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/17 15:46
 */
public interface VillageDao extends BaseMapper<Village> {

    /**
     * 获取用户所处部门
     *
     * @param userId 用户id
     * @return 部门
     */
    @Select("select v_dept_id from tbl_sys_dept_user "
            + "where v_user_id = #{userId}")
    String getDeptIdByUserId(String userId);

    /**
     * 根据部门id获取所有子部门
     *
     * @param deptId 部门id
     * @return 子部门列表
     */
    @Select("select v_dept_id from tbl_sys_dept where v_parent_id = #{deptId}")
    List<String> getUserDeptIdByDeptId(String deptId);

    List<String> getProjectByDeptId(Set<String> results);

    List<Village> getVillageByDeptIds(@Param("userId") String userId,
                                      @Param("admin") Boolean admin,
                                      @Param("villageName") String villageName);
}
