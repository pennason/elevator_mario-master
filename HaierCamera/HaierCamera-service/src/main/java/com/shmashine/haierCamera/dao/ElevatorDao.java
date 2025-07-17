package com.shmashine.haierCamera.dao;

import com.shmashine.haierCamera.entity.TblElevator;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/21 11:09
 */
public interface ElevatorDao {


    /**
     * 根据电梯注册码获取电梯
     *
     * @return
     */
    TblElevator queryEleByResCode(String registerCode);

}
