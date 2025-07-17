package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.shmashine.pm.api.entity.TblPmImage;

public interface TblPmImageDao {

    TblPmImage getById(@NotNull String vPmImageId);

    TblPmImage getByVTargetId(@NotNull String vTargetId);

    int insert(@NotNull TblPmImage tblPmImage);

    int update(@NotNull TblPmImage tblPmImage);

    int insertBatch(@NotEmpty List<TblPmImage> list);

    int updateBatch(@NotEmpty List<TblPmImage> list);

    int deleteById(@NotEmpty String vPmImageId);

    int deleteByTargetId(@NotEmpty String vTargetId);
}
