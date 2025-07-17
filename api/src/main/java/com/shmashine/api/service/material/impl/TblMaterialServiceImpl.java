package com.shmashine.api.service.material.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblMaterialDao;
import com.shmashine.api.service.material.TblMaterialService;
import com.shmashine.common.entity.TblMaterial;

@Service
public class TblMaterialServiceImpl implements TblMaterialService {

    @Resource(type = TblMaterialDao.class)
    private TblMaterialDao tblMaterialDao;

    @Override
    public List<TblMaterial> getMaterialList() {
        return tblMaterialDao.getMaterialList();
    }
}
