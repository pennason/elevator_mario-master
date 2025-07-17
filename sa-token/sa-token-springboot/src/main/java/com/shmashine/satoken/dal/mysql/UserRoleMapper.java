package com.shmashine.satoken.dal.mysql;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.satoken.dal.dto.UserRoleDO;
import com.shmashine.satoken.dal.query.BaseMapperX;

/**
 * 默认说明
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/19 14:48
 * @since v1.0.0
 */
@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRoleDO> {
}
