package com.shmashine.pm.api.controller.checkingBill;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblCheckingBill;
import com.shmashine.pm.api.entity.TblCheckingTask;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.enums.TblCheckingBillEnum;
import com.shmashine.pm.api.enums.TblCheckingTaskEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.checkingBill.CheckingBillModule;
import com.shmashine.pm.api.service.checkingBill.TblCheckingBillService;
import com.shmashine.pm.api.service.checkingTask.TblCheckingTaskService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.project.TblProjectServiceI;
import com.shmashine.pm.api.service.village.TblVillageServiceI;

/**
 * 验收单
 */
@RequestMapping("checkingBill")
@RestController
public class CheckingBillController extends BaseRequestEntity {

    @Autowired
    private TblCheckingBillService tblCheckingBillService;
    @Autowired
    private TblCheckingTaskService tblCheckingTaskService;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private TblProjectServiceI tblProjectServiceI;

    /**
     * 调测单任务
     *
     * @param module
     * @return
     */
    @PostMapping("/searchList")
    public Object getList(@RequestBody CheckingBillModule module) {
        return ResponseResult.successObj(tblCheckingBillService.selectByBillModule(module));
    }

    /**
     * 编辑验收单子
     *
     * @param tblCheckingBill
     * @return
     */
    @PostMapping("/editCheckingBill")
    @Transactional(rollbackFor = Exception.class)
    public Object editCheckingTaskBill(@RequestBody TblCheckingBill tblCheckingBill) {

        int result = tblCheckingBillService.update(tblCheckingBill);

        if (result > 0) {
            if (tblCheckingBill.getiStatus() == TblCheckingBillEnum.Done.getValue()) {

                tblCheckingBill = tblCheckingBillService.selectByBillId(tblCheckingBill.getvCheckingBillId());

                CheckingBillModule checkingBillModule = new CheckingBillModule();
                checkingBillModule.setvCheckingTaskId(tblCheckingBill.getvCheckingTaskId());

                List<Integer> billStatuses = tblCheckingBillService.getAllStatus(checkingBillModule);

                billStatuses = billStatuses.stream().filter(bs -> bs != TblCheckingBillEnum.Canceled.getValue()).collect(Collectors.toList());

                TblCheckingTask tblCheckingTask = tblCheckingTaskService.getById(tblCheckingBill.getvCheckingTaskId());

                if (billStatuses.size() == tblCheckingTask.getiElevatorCount()) {
                    if (billStatuses.stream().allMatch(st -> st == TblCheckingBillEnum.Done.getValue())) {
                        tblCheckingTask.setiStatus(TblCheckingTaskEnum.Checked.getValue());
                    } else {
                        tblCheckingTask.setiStatus(TblCheckingTaskEnum.Checking.getValue());
                    }
                }

                tblCheckingTaskService.update(tblCheckingTask);

            }
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }


    }

    /**
     * 详细
     *
     * @param vCheckingBillId
     * @return
     */
    @GetMapping("getCheckingBillInfo")
    public Object getCheckingBillInfo(@RequestParam("vCheckingBillId") @NotNull String vCheckingBillId) {
        return ResponseResult.successObj(tblCheckingBillService.selectByBillId(vCheckingBillId));
    }


    /**
     * 取消单子
     *
     * @return
     */
    @PostMapping("/cancelBill")
    @Transactional(rollbackFor = Exception.class)
    public Object cancelBill(@RequestBody TblCheckingBill tblCheckingBill) {

        tblCheckingBill = tblCheckingBillService.selectByBillId(tblCheckingBill.getvCheckingBillId());

        if (tblCheckingBill.getiStatus() == TblCheckingBillEnum.Doing.getValue()) {

            tblCheckingBill.setiStatus(TblCheckingTaskEnum.Canceled.getValue());
            tblCheckingBillService.update(tblCheckingBill);

            TblElevator tblElevator = tblElevatorService.getByElevatorCode(tblCheckingBill.getvElevatorCode());
            tblElevator.setiPmStatus(TblVillageStatusEnum.CheckLess.getValue());
            tblElevatorService.update(tblElevator);

            TblCheckingTask tblCheckingTask = tblCheckingTaskService.getById(tblCheckingBill.getvCheckingTaskId());

            CheckingBillModule checkingBillModule = new CheckingBillModule();
            checkingBillModule.setvCheckingTaskId(tblCheckingBill.getvCheckingTaskId());
            List<Integer> statuses = tblCheckingBillService.getAllStatus(checkingBillModule);

            statuses = statuses.stream().filter(st -> st != TblCheckingBillEnum.Canceled.getValue()).collect(Collectors.toList());

            tblCheckingTask.setiElevatorCount(statuses.size());

            if (statuses.size() == 0) {
                tblCheckingTask.setiStatus(TblCheckingTaskEnum.Canceled.getValue());
            } else {
                if (statuses.stream().allMatch(st -> st == TblCheckingBillEnum.Done.getValue())) {
                    tblCheckingTask.setiStatus(TblCheckingTaskEnum.Checked.getValue());
                }
            }

            tblCheckingTaskService.update(tblCheckingTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有未调测的单子才能取消"));
        }

    }

}
