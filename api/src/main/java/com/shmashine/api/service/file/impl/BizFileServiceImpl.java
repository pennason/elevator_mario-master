package com.shmashine.api.service.file.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizFileDao;
import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.service.file.BizFileService;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * @PackgeName: com.shmashine.api.service.file.impl
 * @ClassName: BizFileServiceImpl
 * @Date: 2020/7/2116:18
 * @Author: LiuLiFu
 * @Description: 文件
 */
@Service
public class BizFileServiceImpl implements BizFileService {

    private TblSysFileDao tblSysFileDao;
    private BizFileDao bizFileDao;

    @Autowired
    public BizFileServiceImpl(TblSysFileDao tblSysFileDao, BizFileDao bizFileDao) {
        this.tblSysFileDao = tblSysFileDao;
        this.bizFileDao = bizFileDao;
    }


    /**
     * 保存工单
     *
     * @param fileNameList
     * @param workOrderDetailId
     */
    @Override
    public void insertWorkOrderBatch(List<String> fileNameList, String workOrderDetailId) {
        for (String fileName : fileNameList) {
            TblSysFile file = new TblSysFile();
            file.setVFileId(SnowFlakeUtils.nextStrId());
            file.setVFileType(String.valueOf(0));
            file.setVFileName(fileName);
            file.setVUrl(OSSUtil.OSS_URL + fileName);
            file.setDtCreateTime(new Date());
            file.setDtModifyTime(new Date());
            file.setIBusinessType(1);
            file.setVBusinessId(workOrderDetailId);
            tblSysFileDao.insert(file);
        }
    }

    /**
     * @param elevatorId
     */
    @Override
    public void deleteElevatorDetailImages(String fileId, String elevatorId) {
        bizFileDao.deleteFile(fileId, elevatorId, 3);
    }

    @Override
    public void deleteSystemLogImages(String businessId) {
        bizFileDao.deleteFileAll(businessId, 4);
    }

    @Override
    public List<Map> getFileElevatorImg(String elevatorId) {
        return bizFileDao.getElevatorImg(elevatorId, 3);
    }

    @Override
    public List<Map> getFileElevatorQRCodeImg(String elevatorId) {
        return bizFileDao.getElevatorImg(elevatorId, 6);
    }


}
