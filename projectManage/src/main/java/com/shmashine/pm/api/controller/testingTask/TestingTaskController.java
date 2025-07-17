package com.shmashine.pm.api.controller.testingTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblInstallingBill;
import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblTestingTaskDto;
import com.shmashine.pm.api.enums.TblTestingBillStatusEnum;
import com.shmashine.pm.api.enums.TblTestingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.genTaskModule.GenerateTaskModule;
import com.shmashine.pm.api.module.testingTask.input.SearchTestingTaskListModule;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.installingBill.BizInstallingBillService;
import com.shmashine.pm.api.service.installingBill.TblInstallingBillService;
import com.shmashine.pm.api.service.installingTask.TblInstallingTaskService;
import com.shmashine.pm.api.service.testingBill.TblTestingBillService;
import com.shmashine.pm.api.service.testingTask.BizTestingTaskService;
import com.shmashine.pm.api.service.testingTask.TblTestingTaskService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;

;

/**
 * 安装接口
 */
@RequestMapping("testingTask")
@RestController
public class TestingTaskController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private BizTestingTaskService bizTestingTaskService;
    @Autowired
    private TblTestingTaskService tblTestingTaskService;
    @Autowired
    private BizInstallingBillService bizInstallingBillService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblTestingBillService tblTestingBillService;
    @Autowired
    private TblInstallingTaskService tblInstallingTaskService;
    @Autowired
    private TblInstallingBillService tblInstallingBillService;

    /**
     * 获取任务列表
     */
    @PostMapping("/searchTestingTaskList")
    public Object searchTestingTaskList(@RequestBody SearchTestingTaskListModule searchTestingTaskListModule) {
        searchTestingTaskListModule.setAdminFlag(bizUserService.isAdminOrPm(getUserId()));
        searchTestingTaskListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizTestingTaskService.searchTestingTaskList(searchTestingTaskListModule));
    }

    @PostMapping("/getTestingTaskInfo")
    public Object getTestingTaskInfo(@RequestBody TblTestingTask tblTestingTask) {
        TblTestingTaskDto tblTestingTaskDto = bizTestingTaskService.getTaskDetail(tblTestingTask.getvTestingTaskId());

        var billStatusInfo = bizInstallingBillService.getStatusInfo(tblTestingTaskDto.getvVillageId());
        tblTestingTaskDto.setInstallBillInfo(billStatusInfo);

        return ResponseResult.successObj(tblTestingTaskDto);
    }

    @PostMapping("/getElevatorsInfo")
    public Object getElevatorsInfo(@RequestBody TblTestingTask tblTestingTask) {
        return ResponseResult.successObj(bizTestingTaskService.getElevatorsInfo(tblTestingTask.getvTestingTaskId()));
    }

    /**
     * 状态数量
     */
    @GetMapping("/getCountByStatus")
    public Object getCountByStatus() {
        return ResponseResult.successObj(tblTestingTaskService.countByStatus());
    }

    @PostMapping("/insertTestTask")
    @Transactional(rollbackFor = Exception.class)
    public Object addTask(@RequestBody GenerateTaskModule generateTaskModule) {
        TblVillage village = tblVillageServiceI.getById(generateTaskModule.getvVillageId());

        List<String> ids = Arrays.asList(generateTaskModule.getvElevatorIds().split(","));
        List<TblElevator> elevators = tblElevatorService.listByIds(ids);

        if (elevators.stream().allMatch(elevator
                -> elevator.getiPmStatus().equals(TblVillageStatusEnum.TestLess.getValue()))) {
            TblTestingTask tblTestingTask = new TblTestingTask();

            String taskId = SnowFlakeUtils.nextStrId();
            tblTestingTask.setvTestingTaskId(taskId);

            tblTestingTask.setvProjectId(village.getVProjectId());
            tblTestingTask.setvVillageId(village.getVVillageId());
            tblTestingTask.setiStatus(TblTestingTaskStatusEnum.Testing.getValue());
            tblTestingTask.setvAddress(village.getVAddress());
            tblTestingTask.setdTestingDate(village.getdInstallDate());
            tblTestingTask.setiElevatorCount(elevators.size());
            tblTestingTask.setvCreateUserId(getUserId());
            tblTestingTask.setvModifyUserId(getUserId());
            tblTestingTask.setDtCreateTime(new Date());
            tblTestingTask.setDtModifyTime(new Date());
            tblTestingTask.setvPrincipalId(generateTaskModule.getvPrincipalId());
            int result = tblTestingTaskService.insert(tblTestingTask);

            if (result > 0) {

                TblInstallingTask task = tblInstallingTaskService.getById(generateTaskModule.getvTaskId());
                task.setiNextStep(1);

                tblInstallingTaskService.update(task);

                List<TblTestingBill> testingBills = new ArrayList<>();

                elevators.forEach(ele -> {

                    ele.setiPmStatus(TblVillageStatusEnum.Testing.getValue());
                    tblElevatorService.update(ele);

                    TblInstallingBill tblInstallingBill = new TblInstallingBill();
                    tblInstallingBill.setvElevatorCode(ele.getVElevatorCode());
                    tblInstallingBill = tblInstallingBillService.getByEntity(tblInstallingBill);

                    TblTestingBill testingBill = new TblTestingBill();

                    testingBill.setvTestingTaskId(taskId);
                    testingBill.setvTestingBillId(SnowFlakeUtils.nextStrId());
                    testingBill.setvProjectId(village.getVProjectId());
                    testingBill.setvVillageId(village.getVVillageId());
                    testingBill.setiStatus(TblTestingBillStatusEnum.Doing.getValue());
                    testingBill.setvElevatorCode(ele.getVElevatorCode());
                    testingBill.setvDeviceType(tblInstallingBill.getvDeviceType());
                    testingBill.setDtCreateTime(new Date());
                    testingBill.setDtModifyTime(new Date());
                    testingBill.setvCreateUserId(getUserId());
                    testingBill.setvModifyUserId(getUserId());
                    testingBill.setiDelFlag(0);

                    testingBills.add(testingBill);
                });

                tblTestingBillService.insertBatch(testingBills);
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有待调测的电梯才能生成调测单"));
        }
    }
}
