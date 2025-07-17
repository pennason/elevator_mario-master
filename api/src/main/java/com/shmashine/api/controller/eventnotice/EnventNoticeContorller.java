package com.shmashine.api.controller.eventnotice;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.enoentnotice.input.SearchEnoentNoticeModule;
import com.shmashine.api.service.enventnotice.BizEnventNoticeService;
import com.shmashine.api.service.user.BizUserService;

/**
 * 查询事件 通知 需关注电梯
 */
@RestController
@RequestMapping("notice")
public class EnventNoticeContorller extends BaseRequestEntity {

    private BizEnventNoticeService bizEnventNoticeService;
    private BizUserService bizUserService;

    @Autowired
    public EnventNoticeContorller(BizEnventNoticeService bizEnventNoticeService, BizUserService bizUserService) {
        this.bizEnventNoticeService = bizEnventNoticeService;
        this.bizUserService = bizUserService;
    }

    /**
     * 获取电梯品牌接口
     *
     * @param searchEnoentNoticeModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @PostMapping("/searchEnoentNoticeList")
    public Object searchEnoentNoticeList(@RequestBody @Valid SearchEnoentNoticeModule searchEnoentNoticeModule) {
        searchEnoentNoticeModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchEnoentNoticeModule.setUserId(super.getUserId());
        return ResponseResult.successObj(bizEnventNoticeService.searchEnoentNotice(searchEnoentNoticeModule));
    }
}
