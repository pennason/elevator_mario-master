package com.shmashine.hkCameraForTY.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.hkCameraForTY.entity.TblResponseHkReport;

/**
 * DO
 * @author: jiangheng
 * @version: 1.0
 * @date: 2021/6/7 9:57
 */
public interface CameraForTYDao {

    List<String> getFaultBy37ok(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    List<String> getFaultBy37fault(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 添加下载视频失败记录
     */
    void add(@Param("fileStatus") int fileStatus, @Param("createTime") Date createTime,
             @Param("returnCode") int returnCode, @Param("elevatorCode") String elevatorCode,
             @Param("faultId") String faultId, @Param("startTime") Date startTime,
             @Param("endTime") Date endTime, @Param("requestFailedNum") int requestFailedNum,
             @Param("comment") String comment, @Param("faultType") String faultType);

    /**
     * 取证视频上传阿里云成功落表
     */
    void addTblSysFile(@Param("fileId") String fileId, @Param("businessId") String businessId,
                       @Param("url") String url, @Param("fileName") String fileName,
                       @Param("businessType") int businessType, @Param("createTime") Date createTime,
                       @Param("fileType") String fileType);

    /**
     * 查询失败记录
     */
    List<TblResponseHkReport> queryResHkReport();

    /**
     * 更新海康下载失败记录
     */
    void updateHkReport(@Param("fileStatus") int fileStatus, @Param("businessId") String faultId,
                        @Param("requestFailedNum") Integer requestFailedNum);

    /**
     * 根据电梯编号获取海康天翼物联配置
     * @param elevatorCode 电梯编号
     */
    HashMap<String, String> getHkCameraInfo(@Param("elevatorCode") String elevatorCode);

    /**
     * 获取故障开始时间结束时间
     * @param workOrderId 工单id
     */
    Map<String, Object> getStartAndEndTime(@Param("workOrderId") String workOrderId);
}
