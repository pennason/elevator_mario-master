package com.shmashine.camera.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.common.entity.TblFaultTemp;

/**
 * 文件接口
 *
 * @author little.li
 */
public interface FileService {


    /**
     * 保存文件路径
     *
     * @param fileName 文件名称
     * @param faultId  关联的故障id
     */
    void saveVideoFile(String fileName, String faultId);


    /**
     * 保存文件路径
     *
     * @param fileName 文件名称
     * @param faultId  关联的故障id
     */
    void saveImageFile(List<String> fileName, String faultId);

    TblFaultTemp getFalutTempById(String faultId);

    /**
     * 获取故障审核图片文件
     *
     * @param faultId
     * @return
     */
    String getFaultTempImage(String faultId);

    /**
     * 取证文件重新下载
     *
     * @param faultId
     * @param startTime
     * @param endTime
     * @return
     */
    String fileReDownload(String faultId, String startTime, String endTime);

    /**
     * 取证文件手动上传替换
     *
     * @param faultId  故障id
     * @param fileType 文件类型
     * @param file     文件
     * @return
     */
    ResponseResult fileReplace(String faultId, Integer fileType, MultipartFile file);
}
