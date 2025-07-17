package com.shmashine.api.dao;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.TblVerifyCode;

public interface TblVerifyCodeDao {

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
    TblVerifyCode findRecord(@Param("mobileNum") String mobileNum, @Param("type") int type);
}
