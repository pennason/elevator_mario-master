package com.shmashine.api.service.material;

import java.util.List;

import com.shmashine.common.entity.TblMaterial;

public interface TblMaterialService {
    /*
     * @params java.lang.String materialID
     * @author depp.yu
     */
    public List<TblMaterial> getMaterialList();
}
