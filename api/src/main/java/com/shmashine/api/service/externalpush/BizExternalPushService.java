package com.shmashine.api.service.externalpush;

import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.externalpush.input.SearchExternalPushModule;

public interface BizExternalPushService {

    /**
     * 对外推送管理
     */
    PageListResultEntity<Map> searchExternalPushList(SearchExternalPushModule searchExternalPushModule);
}
