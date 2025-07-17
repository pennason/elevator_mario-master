package com.shmashine.api.service.material.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblMaterialAttributeDao;
import com.shmashine.api.service.material.TblMaterialAttributeService;
import com.shmashine.common.entity.TblMaterialAttribute;

@Service
public class TblMaterialAttributeServiceImpl implements TblMaterialAttributeService {

    @Resource(type = TblMaterialAttributeDao.class)
    private TblMaterialAttributeDao tblMaterialAttributeDao;

    @Override
    public List<TblMaterialAttribute> getMaterialAttributesByMaterialId(String materialId) {
        return tblMaterialAttributeDao.getMaterialAttributesByMaterId(materialId);
    }
}
