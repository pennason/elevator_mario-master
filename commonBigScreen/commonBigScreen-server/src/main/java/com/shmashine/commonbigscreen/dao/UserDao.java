package com.shmashine.commonbigscreen.dao;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.commonbigscreen.entity.User;

/**
 * 用户DO
 *
 * @author: jiangheng
 * @version: 1.0
 * @date: 2022/2/8 18:10
 */
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT '1' FROM tbl_sys_user user "
            + "JOIN tbl_sys_user_role role ON role.v_user_id = user.v_user_id "
            + "JOIN tbl_sys_dept_user dept ON dept.v_user_id = user.v_user_id "
            + "WHERE dept.v_dept_id = '1274176398029885440' "
            + "and role.v_role_id = 'ROLE0000000000000001' "
            + "and user.v_user_id = #{userId}")
    String isAdmin(String userId);
}