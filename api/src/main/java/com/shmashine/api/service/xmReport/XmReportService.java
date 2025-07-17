package com.shmashine.api.service.xmReport;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shmashine.api.entity.TblRequestXmReport;
import com.shmashine.api.entity.base.PageListResultEntity;

/**
 * @author jiangheng
 * @date 2020/12/28 —— 11:16
 * 雄迈报告
 */
@Service
public interface XmReportService {

    /**
     * 分页查询发生故障的摄像头
     *
     * @param tblRequestXmReport
     * @return
     */
    PageListResultEntity queryXmReportBypage(TblRequestXmReport tblRequestXmReport, List<String> eleCodeList);

    /**
     * 手动获取视频
     *
     * @param faultId
     * @return
     */
    String getUrlByHand(String faultId);
}
