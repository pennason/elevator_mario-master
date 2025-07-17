package com.shmashine.pm.api.dao;

import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblPmFile;

public interface TblPmFileDao {

    TblPmFile getById(@NotNull String vPmFileId);

    TblPmFile getByEntity(TblPmFile tblPmFile);

    int insert(@NotNull TblPmFile tblPmFile);

    int update(@NotNull TblPmFile tblPmFile);

    int deleteById(@NotNull String vPmFileId);
}
