package com.shmashine.api.service.externalpush.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizExternalPushDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.externalpush.input.SearchExternalPushModule;
import com.shmashine.api.service.externalpush.BizExternalPushService;
import com.shmashine.common.constants.SystemConstants;

/**
 * @PackgeName: com.shmashine.api.service.externalpush.impl
 * @ClassName: BizExternalPushServiceImpl
 * @Date: 2020/7/818:38
 * @Author: LiuLiFu
 * @Description: 对外推送管理业务
 */
@Service
public class BizExternalPushServiceImpl implements BizExternalPushService {

    private BizExternalPushDao bizExternalPushDao;

    @Autowired
    public BizExternalPushServiceImpl(BizExternalPushDao bizExternalPushDao) {
        this.bizExternalPushDao = bizExternalPushDao;
    }

    /**
     * @param searchExternalPushModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchExternalPushList(SearchExternalPushModule searchExternalPushModule) {
        Integer pageIndex = searchExternalPushModule.getPageIndex();
        Integer pageSize = searchExternalPushModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizExternalPushDao.searchExternalPushList(searchExternalPushModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }
}
