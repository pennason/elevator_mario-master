package com.shmashine.api.service.material;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblMaterialAttribute;

public interface TblMaterialAttributeService {
    List<TblMaterialAttribute> getMaterialAttributesByMaterialId(@Param("materialId") String materialId);
}
