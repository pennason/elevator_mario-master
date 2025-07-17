package com.shmashine.camera.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import com.shmashine.camera.dao.TblSysFileDao;
import com.shmashine.camera.entity.TblSysFile;
import com.shmashine.camera.model.CameraModule;
import com.shmashine.camera.model.FaultCameraModule;
import com.shmashine.camera.model.ImageHandleRequest;
import com.shmashine.camera.model.Result;
import com.shmashine.camera.model.SearchCamerasModule;
import com.shmashine.camera.model.VideoHandlerRequest;
import com.shmashine.camera.model.XmHlsHttpOrHttpsModule;
import com.shmashine.camera.model.base.PageListResultEntity;
import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblCameraCascadePlatformEntity;
import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.model.request.FaceRecognitionRequest;

/**
 * 摄像头业务接口
 *
 * @author Dean Winchester
 */
public interface CameraServer {

    /**
     * 根据请求类型状态获取文件记录
     */
    List<TblResponseXmReport> findResponeXmReportByStatus(@Param("actionType") String actionType);

    /**
     * 根据请求状态和请求类型获取文件记录
     */
    List<TblResponseXmReport> findResponeXmReportByFileStatus(@Param("fileStatus") String fileStatus,
                                                              @Param("actionType") String actionType);


    /**
     * 基础服务：分页获取摄像头绑定信息
     */
    PageListResultEntity searchElevatorListByPage(SearchCamerasModule searchCamerasModule);

    /**
     * 基础服务：分页获取摄像头信息
     */
    Object searchCamerasListByPage(SearchCamerasModule searchCamerasModule);

    /**
     * API服务调用：根据电梯id获取摄像头信息
     */
    Object getCameraInfoByElevatorId(String elevatorId);

    /**
     * API服务调用：获取封装摄像头信息
     */
    CameraModule getCamera(TblCamera tblCamera);

    /**
     * API服务调用：根据电梯code获取摄像头信息
     */
    TblCamera getByElevatorCode(String code);

    /**
     * API服务调用：获取萤石云访问token
     */
    String getEzopenToken();


    /**
     * API服务调用：保存文件路径
     *
     * @param fileName 文件名称
     * @param faultId  关联的故障id
     */
    TblSysFile saveVideoFile(String fileName, String faultId);

    /**
     * API服务调用：保存文件路径
     *
     * @param fileNameList 文件名称
     * @param faultId      关联的故障id
     */
    void saveImageFile(List<String> fileNameList, String faultId);

    /**
     * Socket服务调用：根据电梯编号 获取rtmp流地址
     *
     * @param elevatorCode 电梯编号
     * @return rtmp流地址
     */
    String getRtmpUrlByElevatorCode(String elevatorCode);


    /**
     * Socket服务调用：通过电梯编号 获取hls流地址
     *
     * @param elevatorCode 电梯编号
     * @return hls流地址
     */
    String getHlsUrlByElevatorCode(String elevatorCode);


    /**
     * Socket服务调用：截取摄像头当前一帧图片，返回图片存储路径
     *
     * @param elevatorCode 电梯编号
     * @return 图片存储路径
     */
    String getCurrentImagePathByElevatorCode(String elevatorCode);


    /**
     * 添加文件上传记录
     */
    Result insertResponeXmReport(TblResponseXmReport tblResponseXmReport);

    /**
     * 更新文件上传记录
     */
    Result updateResponeXmReport(TblResponseXmReport tblResponseXmReport);

    /**
     * 根据文件名（路径）获取当前记录
     */
    TblResponseXmReport findResponeXmReportByUrl(String url);

    /**
     * 请求雄迈
     */
    void requestXmTask();

    /**
     * 雄迈回调后处理
     */
    void responeXmTask();

    /**
     * 根据故障id查询
     */
    TblResponseXmReport findResponeXmReportByFaultId(String faultId);

    TblSysFileDao getTblSysFileDao();

    TblSysFile getById(String vFileId);

    List<TblSysFile> getByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByIds(List<String> ids);

    int insertFile(TblSysFile tblSysFile);

    int insertCamera(TblCamera tblCamera);

    void insertWorkOrderBatch(List<String> fileNameList, String workOrderDetailId);

    int updateFile(TblSysFile tblSysFile);

    int updateCamera(TblCamera tblCamera);

    int updateBatch(List<TblSysFile> list);

    int deleteById(String vFileId);

    int deleteByEntity(TblSysFile tblSysFile);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysFile tblSysFile);


    Object getByCode(String code);

    Object saveAndTest(TblCamera tblCamera);

    String getVedio(String businessNo);

    String getPicture(String businessNo);

    Result xmSaveVideoReport(String fileName);

    Result xmSaveVideo(String fileName);

    Result hkSaveVideo(String value);

    void xmSaveImage(String fileName);

    Object ezSaveVideo(String filePath);

    Object ezSaveImage(String filePath);

    /**
     * 添加摄像头接口
     */
    ResponseResult insert(TblCamera tblCamera);

    Result getCameraXmHistory(String elevatorCode, FaultCameraModule faultCameraModule);

    Result getVedioHlsForHttpsOrHttp(XmHlsHttpOrHttpsModule xmHlsHttpOrHttpsModule);

    PageListResultEntity searchCamerasVedioAndPicByPage(SearchCamerasModule searchCamerasModule);

    ResponseResult videoHandlerApplication(VideoHandlerRequest videoHandlerRequest);

    ResponseResult imageHandleApplication(ImageHandleRequest imageHandleRequest);


    Integer faceRecognition(FaceRecognitionRequest faceRecognitionRequest);

    /**
     * 修改摄像头信息接口
     */
    ResponseResult update(TblCamera tblCamera);

    /**
     * 电梯绑定修改
     */
    ResponseResult updateElevotorBound(TblCamera tblCamera);

    /**
     * 清理下载超过一天的视频
     */
    void autoClearnDownloadingVideo();

    /**
     * 重新调用困人图像识别服务
     */
    void afreshGetRecognitionResult();

    /**
     * 重新下载取证失败的照片
     */
    void afreshDownloadImage();

    /**
     * 根据条件下载摄像头图片或者视频
     *
     * @param request 请求参数
     * @return 结果
     */
    ResponseEntity<String> downloadCameraFileByElevatorCode(CamareMediaDownloadRequestDTO request);

    /**
     * 根据通道级联编码获取摄像头信息
     */
    TblCameraCascadePlatformEntity queryCameraInfoByChannelCode(String channelCode);

    /**
     * 根据电梯编号获取语音对讲接口信息
     *
     * @param elevatorCode 电梯编号
     * @param domain       1:域名 0：IP+端口
     * @return 结果
     */
    ResponseResult getVoiceWssInfoByElevatorCode(String elevatorCode, Integer domain);

    /**
     * 根据摄像头序列号获取语音对讲WSS信息
     *
     * @param cloudNumber 摄像头序列号
     * @param domain      1:域名 0：IP+端口
     * @return 结果
     */
    ResponseResult getVoiceWssInfoByCloudNumber(String cloudNumber, Integer domain);
}
