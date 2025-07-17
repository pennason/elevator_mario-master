// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.system;

import java.util.List;
import java.util.Map;

import com.shmashine.api.controller.user.VO.SearchOutServiceUserListRespVO;
import com.shmashine.api.controller.user.VO.SearchUserListRespVO;
import com.shmashine.common.entity.TblSysUserWecomEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/15 11:31
 * @since v1.0
 */

public interface TblSysUserWecomServiceI {

    /**
     * 根据麦信用户ID获取企业微信用户信息
     *
     * @param userId 麦信用户ID
     * @return 企业微信用户信息
     */
    TblSysUserWecomEntity getUserWecomByUserId(String userId);

    /**
     * 根据麦信用户ID和企业微信用户ID获取企业微信用户信息
     *
     * @param userId      麦信用户ID
     * @param wecomUserId 企业微信用户ID
     * @return 企业微信用户信息
     */
    TblSysUserWecomEntity getUserWecomByUserId(String userId, String wecomUserId);

    /**
     * 根据麦信用户ID列表获取企业微信用户信息列表
     *
     * @param userIds 麦信用户ID列表
     * @return 企业微信用户信息列表
     */
    List<TblSysUserWecomEntity> listByUserIds(List<String> userIds);

    /**
     * 根据麦信用户ID列表和企业微信CORP_ID获取企业微信用户信息列表
     *
     * @param userIds     麦信用户ID列表
     * @param wecomCorpId 企业微信CORP_ID
     * @return 企业微信用户信息列表
     */
    List<TblSysUserWecomEntity> listByUserIds(List<String> userIds, String wecomCorpId);

    /**
     * 保存企业微信用户信息
     *
     * @param tblSysUserWecomEntity 企业微信用户信息
     * @return 企业微信用户信息
     */
    TblSysUserWecomEntity save(TblSysUserWecomEntity tblSysUserWecomEntity);

    /**
     * 用户列表扩展企业微信信息
     *
     * @param list 用户列表
     */
    void extendWecomInfo(List<Map> list);

    /**
     * 用户列表扩展企业微信信息
     *
     * @param list 用户列表
     */
    void extendWecomInfo1(List<SearchUserListRespVO> list);

    void extendWecomInfo2(List<SearchOutServiceUserListRespVO> list);
}
