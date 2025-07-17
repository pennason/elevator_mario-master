package com.shmashine.api.dao;

import com.shmashine.api.entity.TblDataAccount;

public interface TblDataAccountDao {

    TblDataAccount getById(String vDataAccountId);

    TblDataAccount getByEntity(TblDataAccount tblDataAccount);

    int insert(TblDataAccount tblDataAccount);

    int update(TblDataAccount tblDataAccount);

    int deleteById(String vDataAccountId);
}
