package com.shmashine.pm.api.service.investigateBill.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizInvestigateBillDao;
import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblInvestigateBillDto;
import com.shmashine.pm.api.module.investigateBill.InvestigateBillModule;
import com.shmashine.pm.api.module.investigateBill.input.SearchTaskBillModule;
import com.shmashine.pm.api.service.investigateBill.BizInvestigateBillService;

@Service
public class BizInvestigateBillServiceImpl implements BizInvestigateBillService {

    @Autowired
    BizInvestigateBillDao bizInvestigateBillDao;

    /**
     * 现勘单列表
     *
     * @param searchTaskBillModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchInvestigateBillList(SearchTaskBillModule searchTaskBillModule) {
        Integer pageIndex = searchTaskBillModule.getPageIndex();
        Integer pageSize = searchTaskBillModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizInvestigateBillDao.searchInvestigateBillList(searchTaskBillModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }

    /**
     * 获取所有现勘单结果
     *
     * @param searchTaskBillModule
     * @return
     */
    @Override
    public List<Map> searchAllInvestigateBill(SearchTaskBillModule searchTaskBillModule) {
        return bizInvestigateBillDao.searchInvestigateBillList(searchTaskBillModule);

    }

    @Override
    public TblInvestigateBill getByBillId(String vInvestigateBillId) {
        return bizInvestigateBillDao.getByBillId(vInvestigateBillId);
    }

    @Override
    public Integer getElevatorCountByTaskId(String vInvestigateTaskId, String vInvestigateBillId) {
        return bizInvestigateBillDao.getElevatorCountByTaskId(vInvestigateTaskId, vInvestigateBillId);
    }

    @Override
    public List<Map> getAllBillByInvestigateTaskId(String vInvestigateTaskId) {
        return bizInvestigateBillDao.getAllBillByInvestigateTaskId(vInvestigateTaskId);
    }

    @Override
    public TblInvestigateBillDto getBillInfoByInvestigateBillId(String vInvestigateBillId) {
        return bizInvestigateBillDao.getBillInfoByInvestigateBillId(vInvestigateBillId);
    }

    @Override
    public List<Map> selectRelatedInfosByTaskId(String vInvestigateTaskId) {
        return bizInvestigateBillDao.selectRelatedInfosByTaskId(vInvestigateTaskId);
    }

    @Override
    public Map selectRelatedInfo(String vInvestigateBillId) {
        return bizInvestigateBillDao.selectRelatedInfo(vInvestigateBillId);
    }

    @Override
    public List<Map> statusCountByVillageId(String vVillageId) {
        return bizInvestigateBillDao.statusCountByVillageId(vVillageId);
    }

    @Override
    public List<Integer> getAllStatus(SearchTaskBillModule searchTaskBillModule) {
        return bizInvestigateBillDao.getAllStatus(searchTaskBillModule);
    }

    @Override
    public List<Map> selectByBillModule(InvestigateBillModule investigateBillModule) {
        return bizInvestigateBillDao.selectByBillModule(investigateBillModule);
    }
}
