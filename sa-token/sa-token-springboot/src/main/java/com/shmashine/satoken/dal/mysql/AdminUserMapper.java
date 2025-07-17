package com.shmashine.satoken.dal.mysql;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.satoken.dal.dto.AdminUserDO;
import com.shmashine.satoken.dal.query.BaseMapperX;

/**
 * 管理员用户表
 *
 * @author jiangheng
 * @version 2024/3/18 18:13
 * @description: com.pj.dal.mysql
 */
@Mapper
public interface AdminUserMapper extends BaseMapperX<AdminUserDO> {

}
