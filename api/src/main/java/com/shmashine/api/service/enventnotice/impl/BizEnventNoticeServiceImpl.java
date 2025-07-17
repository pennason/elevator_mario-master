package com.shmashine.api.service.enventnotice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizEnventNoticeDao;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.enoentnotice.input.SearchEnoentNoticeModule;
import com.shmashine.api.service.enventnotice.BizEnventNoticeService;
import com.shmashine.common.constants.SystemConstants;

import lombok.RequiredArgsConstructor;

/**
 * @PackgeName: com.shmashine.api.service.enventnotice.impl
 * @ClassName: BizEnventNoticeServiceImpl
 * @Date: 2020/7/1010:35
 * @Author: LiuLiFu
 * @Description: 查询事件 通知
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizEnventNoticeServiceImpl implements BizEnventNoticeService {

    private final BizEnventNoticeDao bizEnventNoticeDao;
    private final TblElevatorDao elevatorDao;


    /**
     * 查询事件通知需关注电梯列表
     *
     * @param searchElevatorProjectListModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchEnoentNotice(SearchEnoentNoticeModule searchElevatorProjectListModule) {
        Integer pageIndex = searchElevatorProjectListModule.getPageIndex();
        Integer pageSize = searchElevatorProjectListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        String noticeType = searchElevatorProjectListModule.getNoticeType();
        String userId = searchElevatorProjectListModule.getUserId();
        boolean adminFlag = searchElevatorProjectListModule.isAdminFlag();
        // 补充电梯IDS
        List<String> elevatorIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchElevatorProjectListModule.getProjectIds())) {
            elevatorIds = elevatorDao.getElevatorIdsByProjectIds(searchElevatorProjectListModule.getProjectIds());
        }

        PageInfo<Map> tPageInfo = new PageInfo<>(bizEnventNoticeDao.searchEnoentNotice(noticeType, adminFlag, userId, elevatorIds), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }
}
