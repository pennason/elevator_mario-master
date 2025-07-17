package com.shmashine.api.service.device;

import java.util.List;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.device.SearchDeviceFileModule;
import com.shmashine.api.module.device.UploadFileModule;
import com.shmashine.common.entity.TblDeviceFile;

/**
 * 设备升级文件管理
 *
 * @author little.li
 */
public interface TblDeviceFileServiceI {


    /**
     * 获取设备升级文件列表
     */
    PageListResultEntity searchDeviceFileList(SearchDeviceFileModule deviceFileModule);


    void saveByUploadFile(String fileUrl, String fileName, String crc, UploadFileModule uploadFileModule);

    TblDeviceFile getByDeviceFileId(String deviceFileId);

    TblDeviceFile getByDeviceFileName(String fileName);

    /**
     * 设备升级文件修或删除
     *
     * @param uploadFileModule
     */
    void modifyOrDeleteFileInformation(UploadFileModule uploadFileModule);

    List<String> getHwVersions(String eType);
}