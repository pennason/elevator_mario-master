package com.shmashine.userclientapplets.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.userclientapplets.entity.WeChatUser;

/**
 * WeChatLoginDao
 */
public interface WeChatLoginDao extends BaseMapper<WeChatUser> {

    /**
     * 查询用户是否注册
     *
     * @param userId 用户id
     */
    @Select("select count(1) from tbl_sys_user where v_user_id = #{userId}")
    int getUserById(String userId);

    /**
     * 添加用户
     *
     * @param weChatUser 用户
     */
    @Insert("insert into tbl_sys_user "
            + "(v_user_id,v_name,v_password,open_id,v_mobile,i_status,i_work_order_flag,"
            + "i_send_phone_status,i_send_message_status,dt_createTime,dt_modifyTime) values "
            + "(#{userId},#{userName},#{passWord},#{openId},#{phoneNumber},#{isDeleted},"
            + "1,1,1,#{createTime},#{modifyTime})")
    int insertUser(WeChatUser weChatUser);


    int addUserResource(@Param("userId") String userId, @Param("elevatorIds") List<String> elevatorIds,
                        @Param("requestUserId") String requestUserId);

    @Select("delete FROM tbl_sys_user_resource WHERE v_user_id = #{userId}")
    List<String> removeElevatorByuserId(String userId);

    /**
     * 删除用户
     *
     * @param userId 用户
     */
    @Delete("delete from tbl_sys_user where v_user_id = #{userId}")
    void removeUserById(String userId);

    /**
     * 系统部门用户
     *
     * @param weChatUser 用户
     */
    @Insert("insert into tbl_sys_dept_user"
            + " (v_dept_id, v_user_id, dt_createTime, dt_modifyTime, v_createid, v_modifyid)"
            + " values (#{deptId}, #{userId}, #{createTime}, #{modifyTime}, 'system', 'system')")
    int insertDeptUser(WeChatUser weChatUser);

    /**
     * 系统角色用户
     *
     * @param weChatUser 用户
     */
    @Insert("insert into tbl_sys_user_role"
            + " (v_user_id, v_role_id, dt_createTime, dt_modifyTime, v_createid, v_modifyid)"
            + " values (#{userId}, #{roleId}, #{createTime}, #{modifyTime}, 'system', 'system')")
    int insertUserRole(WeChatUser weChatUser);

    /**
     * 查看是否分配部门
     *
     * @param weChatUser 用户
     */
    @Select("select count(1) from tbl_sys_dept_user where v_user_id = #{userId}")
    int getDeptByUser(WeChatUser weChatUser);

    /**
     * 查看是否分配角色
     *
     * @param weChatUser 用户
     */
    @Select("select count(1) from tbl_sys_user_role where v_user_id = #{userId}")
    int getRoleByUser(WeChatUser weChatUser);

    /**
     * 更新分配部门
     *
     * @param weChatUser 用户
     */
    @Update("update tbl_sys_dept_user set v_dept_id = #{deptId} where v_user_id = #{userId}")
    int updateDeptInfo(WeChatUser weChatUser);

    /**
     * 更新分配角色
     *
     * @param weChatUser 用户
     */
    @Update("update tbl_sys_user_role set v_role_id = #{roleId} where v_user_id = #{userId}")
    int updateRoleInfo(WeChatUser weChatUser);
}
