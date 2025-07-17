package com.shmashine.socket.file.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.socket.file.dao.FileDao;
import com.shmashine.socket.file.entity.TblSysFile;
import com.shmashine.socket.file.service.FileService;

/**
 * 文件接口
 *
 * @author little.li
 */
@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private FileDao failDao;


    /**
     * 保存文件路径
     *
     * @param fileName 文件路径
     * @param faultId  关联的故障id
     */
    @Override
    public void saveVideoFile(String fileName, String faultId) {
        TblSysFile file = new TblSysFile();

        file.setVFileId(SnowFlakeUtils.nextStrId());
        file.setVFileType(String.valueOf(1));
        file.setVFileName(fileName);
        file.setVUrl(OSSUtil.OSS_URL + fileName);
        file.setDtCreateTime(new Date());
        file.setDtModifyTime(new Date());
        file.setIBusinessType(2);
        file.setVBusinessId(faultId);
        failDao.insert(file);
    }


    @Override
    public void saveImageFile(List<String> fileNameList, String faultId) {

        TblSysFile file = new TblSysFile();
        file.setDtCreateTime(new Date());
        file.setDtModifyTime(new Date());
        file.setIBusinessType(2);
        file.setVFileType(String.valueOf(0));
        file.setVBusinessId(faultId);

        fileNameList.forEach(value -> {
            file.setVFileId(SnowFlakeUtils.nextStrId());
            file.setVFileName(value);
            file.setVUrl(OSSUtil.OSS_URL + value);
            failDao.insert(file);
        });

    }


    @Override
    public void saveFile(String fileName, String faultId) {

        TblSysFile file = new TblSysFile();
        file.setDtCreateTime(new Date());
        file.setDtModifyTime(new Date());
        file.setIBusinessType(2);
        file.setVFileType(String.valueOf(0));
        file.setVBusinessId(faultId);
        file.setVFileId(SnowFlakeUtils.nextStrId());
        file.setVFileName(fileName);
        file.setVUrl(OSSUtil.OSS_URL + fileName);
        failDao.insert(file);

    }


}
