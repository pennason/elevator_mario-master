package com.shmashine.api.controller.elevatorbrand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.elevatorbrand.TblElevatorBrandServiceI;
import com.shmashine.common.entity.TblElevatorBrand;

/**
 * 电梯品牌接口
 */
@RestController
@RequestMapping("elevatorBrand")
public class ElevatorBrandController {


    private TblElevatorBrandServiceI tblElevatorBrandServiceI;

    @Autowired
    public ElevatorBrandController(TblElevatorBrandServiceI tblElevatorBrandServiceI) {
        this.tblElevatorBrandServiceI = tblElevatorBrandServiceI;
    }


    /**
     * 获取电梯品牌接口
     *
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblElevatorBrand #
     */
    @GetMapping("/searchElevatorBrandSelectList")
    public Object searchElevatorBrandSelectList() {
        TblElevatorBrand tblElevatorBrand = new TblElevatorBrand();
        tblElevatorBrand.setIDelFlag(0);
        return ResponseResult.successObj(tblElevatorBrandServiceI.getByEntity(tblElevatorBrand));
    }
}
