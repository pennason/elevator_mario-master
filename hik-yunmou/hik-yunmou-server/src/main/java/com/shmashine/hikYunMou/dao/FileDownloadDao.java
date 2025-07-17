package com.shmashine.hikYunMou.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.hikYunMou.entity.FileDownloadTbl;

/**
 * @Author jiangheng
 * @create 2022/10/11 11:22
 */
@Mapper
public interface FileDownloadDao extends BaseMapper<FileDownloadTbl> {
}
