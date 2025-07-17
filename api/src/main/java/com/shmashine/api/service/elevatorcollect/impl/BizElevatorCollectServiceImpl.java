package com.shmashine.api.service.elevatorcollect.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizElevatorCollectDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevatorCollect.input.OperationElevatorCollectModule;
import com.shmashine.api.module.elevatorCollect.input.SearchElevatorCollectModule;
import com.shmashine.api.service.elevatorcollect.BizElevatorCollectService;
import com.shmashine.common.constants.SystemConstants;

/**
 * @PackgeName: com.shmashine.api.service.elevatorcollect.impl
 * @ClassName: BizElevatorCollectServiceImpl
 * @Date: 2020/7/115:00
 * @Author: LiuLiFu
 * @Description: 收藏电梯业务接口
 */
@Service
public class BizElevatorCollectServiceImpl implements BizElevatorCollectService {

    private BizElevatorCollectDao bizElevatorCollectDao;

    @Autowired
    public BizElevatorCollectServiceImpl(BizElevatorCollectDao bizElevatorCollectDao) {
        this.bizElevatorCollectDao = bizElevatorCollectDao;
    }

    /**
     * 收藏列表
     *
     * @param searchElevatorCollectModule
     * @return
     */
    @Override
    public PageListResultEntity searchElevatorList(SearchElevatorCollectModule searchElevatorCollectModule) {
        Integer pageIndex = searchElevatorCollectModule.getPageIndex();
        Integer pageSize = searchElevatorCollectModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizElevatorCollectDao.searchElevatorCollectList(searchElevatorCollectModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
    }

    /**
     * 判断用户是否收藏电梯
     *
     * @param operationElevatorCollectModule
     * @return
     */
    @Override
    public boolean searchUserCollectElevatorInfo(OperationElevatorCollectModule operationElevatorCollectModule) {
        Map map = bizElevatorCollectDao.searchUserCollectElevatorInfo(operationElevatorCollectModule.getUserId(), operationElevatorCollectModule.getElevatorId());
        if (map == null) {
            return false;
        }
        String flag = map.get("flag").toString();
        if (flag.equals("1")) {
            return true;
        }
        return false;
    }


}
