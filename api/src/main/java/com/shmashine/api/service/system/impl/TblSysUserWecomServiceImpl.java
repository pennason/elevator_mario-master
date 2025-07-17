// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.system.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shmashine.api.controller.user.VO.SearchOutServiceUserListRespVO;
import com.shmashine.api.controller.user.VO.SearchUserListRespVO;
import com.shmashine.api.dao.TblSysUserWecomMapper;
import com.shmashine.api.service.system.TblSysUserWecomServiceI;
import com.shmashine.common.entity.TblSysUserWecomEntity;
import com.shmashine.wecom.components.properties.WeComBaseProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/15 11:32
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TblSysUserWecomServiceImpl implements TblSysUserWecomServiceI {
    private final TblSysUserWecomMapper tblSysUserWecomMapper;


    @Override
    public TblSysUserWecomEntity getUserWecomByUserId(String userId) {
        return tblSysUserWecomMapper.getByUserId(userId, WeComBaseProperties.CORP_ID);
    }

    @Override
    public TblSysUserWecomEntity getUserWecomByUserId(String userId, String wecomUserId) {
        return tblSysUserWecomMapper.getByUserId(userId, wecomUserId);
    }

    @Override
    public List<TblSysUserWecomEntity> listByUserIds(List<String> userIds) {
        return tblSysUserWecomMapper.listByUserIds(userIds, WeComBaseProperties.CORP_ID);
    }

    @Override
    public List<TblSysUserWecomEntity> listByUserIds(List<String> userIds, String wecomCorpId) {
        return tblSysUserWecomMapper.listByUserIds(userIds, wecomCorpId);
    }

    @Override
    public TblSysUserWecomEntity save(TblSysUserWecomEntity tblSysUserWecomEntity) {
        if (tblSysUserWecomEntity == null) {
            return null;
        }
        if (tblSysUserWecomEntity.getWecomCorpId() == null || tblSysUserWecomEntity.getWecomCorpId().isEmpty()) {
            tblSysUserWecomEntity.setWecomCorpId(WeComBaseProperties.CORP_ID);
            tblSysUserWecomEntity.setWecomCorpName(WeComBaseProperties.CORP_NAME);
        }
        if (hasUniKey(tblSysUserWecomEntity)) {
            tblSysUserWecomMapper.updateById(tblSysUserWecomEntity);
        } else {
            tblSysUserWecomMapper.insert(tblSysUserWecomEntity);
        }
        return tblSysUserWecomEntity;
    }

    @Override
    public void extendWecomInfo(List<Map> list) {
        var userIds = list.stream()
                .map(x -> x.get("vUserId").toString())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        // 获取企业微信信息
        var wecomMap = listByUserIds(userIds)
                .stream()
                .collect(Collectors.toMap(TblSysUserWecomEntity::getUserId, Function.identity()));
        if (CollectionUtils.isEmpty(wecomMap)) {
            return;
        }
        // 扩展企业微信信息
        list.forEach(item -> {
            var wecom = wecomMap.get(item.get("vUserId").toString());
            if (wecom != null) {
                item.put("wecomUserId", wecom.getWecomUserId());
                item.put("wecomUserName", wecom.getWecomUserName());
            }
        });
    }

    @Override
    public void extendWecomInfo1(List<SearchUserListRespVO> list) {

        var userIds = list.stream()
                .map(x -> x.getVUserId())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        // 获取企业微信信息
        var wecomMap = listByUserIds(userIds)
                .stream()
                .collect(Collectors.toMap(TblSysUserWecomEntity::getUserId, Function.identity()));
        if (CollectionUtils.isEmpty(wecomMap)) {
            return;
        }
        // 扩展企业微信信息
        list.forEach(item -> {
            var wecom = wecomMap.get(item.getVUserId());
            if (wecom != null) {
                item.setWecomUserId(wecom.getWecomUserId());
                item.setWecomUserName(wecom.getWecomUserName());
            }
        });

    }

    @Override
    public void extendWecomInfo2(List<SearchOutServiceUserListRespVO> list) {
        var userIds = list.stream()
                .map(x -> x.getVUserId())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        // 获取企业微信信息
        var wecomMap = listByUserIds(userIds)
                .stream()
                .collect(Collectors.toMap(TblSysUserWecomEntity::getUserId, Function.identity()));
        if (CollectionUtils.isEmpty(wecomMap)) {
            return;
        }
        // 扩展企业微信信息
        list.forEach(item -> {
            var wecom = wecomMap.get(item.getVUserId());
            if (wecom != null) {
                item.setWecomUserId(wecom.getWecomUserId());
                item.setWecomUserName(wecom.getWecomUserName());
            }
        });
    }

    private Boolean hasUniKey(TblSysUserWecomEntity tblSysUserWecomEntity) {
        return tblSysUserWecomEntity.getId() != null && tblSysUserWecomEntity.getId() > 0;
    }
}