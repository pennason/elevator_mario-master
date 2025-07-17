package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblMaterialAttribute;

public interface TblMaterialAttributeDao {

    List<TblMaterialAttribute> getMaterialAttributesByMaterId(@Param("materialId") String materialId);
}
