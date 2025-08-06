package com.shmashine.camera.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.shmashine.camera.dao.TblSysFileDao;
import com.shmashine.camera.entity.TblSysFile;
import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.camera.service.FileService;
import com.shmashine.camera.utils.FileTypeJudge;
import com.shmashine.common.entity.TblFaultTemp;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.hkcamerabyys.client.RemoteHikEzvizClient;

/**
 * 文件接口
 *
 * @author little.li
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private RemoteHikEzvizClient hikEzvizClient;

    @Resource(type = TblSysFileDao.class)
    private TblSysFileDao tblSysFileDao;

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
        tblSysFileDao.insert(file);
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
            tblSysFileDao.insert(file);
        });

    }

    @Override
    public TblFaultTemp getFalutTempById(String faultId) {
        return tblSysFileDao.getFalutTempById(faultId);
    }

    @Override
    public String getFaultTempImage(String faultId) {
        return tblSysFileDao.getFaultTempImage(faultId);
    }

    @Override
    public String fileReDownload(String faultId, String startTime, String endTime) {

        return hikEzvizClient.videoFileReDownload(faultId, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult fileReplace(String faultId, Integer fileType, MultipartFile file) {

        Integer type = null;
        //1 表示图片,2 表示文档,3 表示视频,4 表示种子,5 表示音乐,6 表示其它
        try {
            type = FileTypeJudge.isFileType(FileTypeJudge.getType(file.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (type != 1 && type != 3) {
            return new ResponseResult("1111", "msg1_013");
        }

        //获取url
        String videoUrl = tblSysFileDao.getVideoUrl(faultId, 2, fileType);

        String fileName;

        if (videoUrl == null) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String fileLastName = ".mp4";
            if (fileType == 0) {
                fileLastName = ".jpg";
            }
            if (fileType == 1) {
                fileLastName = ".mp4";
            }

            fileName = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + faultId + fileLastName;
        } else {

            fileName = videoUrl.substring(48);

            //删除文件表
            TblSysFile tblSysFile = new TblSysFile();
            tblSysFile.setVFileType(String.valueOf(fileType));
            tblSysFile.setVBusinessId(faultId);
            tblSysFile.setIBusinessType(2);

            tblSysFileDao.deleteByEntity(tblSysFile);
        }

        //文件上传
        try {
            ossUpload(fileName, file);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败。。。");
        }

        //落表
        TblSysFile tblSysFile = new TblSysFile();
        tblSysFile.setVFileId(IdUtil.createSnowflake(1, 1).nextIdStr());
        tblSysFile.setVBusinessId(faultId);
        tblSysFile.setVUrl("https://oss-maixin.oss-cn-shanghai.aliyuncs.com/" + fileName);
        tblSysFile.setVFileName(fileName);
        tblSysFile.setIBusinessType(2);
        tblSysFile.setDtCreateTime(new Date());
        tblSysFile.setVFileType(String.valueOf(fileType));
        tblSysFileDao.insert(tblSysFile);

        return ResponseResult.successObj("文件正在上传......");
    }

    /**
     * 文件上传
     *
     * @param fileName
     * @param file
     * @throws IOException
     */
    private void ossUpload(String fileName, MultipartFile file) throws IOException {

        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";

        String accessKeyId = "aaa";

        String accessKeySecret = "aaa";

        String bucketName = "oss-maixin";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName, fileName, file.getInputStream());

        ossClient.shutdown();
    }

}
