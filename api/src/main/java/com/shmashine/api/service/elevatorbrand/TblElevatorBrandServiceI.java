package com.shmashine.api.service.elevatorbrand;

import java.util.List;
import java.util.Map;

import com.shmashine.api.dao.TblElevatorBrandDao;
import com.shmashine.common.entity.TblElevatorBrand;

public interface TblElevatorBrandServiceI {

    TblElevatorBrandDao getTblElevatorBrandDao();

    TblElevatorBrand getById(String vElevatorBrandId);

    List<TblElevatorBrand> getByEntity(TblElevatorBrand tblElevatorBrand);

    List<TblElevatorBrand> listByEntity(TblElevatorBrand tblElevatorBrand);

    List<TblElevatorBrand> listByIds(List<String> ids);

    int insert(TblElevatorBrand tblElevatorBrand);

    int insertBatch(List<TblElevatorBrand> list);

    int update(TblElevatorBrand tblElevatorBrand);

    int updateBatch(List<TblElevatorBrand> list);

    int deleteById(String vElevatorBrandId);

    int deleteByEntity(TblElevatorBrand tblElevatorBrand);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevatorBrand tblElevatorBrand);

    /**
     * 查询所有电梯品牌，批量添梯时校验
     *
     * @return
     */
    List<Map<String, String>> getElevatorBrands();

    /**
     * 根据电梯品牌名 获取品牌信息
     *
     * @param elevatorBrandName 品牌名称
     * @return 结果
     */
    TblElevatorBrand getBrandByBrandName(String elevatorBrandName);
}