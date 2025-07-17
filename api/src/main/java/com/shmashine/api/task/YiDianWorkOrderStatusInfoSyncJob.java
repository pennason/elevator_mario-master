package com.shmashine.api.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson2.JSON;
import com.shmashine.api.dao.TblThirdPartyRuijinEnventDao;
import com.shmashine.api.entity.yidian.RepairOrderExcel;
import com.shmashine.api.util.YiDianPlatformRequestUtils;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  jiangheng
 * @version 2023/4/26 16:26
 * @description: com.shmashine.api.task
 * <p>
 * 仪电工单信息补充
 */
@Slf4j
@Component
public class YiDianWorkOrderStatusInfoSyncJob {

    @Resource(type = TblThirdPartyRuijinEnventDao.class)
    private TblThirdPartyRuijinEnventDao tblThirdPartyRuijinEnventDao;

    @Resource
    private YiDianPlatformRequestUtils yiDianPlatformRequestUtils;

    /**
     * 急修工单信息补充
     */
    @XxlJob("repairOrderInfoSupplement")
    public void repairOrderInfoSupplement() {

        //获取信息未补充的急修工单（根据梯分组获取最大时间和最小时间）
        List<Map<String, String>> needSupplementElevaotrs = tblThirdPartyRuijinEnventDao.getNeedSupplementElevaotr();

        //获取excel信息
        needSupplementElevaotrs.forEach(elevaotr -> {

            String fileUrl = yiDianPlatformRequestUtils.getRepairOrderExcel(elevaotr.get("registerNumber"), elevaotr.get("startTime"), elevaotr.get("endTime"));

            //解析excel
            parsePairExcelAndRepairOrderInfo(fileUrl);

        });

    }


    /**
     * 下载解析excel 并补充工单信息 —— 急修工单
     *
     * @param fileUrl
     */
    private void parsePairExcelAndRepairOrderInfo(String fileUrl) {

        //下载并解析excel
        InputStream inputStream = getInputStream(fileUrl);

        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, RepairOrderExcel.class, new AnalysisEventListener<RepairOrderExcel>() {

                @Override
                public void invoke(RepairOrderExcel data, AnalysisContext analysisContext) {

                    log.info("解析到一条数据:{}", JSON.toJSONString(data));

//                    if ("已完成".equals(data.getOrderStatus()) || "已确认".equals(data.getOrderStatus())) {

                    //补充工单信息
                    tblThirdPartyRuijinEnventDao.repairOrderInfoSupplement(data.getRegisterNumber(), data.getReportTime(), data.getDescription());

//                    }

                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                    log.info("所有数据解析完成！");
                }

            }).build();

            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);

        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }

    private InputStream getInputStream(String fileUrl) {
        InputStream inputStream = null;
        try {
            URL urlfile = new URL(fileUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            inputStream = httpUrl.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
