package com.shmashine.fault.message.handle;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.fault.file.entity.TblSysFile;
import com.shmashine.fault.file.service.TblSysFileServiceI;


/**
 * 故障录制视频、截取图片文件保存
 *
 * @author little.li
 */
@Component
public class HlsHandle {

    @Autowired
    private TblSysFileServiceI fileService;


    /**
     * 故障录制视频、截取图片文件保存
     */
    @Transactional(rollbackFor = Exception.class)
    public void proHandle(String value) {

        JSONObject jsonObject = JSONObject.parseObject(value);

        // 沿用之前代码，实际传输的是故障id
        String faultId = jsonObject.getString("workOrderId");

        String fileName = jsonObject.getString("fileName");
        Integer photoCount = jsonObject.getInteger("photoCount");
        String date = jsonObject.getString("date");

        String photoName = fileName.substring(0, fileName.lastIndexOf("."));

        Date now = new Date();
        Set<TblSysFile> setList = new HashSet<>();


        for (int i = 1; i <= photoCount; i++) {
            TblSysFile file = new TblSysFile();
            String id = SnowFlakeUtils.nextStrId();
            file.setVFileId(id);
            file.setVBusinessId(faultId);
            file.setIBusinessType(2);
            file.setVFileType(String.valueOf(0));
            file.setVFileName(photoName + "-" + i + ".jpg");
            file.setVUrl(OSSUtil.OSS_URL + OSSUtil.OREO_PROJECT_FAULT_URL + date
                    + File.separator + photoName + "-" + i + ".jpg");
            file.setDtCreateTime(now);
            setList.add(file);
        }

        TblSysFile file = new TblSysFile();
        String id = SnowFlakeUtils.nextStrId();
        file.setVFileId(id);
        file.setVBusinessId(faultId);
        file.setIBusinessType(2);
        file.setVFileType(String.valueOf(1));
        file.setVFileName(fileName);
        file.setVUrl(OSSUtil.OSS_URL + OSSUtil.OREO_PROJECT_FAULT_URL + date + File.separator + fileName);
        file.setDtCreateTime(now);
        setList.add(file);
        List<TblSysFile> list = new ArrayList<>(setList);
        fileService.insertBatch(list);

    }

}
