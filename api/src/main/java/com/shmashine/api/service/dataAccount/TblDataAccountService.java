package com.shmashine.api.service.dataAccount;

import com.shmashine.api.entity.TblDataAccount;

public interface TblDataAccountService {
    TblDataAccount getById(String vDataAccountId);

    TblDataAccount getByEntity(TblDataAccount tblDataAccount);

    int insert(TblDataAccount tblDataAccount);

    int update(TblDataAccount tblDataAccount);

    int deleteById(String vDateAccountId);
}
