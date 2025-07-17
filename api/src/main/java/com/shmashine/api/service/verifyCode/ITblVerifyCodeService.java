package com.shmashine.api.service.verifyCode;

import com.shmashine.api.entity.TblVerifyCode;

public interface ITblVerifyCodeService {
    /**
     * 新增记录
     *
     * @param tblVerifyCode
     * @return
     */
    int insertRecord(TblVerifyCode tblVerifyCode);

    /**
     * 更细记录
     *
     * @param tblVerifyCode
     * @return
     */
    int updateRecord(TblVerifyCode tblVerifyCode);

    /**
     * 查找记录
     *
     * @param mobileNum
     * @return
     */
    TblVerifyCode findRecord(String mobileNum, int type);
}
