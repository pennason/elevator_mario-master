package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.externalpush.input.SearchExternalPushModule;

public interface BizExternalPushDao {

    List<Map> searchExternalPushList(SearchExternalPushModule searchExternalPushModule);
}
