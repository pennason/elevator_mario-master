// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysUserWecomEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/15 14:47
 * @since v1.0
 */

public interface TblSysUserWecomMapper {

    /**
     * 根据用户ID和企业微信ID获取用户企业微信信息
     *
     * @param userId      用户ID
     * @param wecomCorpId 企业微信CORP_ID
     * @return 用户企业微信信息
     */
    TblSysUserWecomEntity getByUserId(@Param("userId") String userId, @Param("wecomCorpId") String wecomCorpId);

    /**
     * 根据用户ID列表获取用户企业微信信息
     *
     * @param userIds     用户ID列表
     * @param wecomCorpId 企业微信CORP_ID
     * @return 用户企业微信信息列表
     */
    List<TblSysUserWecomEntity> listByUserIds(@Param("userIds") List<String> userIds, @Param("wecomCorpId") String wecomCorpId);

    /**
     * 新增用户企业微信信息
     *
     * @param entity 用户企业微信信息
     * @return 是否成功
     */
    Boolean insert(@Param("entity") TblSysUserWecomEntity entity);

    /**
     * 更新用户企业微信信息
     *
     * @param tblSysUserWecomEntity 用户企业微信信息, 含主键
     */
    void updateById(@Param("entity") TblSysUserWecomEntity tblSysUserWecomEntity);

    /**
     * 按主键删除
     *
     * @param id 主键
     * @return 影响行数
     */
    Integer deleteById(@Param("id") Long id);
}
