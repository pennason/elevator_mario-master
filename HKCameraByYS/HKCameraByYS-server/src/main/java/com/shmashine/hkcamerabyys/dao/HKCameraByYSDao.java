package com.shmashine.hkcamerabyys.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.CameraStatusRecordEntity;
import com.shmashine.hkcamerabyys.client.entity.TblHKCameraDownload;

/**
 * HKCameraByYSDao
 *
 * @author jiangheng
 * @version v1.0 - 2021/11/8 10:52
 */
@Mapper
public interface HKCameraByYSDao {

    /**
     * 上传海康萤石云历史视频下载记录
     *
     * @param hkCameraDownload 下载记录
     */
    int insertHKCameraDownload(TblHKCameraDownload hkCameraDownload);

    /**
     * 上传故障视频文件表
     *
     * @param fileId       文件id
     * @param businessId   故障id
     * @param url          地址
     * @param fileName     文件name
     * @param businessType 类型
     * @param createTime   创建时间
     * @param fileType     文件类型
     */
    void addTblSysFile(@Param("fileId") String fileId, @Param("businessId") String businessId,
                       @Param("url") String url, @Param("fileName") String fileName,
                       @Param("businessType") int businessType, @Param("createTime") Date createTime,
                       @Param("fileType") String fileType);

    List<TblHKCameraDownload> findNeedDownloadFileList();

    void updateDownloadReport(TblHKCameraDownload tblHKCameraDownload);

    List<TblHKCameraDownload> retryDownloadFailReportTask();

    /**
     * 根据故障id获取海康取证记录
     *
     * @param faultId 故障id
     */
    TblHKCameraDownload queryHkDownloadReportById(@Param("faultId") String faultId, @Param("fileType") int fileTyp);

    /**
     * 获取取证基本信息
     *
     * @param faultId 故障id
     */
    HashMap<String, Object> queryDownloadInfoById(String faultId);

    /**
     * 根据故障id删除取证文件
     *
     * @param faultId 故障id
     */
    void delSysFileById(@Param("faultId") String faultId, @Param("fileType") int fileType);

    /**
     * 是否下载视频  未安装不下载
     *
     * @param elevatorCode 电梯编号
     */
    Boolean isNeedDownVideo(String elevatorCode);

    /**
     * 凌晨重新录制
     */
    void freeTimeRetryDownload();

    List<TblHKCameraDownload> getFiledFileList();

    /**
     * 添加摄像头上下线记录
     *
     * @param cameraStatusRecord 上下线记录
     */
    void insertCameraStatusRecord(CameraStatusRecordEntity cameraStatusRecord);

    /**
     * 修改摄像头当前状态
     *
     * @param cameraStatusRecord 在线状态
     */
    void updateCameraStatus(CameraStatusRecordEntity cameraStatusRecord);

    /**
     * 根据CameraId 获取对应记录
     * @param cameraId 摄像头ID
     * @return 结果
     */
    //TblCameraEntity getCameraInfoById(@Param("cameraId") String cameraId);

    /**
     * 根据电梯编号 获取摄像头记录
     *
     * @param elevatorCode 电梯便哈
     * @return 摄像头信息
     */
    TblCameraDTO getCameraInfoByElevatorCode(@Param("elevatorCode") String elevatorCode);

    /**
     * 根据摄像头序列号获取电梯id
     *
     * @param serialNumber 摄像头序列号
     * @return 电梯id
     */
    String getElevatorIdBySerialNumber(String serialNumber);
}
