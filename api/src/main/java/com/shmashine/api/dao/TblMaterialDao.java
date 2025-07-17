package com.shmashine.api.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblMaterial;

@Service
public interface TblMaterialDao {
    List<TblMaterial> getMaterialList();
}
