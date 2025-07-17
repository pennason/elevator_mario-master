package com.shmashine.socket.file.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.socket.file.entity.TblSysFile;

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
    void insert(TblSysFile file);


}
