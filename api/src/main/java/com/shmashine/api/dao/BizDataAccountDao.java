package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.dataAccount.SearchDataAccountModule;

public interface BizDataAccountDao {
    List<Map> search(SearchDataAccountModule searchDataAccountModule);
}
