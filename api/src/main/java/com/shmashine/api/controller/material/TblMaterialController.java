package com.shmashine.api.controller.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.dao.TblMaterialDao;
import com.shmashine.api.entity.base.ResponseResult;

@RestController
@Controller
@RequestMapping("/material")
public class TblMaterialController {

    @Autowired
    private TblMaterialDao tblMaterialDao;

    /**
     *
     */
    @GetMapping("/allMaterials")
    public Object list() {
        return ResponseResult.successObj(tblMaterialDao.getMaterialList());
    }
}
