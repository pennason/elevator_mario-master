package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.siteInvestigation.SearchSiteInvestigationModule;
import com.shmashine.common.entity.TblSiteInvestigation;


public interface TblSiteInvestigationDao {
    /**
     * 查询【请填写功能名称】
     *
     * @param vSiteInvestigationId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TblSiteInvestigation selectTblSiteInvestigationById(@Param("vSiteInvestigationId") String vSiteInvestigationId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TblSiteInvestigation> selectTblSiteInvestigationList(@Param("tblSiteInvestigation") TblSiteInvestigation tblSiteInvestigation);

    /**
     * 新增【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    public int insertTblSiteInvestigation(@Param("tblSiteInvestigation") TblSiteInvestigation tblSiteInvestigation);

    /**
     * 修改【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    public int updateTblSiteInvestigation(@Param("tblSiteInvestigation") TblSiteInvestigation tblSiteInvestigation);

    /**
     * 删除【请填写功能名称】
     *
     * @param tblSiteInvestigation 【请填写功能名称】
     * @return 结果
     */
    public int deleteTblSiteInvestigationById(@Param("tblSiteInvestigation") TblSiteInvestigation tblSiteInvestigation);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param vSiteInvestigationIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTblSiteInvestigationByIds(String[] vSiteInvestigationIds);


    List<Map<String, Object>> search(@Param("searchSiteInvestigationModule") SearchSiteInvestigationModule searchSiteInvestigationModule);
}
