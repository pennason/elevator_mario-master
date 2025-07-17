package com.shmashine.fault.file.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.fault.file.entity.TblSysFile;

/**
 * tbl_sys_file
 */
@Mapper
public interface TblSysFileDao {

    TblSysFile getById(@NotNull String vFileId);

    List<TblSysFile> listByEntity(TblSysFile tblSysFile);

    List<TblSysFile> getByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysFile tblSysFile);

    int insertBatch(@NotEmpty @Param("list") List<TblSysFile> list);

    int update(@NotNull TblSysFile tblSysFile);

    int updateByField(@NotNull @Param("where") TblSysFile where, @NotNull @Param("set") TblSysFile set);

    int updateBatch(@NotEmpty List<TblSysFile> list);

    int deleteById(@NotNull String vFileId);

    int deleteByEntity(@NotNull TblSysFile tblSysFile);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysFile tblSysFile);

    List<TblSysFile> getFilesById(String vFaultId);
}