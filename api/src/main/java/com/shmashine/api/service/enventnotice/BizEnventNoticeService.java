package com.shmashine.api.service.enventnotice;

import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.enoentnotice.input.SearchEnoentNoticeModule;

public interface BizEnventNoticeService {

    PageListResultEntity<Map> searchEnoentNotice(SearchEnoentNoticeModule searchElevatorProjectListModule);
}
