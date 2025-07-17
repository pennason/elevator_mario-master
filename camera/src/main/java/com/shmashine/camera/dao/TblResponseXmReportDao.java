package com.shmashine.camera.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblResponseXmReport;

/**
 * 雄迈回调记录数据处理
 *
 * @author Dean Winchester
 */
@Mapper
public interface TblResponseXmReportDao {

    Integer insertResponeXmReport(TblResponseXmReport tblResponseXmReport);

    Integer updateResponeXmReport(TblResponseXmReport tblResponseXmReport);

    /**
     * 根据故障id查询记录
     *
     * @param faultId
     * @return
     */
    TblResponseXmReport findResponeXmReportByFaultId(String faultId);

    /**
     * 根据请求类型状态获取文件记录
     *
     * @param actionType
     * @return
     */
    List<TblResponseXmReport> findResponeXmReportByStatus(@Param("actionType") String actionType);

    /**
     * 查找数据库中需要下载取证视频的记录（包括：待下载[0], 下载失败[3], 下载中[2]超时（超时10分钟） ）
     *
     * @return
     */
    List<TblResponseXmReport> findNeedDownloadVedioList();

    /**
     * 查找数据库中需要上传取证视频的记录
     *
     * @return
     */
    List<TblResponseXmReport> findNeedUploadOSSVedioList();

    /**
     * 根据请求状态和请求类型获取文件记录
     *
     * @param fileStatus
     * @param actionType
     * @return
     */
    List<TblResponseXmReport> findResponeXmReportByFileStatus(@Param("fileStatus") String fileStatus, @Param("actionType") String actionType);

    /**
     * 根据文件名获取文件记录
     *
     * @param url
     * @return
     */
    TblResponseXmReport findResponeXmReportByUrl(@Param("url") String url);

    /**
     * 将载时间超过一天的视频置为下载失败
     */
    void autoClearnDownloadingVideo();
}
