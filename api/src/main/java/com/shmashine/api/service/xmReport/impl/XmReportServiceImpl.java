package com.shmashine.api.service.xmReport.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblReportDao;
import com.shmashine.api.entity.TblRequestXmReport;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.service.xmReport.XmReportService;
import com.shmashine.common.constants.SystemConstants;

/**
 * @author jiangheng
 * @date 2020/12/28 —— 11:17
 * 查询分页
 */
@Service("xmReportServiceImpl")
public class XmReportServiceImpl implements XmReportService {

    @Resource
    private TblReportDao tblReportDao;

    @Override
    public PageListResultEntity queryXmReportBypage(TblRequestXmReport tblRequestXmReport, List<String> eleCodeList) {

        //默认页码和条数
        Integer pageIndex = tblRequestXmReport.getPageIndex();
        if (pageIndex == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
        }
        Integer pageSize = tblRequestXmReport.getPageSize();
        if (pageSize == null) {
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);

        //查询数据
        if (eleCodeList == null || eleCodeList.size() == 0) {
            //没有电梯绑定，直接返回null
            return null;
        }
        List<Map<String, Object>> maps = tblReportDao.queryXmReportBypage(tblRequestXmReport, eleCodeList);
        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(maps, pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    @Override
    public String getUrlByHand(String faultId) {

        return tblReportDao.getUrlByHand(faultId);

    }
}
