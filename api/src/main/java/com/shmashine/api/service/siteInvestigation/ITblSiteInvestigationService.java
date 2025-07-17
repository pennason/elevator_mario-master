package com.shmashine.api.service.siteInvestigation;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.siteInvestigation.SearchSiteInvestigationModule;
import com.shmashine.common.entity.TblSiteInvestigation;

public interface ITblSiteInvestigationService {
    /**
     * 查询【请填写功能名称】
     *
     * @param vSiteInvestigationId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TblSiteInvestigation selectTblSiteInvestigationById(String vSiteInvestigationId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TblSiteInvestigation> selectTblSiteInvestigationList(TblSiteInvestigation tblSiteInvestigation);

    /**
     * 新增【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    public int insertTblSiteInvestigation(TblSiteInvestigation tblSiteInvestigation);

    /**
     * 修改【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    public int updateTblSiteInvestigation(TblSiteInvestigation tblSiteInvestigation);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTblSiteInvestigationByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param tblSiteInvestigation 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTblSiteInvestigationById(@Param("tblSiteInvestigation") TblSiteInvestigation tblSiteInvestigation);

    public PageListResultEntity search(@Param("searchSiteInvestigationModule") SearchSiteInvestigationModule searchSiteInvestigationModule);
}
