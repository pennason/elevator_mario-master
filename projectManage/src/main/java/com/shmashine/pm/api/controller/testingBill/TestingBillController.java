package com.shmashine.pm.api.controller.testingBill;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.enums.TblTestingBillStatusEnum;
import com.shmashine.pm.api.enums.TblTestingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.testingBill.input.TestingBillModule;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.testingBill.BizTestingBillService;
import com.shmashine.pm.api.service.testingBill.TblTestingBillService;
import com.shmashine.pm.api.service.testingTask.TblTestingTaskService;

/**
 * 调测单接口
 */
@RequestMapping("testingBill")
@RestController
public class TestingBillController extends BaseRequestEntity {

    @Autowired
    private TblTestingBillService tblTestingBillService;
    @Autowired
    private BizTestingBillService bizTestingBillService;
    @Autowired
    private TblTestingTaskService tblTestingTaskService;
    @Autowired
    private TblElevatorService tblElevatorService;


    /**
     * 调测单任务
     */
    @PostMapping("/searchList")
    public Object getList(@RequestBody TestingBillModule module) {
        return ResponseResult.successObj(bizTestingBillService.selectByBillModule(module));
    }

    /**
     * 调测单任务
     */
    @PostMapping("/getTestingBillInfo")
    public Object getTestingBillInfo(@RequestBody TblTestingBill tblTestingBill) {
        return ResponseResult.successObj(bizTestingBillService.getByBizInfo(tblTestingBill));
    }

    @PostMapping("/getInfoAndElevatorInfo")
    public Object getInfoAndElevatorInfo(@RequestBody TblTestingBill tblTestingBill) {
        return ResponseResult.successObj(bizTestingBillService.getInfoAndElevatorInfo(tblTestingBill));
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editTestingBill")
    public Object editTestingBillInfo(@RequestBody TblTestingBill tblTestingBill) {

        tblTestingBill.setvModifyUserId(getUserId());
        tblTestingBill.setDtModifyTime(new Date());

        int success = tblTestingBillService.update(tblTestingBill);

        if (success > 0) {

            TestingBillModule testingBillModule = new TestingBillModule();

            testingBillModule.setVTestingTaskId(tblTestingBill.getvTestingTaskId());
            List<Integer> testingStatuses = bizTestingBillService.getAllStatus(testingBillModule);

            testingStatuses = testingStatuses.stream()
                    .filter(st -> st != TblTestingBillStatusEnum.Canceled.getValue()).toList();

            TblTestingTask tblTestingTask = tblTestingTaskService.getById(tblTestingBill.getvTestingTaskId());

            if (testingStatuses.stream().allMatch(ts -> ts == TblTestingBillStatusEnum.Done.getValue())) {
                tblTestingTask.setiStatus(TblTestingTaskStatusEnum.Tested.getValue());
            } else {
                tblTestingTask.setiStatus(TblTestingTaskStatusEnum.Testing.getValue());
            }

            tblTestingTask.setvModifyUserId(getUserId());
            tblTestingTask.setDtModifyTime(new Date());

            if (tblTestingTask.getvPrincipalId() == null) {
                tblTestingTask.setvPrincipalId(getUserId());
            }

            tblTestingTaskService.update(tblTestingTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("调测单调测失败！请稍后再试"));
        }
    }

    /**
     * 取消单子
     */
    @PostMapping("/cancelBill")
    @Transactional(rollbackFor = Exception.class)
    public Object cancelBill(@RequestBody TblTestingBill tblTestingBill) {

        tblTestingBill = tblTestingBillService.getById(tblTestingBill.getvTestingBillId());

        if (tblTestingBill.getiStatus() == TblTestingTaskStatusEnum.TestingLess.getValue()) {

            tblTestingBill.setiStatus(TblTestingTaskStatusEnum.Canceled.getValue());
            tblTestingBillService.update(tblTestingBill);

            TblElevator tblElevator = tblElevatorService.getByElevatorCode(tblTestingBill.getvElevatorCode());
            tblElevator.setiPmStatus(TblVillageStatusEnum.TestLess.getValue());
            tblElevatorService.update(tblElevator);

            TblTestingTask tblTestingTask = tblTestingTaskService.getById(tblTestingBill.getvTestingTaskId());

            TestingBillModule testingBillModule = new TestingBillModule();
            testingBillModule.setVTestingTaskId(tblTestingBill.getvTestingTaskId());
            List<Integer> statuses = bizTestingBillService.getAllStatus(testingBillModule);

            statuses = statuses.stream().filter(st -> st != TblTestingBillStatusEnum.Canceled.getValue()).toList();

            tblTestingTask.setiElevatorCount(statuses.size());

            if (statuses.isEmpty()) {
                tblTestingTask.setiStatus(TblTestingTaskStatusEnum.Canceled.getValue());
            } else {
                if (statuses.stream().allMatch(st -> st == TblTestingBillStatusEnum.Done.getValue())) {
                    tblTestingTask.setiStatus(TblTestingTaskStatusEnum.Tested.getValue());
                }
            }

            tblTestingTaskService.update(tblTestingTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有未调测的单子才能取消"));
        }
    }

}
