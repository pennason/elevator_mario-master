package com.shmashine.api.controller.elevatocollect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevatorCollect.input.OperationElevatorCollectModule;
import com.shmashine.api.module.elevatorCollect.input.SearchElevatorCollectModule;
import com.shmashine.api.service.elevatorcollect.BizElevatorCollectService;
import com.shmashine.api.service.elevatorcollect.TblElevatorCollectServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.entity.TblElevatorCollect;

/**
 * 电梯收藏接口
 */
@RequestMapping("/elevatorCollect")
@RestController
public class ElevatorCollectController extends BaseRequestEntity {

    private BizElevatorCollectService bizElevatorCollectService;
    private TblElevatorCollectServiceI tblElevatorCollectServiceI;
    private final BizUserService bizUserService;

    @Autowired
    public ElevatorCollectController(BizElevatorCollectService bizElevatorCollectService, TblElevatorCollectServiceI tblElevatorCollectServiceI, BizUserService bizUserService) {
        this.bizElevatorCollectService = bizElevatorCollectService;
        this.tblElevatorCollectServiceI = tblElevatorCollectServiceI;
        this.bizUserService = bizUserService;
    }

    /**
     * 查找收藏电梯列表
     *
     * @param searchElevatorCollectModule
     * @return #type:com.shmashine.api.entity.base.PageListResultEntity #
     */
    @PostMapping("/searchElevatorCollectList")
    public Object searchElevatorCollectList(@RequestBody SearchElevatorCollectModule searchElevatorCollectModule) {
        searchElevatorCollectModule.setUserId(super.getUserId());
        searchElevatorCollectModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));

        return bizElevatorCollectService.searchElevatorList(searchElevatorCollectModule);
    }

    /**
     * 查看用户是否收藏电梯
     *
     * @param searchElevatorCollectModule
     * @return #type:com.shmashine.api.service.elevatorcollect.BizElevatorCollectService#searchUserCollectElevatorInfo(com.shmashine.api.module.elevatorCollect.input.OperationElevatorCollectModule) #
     */
    @PostMapping("/searchUserCollectElevatorInfo")
    public Object searchUserCollectElevatorInfo(@RequestBody OperationElevatorCollectModule searchElevatorCollectModule) {
        searchElevatorCollectModule.setUserId(super.getUserId());
        return bizElevatorCollectService.searchUserCollectElevatorInfo(searchElevatorCollectModule);
    }

    /**
     * 收藏电梯
     *
     * @param searchElevatorCollectModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult #
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertUserCollectElevatorInfo")
    public Object insertUserCollectElevatorInfo(@RequestBody OperationElevatorCollectModule searchElevatorCollectModule) {
        TblElevatorCollect tblElevatorCollect = new TblElevatorCollect();
        tblElevatorCollect.setVUserId(super.getUserId());
        tblElevatorCollect.setVElevatorId(searchElevatorCollectModule.getElevatorId());
        int insert = tblElevatorCollectServiceI.insert(tblElevatorCollect);
        if (insert == 0) {
            throw new BizException(ResponseResult.error());
        }
        return ResponseResult.success();
    }

    /**
     * 取消收藏电梯
     *
     * @param searchElevatorCollectModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult #
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/deleteUserCollectElevatorInfo")
    public Object deleteUserCollectElevatorInfo(@RequestBody OperationElevatorCollectModule searchElevatorCollectModule) {
        int delete = tblElevatorCollectServiceI.deleteById(super.getUserId(), searchElevatorCollectModule.getElevatorId());
        if (delete == 0) {
            throw new BizException(ResponseResult.error());
        }
        return ResponseResult.success();
    }


}
