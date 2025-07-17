package com.shmashine.api.service.device.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblDeviceFileDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.device.SearchDeviceFileModule;
import com.shmashine.api.module.device.UploadFileModule;
import com.shmashine.api.properties.EndpointProperties;
import com.shmashine.api.service.device.TblDeviceFileServiceI;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblDeviceFile;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.RequiredArgsConstructor;

/**
 * 设备升级文件管理
 *
 * @author little.li
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TblDeviceFileServiceImpl implements TblDeviceFileServiceI {
    private final TblDeviceFileDao tblDeviceFileDao;
    private final EndpointProperties endpointProperties;


    @Override
    public PageListResultEntity searchDeviceFileList(SearchDeviceFileModule deviceFileModule) {
        Integer pageIndex = deviceFileModule.getPageIndex();
        Integer pageSize = deviceFileModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var hashMapPageInfo = new PageInfo<>(tblDeviceFileDao.searchDeviceFileList(deviceFileModule), pageSize);
        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                hashMapPageInfo.getList());
    }

    @Override
    public void saveByUploadFile(String fileUrl, String fileName, String crc, UploadFileModule uploadFileModule) {
        TblDeviceFile deviceFile = new TblDeviceFile();
        Date date = new Date();
        String deviceFileId = SnowFlakeUtils.nextStrId();
        deviceFile.setDeviceFileId(deviceFileId);
        deviceFile.setFileName(fileName);
        deviceFile.setFileUrl(fileUrl);
        // 设备只支持定向ip的升级方式 提供接口供其访问
        deviceFile.setRequestFileUrl(endpointProperties.getUpgradeFileServer()
                + MessageConstants.DEVICE_UPLOAD_REQUEST_URL + fileName);
        deviceFile.setVersion(uploadFileModule.getVersion());
        deviceFile.setLabel(uploadFileModule.getLabel());
        deviceFile.setSignature(uploadFileModule.getSignature());

        //升级文件,上传文件的硬件版本号
        deviceFile.setHWVersion(uploadFileModule.gethWVersion());

        deviceFile.setCrc(crc);
        deviceFile.setCreateTime(date);
        deviceFile.setModifyTime(date);
        deviceFile.setEType(uploadFileModule.geteType());
        tblDeviceFileDao.insert(deviceFile);
    }

    @Override
    public TblDeviceFile getByDeviceFileId(String deviceFileId) {
        return tblDeviceFileDao.getById(deviceFileId);
    }

    @Override
    public TblDeviceFile getByDeviceFileName(String fileName) {
        return tblDeviceFileDao.getByDeviceFileName(fileName);
    }

    @Override
    public void modifyOrDeleteFileInformation(UploadFileModule uploadFileModule) {
        tblDeviceFileDao.modifyOrDeleteFileInformation(uploadFileModule);
    }

    @Override
    public List<String> getHwVersions(String eType) {
        return tblDeviceFileDao.getHwVersions(eType);
    }


}