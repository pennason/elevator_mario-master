package com.shmashine.haierCamera.dao;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/18 17:04
 */
public interface CameraDao {

    /**
     * 获取电梯注册码
     *
     * @param elevatorId
     * @return
     */
    String getRegisterCodeById(String elevatorId);

}
