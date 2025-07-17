package com.shmashine.camera.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.camera.entity.TblSysFile;

/**
 * 文件接口
 *
 * @author little.li
 */
@Mapper
public interface FileDao {

    /**
     * 保存文件
     */
    Integer insert(TblSysFile file);


}
