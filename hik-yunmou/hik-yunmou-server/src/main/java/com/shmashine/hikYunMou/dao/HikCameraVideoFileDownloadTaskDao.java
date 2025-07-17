package com.shmashine.hikYunMou.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.hikYunMou.entity.HikCameraVideoFileDownloadTask;

/**
 * @author  jiangheng
 * @version 2022/11/10 16:48
 * @description: com.shmashine.hikvPlatform.dao
 */
@Mapper
public interface HikCameraVideoFileDownloadTaskDao extends BaseMapper<HikCameraVideoFileDownloadTask> {

    @Select("SELECT * " +
            "FROM tbl_hikCamera_fileDownload_task " +
            "WHERE  download_failed_num < 5 " +
            "AND (end_time < DATE_SUB( NOW( ), INTERVAL 1 MINUTE ) OR fault_type = 0) " +
            "AND (file_status = 0 OR  file_status = 3) ")
    List<HikCameraVideoFileDownloadTask> queryDownloadtaskList();
}
