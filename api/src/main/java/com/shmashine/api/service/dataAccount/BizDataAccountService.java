package com.shmashine.api.service.dataAccount;

import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.dataAccount.SearchDataAccountModule;

public interface BizDataAccountService {
    PageListResultEntity<Map> search(SearchDataAccountModule searchDataAccountModule);
}
