package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevatorBrand;

public interface TblElevatorBrandDao {

    TblElevatorBrand getById(@NotNull String vElevatorBrandId);

    List<TblElevatorBrand> listByEntity(TblElevatorBrand tblElevatorBrand);

    List<TblElevatorBrand> getByEntity(TblElevatorBrand tblElevatorBrand);

    List<TblElevatorBrand> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblElevatorBrand tblElevatorBrand);

    int insertBatch(@NotEmpty List<TblElevatorBrand> list);

    int update(@NotNull TblElevatorBrand tblElevatorBrand);

    int updateByField(@NotNull @Param("where") TblElevatorBrand where, @NotNull @Param("set") TblElevatorBrand set);

    int updateBatch(@NotEmpty List<TblElevatorBrand> list);

    int deleteById(@NotNull String vElevatorBrandId);

    int deleteByEntity(@NotNull TblElevatorBrand tblElevatorBrand);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevatorBrand tblElevatorBrand);

    /**
     * 查询所有电梯品牌列表，添梯时品牌校验
     *
     * @return
     */
    List<Map<String, String>> getElevatorBrands();

    TblElevatorBrand getBrandByName(String elevatorBrandName);
}