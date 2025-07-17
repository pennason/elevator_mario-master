package com.shmashine.api.service.siteInvestigation.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.convert.Convert;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.TblSiteInvestigationDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.siteInvestigation.SearchSiteInvestigationModule;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.api.service.siteInvestigation.ITblSiteInvestigationService;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSiteInvestigation;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author depp
 * @date 2021-07-06
 */
@Service
public class TblSiteInvestigationServiceImpl implements ITblSiteInvestigationService {
    @Autowired
    private TblSiteInvestigationDao tblSiteInvestigationDao;
    private BizProjectService bizProjectService;
    private BizElevatorDao bizElevatorDao;

    /**
     * 查询【请填写功能名称】
     *
     * @param vSiteInvestigationId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TblSiteInvestigation selectTblSiteInvestigationById(String vSiteInvestigationId) {
        return tblSiteInvestigationDao.selectTblSiteInvestigationById(vSiteInvestigationId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TblSiteInvestigation> selectTblSiteInvestigationList(TblSiteInvestigation tblSiteInvestigation) {
        return tblSiteInvestigationDao.selectTblSiteInvestigationList(tblSiteInvestigation);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTblSiteInvestigation(TblSiteInvestigation tblSiteInvestigation) {
        return tblSiteInvestigationDao.insertTblSiteInvestigation(tblSiteInvestigation);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTblSiteInvestigation(TblSiteInvestigation tblSiteInvestigation) {
        return tblSiteInvestigationDao.updateTblSiteInvestigation(tblSiteInvestigation);
    }

    /**
     * 删除【请填写功能名称】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTblSiteInvestigationByIds(String ids) {
        return tblSiteInvestigationDao.deleteTblSiteInvestigationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param tblSiteInvestigation 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTblSiteInvestigationById(TblSiteInvestigation tblSiteInvestigation) {
        return tblSiteInvestigationDao.deleteTblSiteInvestigationById(tblSiteInvestigation);
    }

    @Override
    public PageListResultEntity search(@Param("searchSiteInvestigationModule") SearchSiteInvestigationModule searchSiteInvestigationModule) {
        Integer pageIndex = searchSiteInvestigationModule.getPageIndex();
        Integer pageSize = searchSiteInvestigationModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(tblSiteInvestigationDao.search(searchSiteInvestigationModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }
}