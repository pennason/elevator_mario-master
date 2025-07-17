package com.shmashine.api.controller.externalpush;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.dao.TblFaultSendShiledDao;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.externalpush.input.SearchExternalPushModule;
import com.shmashine.api.service.externalpush.BizExternalPushService;
import com.shmashine.common.entity.TblFaultSendShiled;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 对外推送管理接口
 */
@RestController
@RequestMapping("externalPush")
public class ExternalPushController extends BaseRequestEntity {

    private BizExternalPushService bizExternalPushService;
    private TblFaultSendShiledDao tblFaultSendShiledDao;

    @Autowired
    public ExternalPushController(BizExternalPushService bizExternalPushService, TblFaultSendShiledDao tblFaultSendShiledDao) {
        this.bizExternalPushService = bizExternalPushService;
        this.tblFaultSendShiledDao = tblFaultSendShiledDao;
    }

    /**
     * 推送列表查询
     *
     * @param searchExternalPushModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @PostMapping("/searchExternalPushList")
    public Object searchExternalPushList(@RequestBody SearchExternalPushModule searchExternalPushModule) {
        return ResponseResult.successObj(bizExternalPushService.searchExternalPushList(searchExternalPushModule));
    }

    /**
     * 添加推送规则
     *
     * @param tblFaultSendShiled
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertElevatorExternalPush")
    public Object insertElevatorExternalPush(@RequestBody TblFaultSendShiled tblFaultSendShiled) {
        tblFaultSendShiled.setVFaultSendShiledId(SnowFlakeUtils.nextStrId());

        int insert = tblFaultSendShiledDao.insert(tblFaultSendShiled);
        if (insert == 0) {
            throw new BizException(ResponseResult.error());
        }
        return ResponseResult.success();
    }

    /**
     * 查看推送规则
     *
     * @param faultSendSailedId ID
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblFaultSendShiled#
     */
    @PostMapping("/getElevatorExternalPushInfo")
    public Object getElevatorExternalPushInfo(@RequestBody @Valid @NotNull(message = "请输入Id") String faultSendSailedId) {
        return ResponseResult.successObj(tblFaultSendShiledDao.getById(faultSendSailedId));
    }

    /**
     * 修改推送规则
     *
     * @param tblFaultSendShiled
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editElevatorExternalPushInfo")
    public Object editElevatorExternalPushInfo(@RequestBody TblFaultSendShiled tblFaultSendShiled) {
        int update = tblFaultSendShiledDao.update(tblFaultSendShiled);
        if (update == 0) {
            throw new BizException(ResponseResult.error());
        }
        return ResponseResult.success();
    }

    /**
     * 删除推送规则
     *
     * @param faultSendSailedId
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/deleteElevatorExternalPushInfo")
    public Object deleteElevatorExternalPushInfo(@RequestBody @Valid @NotNull(message = "请输入Id") String faultSendSailedId) {
        int deleteById = tblFaultSendShiledDao.deleteById(faultSendSailedId);
        if (deleteById == 0) {
            throw new BizException(ResponseResult.error());
        }
        return ResponseResult.success();
    }
}
