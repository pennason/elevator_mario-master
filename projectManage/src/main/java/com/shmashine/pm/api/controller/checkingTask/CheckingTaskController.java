package com.shmashine.pm.api.controller.checkingTask;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.shmashine.pm.api.entity.TblCheckingBill;
import com.shmashine.pm.api.entity.TblCheckingTask;
import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblCheckingTaskDto;
import com.shmashine.pm.api.enums.TblCheckingBillEnum;
import com.shmashine.pm.api.enums.TblCheckingTaskEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskListModule;
import com.shmashine.pm.api.module.genTaskModule.GenerateTaskModule;
import com.shmashine.pm.api.service.checkingBill.TblCheckingBillService;
import com.shmashine.pm.api.service.checkingTask.BizCheckingTaskService;
import com.shmashine.pm.api.service.checkingTask.TblCheckingTaskService;
import com.shmashine.pm.api.service.dept.BizDeptService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.project.TblProjectServiceI;
import com.shmashine.pm.api.service.testingBill.BizTestingBillService;
import com.shmashine.pm.api.service.testingBill.TblTestingBillService;
import com.shmashine.pm.api.service.testingTask.TblTestingTaskService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;

/**
 * 验收接口
 */
@RequestMapping("checkingTask")
@RestController
public class CheckingTaskController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private BizCheckingTaskService bizCheckingTaskService;
    @Autowired
    private TblCheckingTaskService tblCheckingTaskService;
    @Autowired
    private BizTestingBillService bizTestingBillService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private TblProjectServiceI tblProjectServiceI;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblCheckingBillService tblCheckingBillService;
    @Autowired
    private TblTestingTaskService tblTestingTaskService;
    @Autowired
    private TblTestingBillService tblTestingBillService;

    /**
     * 获取任务列表
     *
     * @return
     */
    @PostMapping("/searchCheckingTaskList")
    public Object searchCheckingTaskList(@RequestBody SearchCheckingTaskListModule searchCheckingTaskListModule) {
        searchCheckingTaskListModule.setAdminFlag(bizUserService.isAdminOrPm(getUserId()));
        searchCheckingTaskListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizCheckingTaskService.searchCheckingTaskList(searchCheckingTaskListModule));
    }

    @PostMapping("/getCheckingTaskInfo")
    public Object getCheckingTaskInfo(@RequestBody TblCheckingTask tblCheckingTask) {
        TblCheckingTaskDto tblCheckingTaskDto = bizCheckingTaskService.getTaskDetail(tblCheckingTask.getvCheckingTaskId());
        List<Map> testingBillStatusInfo = bizTestingBillService.getStatusInfo(tblCheckingTaskDto.getvVillageId());
        tblCheckingTaskDto.setTestingBillInfo(testingBillStatusInfo);

        return ResponseResult.successObj(tblCheckingTaskDto);
    }

    @GetMapping("/getCountByStatus")
    public Object getCountByStatus() {
        return tblCheckingTaskService.countByStatus();
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editCheckingTask")
    public Object editCheckingTaskInfo(@RequestBody TblCheckingTask tblCheckingTask) {
        int success = tblCheckingTaskService.update(tblCheckingTask);

        if (success > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertCheckingTask")
    public Object insertCheckingTask(@RequestBody GenerateTaskModule generateTaskModule) {
        TblVillage village = tblVillageServiceI.getById(generateTaskModule.getvVillageId());

        List<String> ids = Arrays.asList(generateTaskModule.getvElevatorIds().split(","));
        List<TblElevator> elevators = tblElevatorService.listByIds(ids);

        if (elevators.stream().allMatch(elevator -> elevator.getiPmStatus() == TblVillageStatusEnum.CheckLess.getValue())) {
            TblCheckingTask tblCheckingTask = new TblCheckingTask();

            String taskId = SnowFlakeUtils.nextStrId();
            tblCheckingTask.setvCheckingTaskId(taskId);
            tblCheckingTask.setvProjectId(village.getVProjectId());
            tblCheckingTask.setvVillageId(village.getVVillageId());
            tblCheckingTask.setiStatus(TblCheckingTaskEnum.Checking.getValue());
            tblCheckingTask.setvAddress(village.getVAddress());
            tblCheckingTask.setiElevatorCount(elevators.size());
            tblCheckingTask.setvPrincipalId(generateTaskModule.getvPrincipalId());

            TblProject project = tblProjectServiceI.getById(village.getVProjectId());
            tblCheckingTask.setdCheckDate(project.getdEndTime());

            tblCheckingTask.setDtCreateTime(new Date());
            tblCheckingTask.setDtModifyTime(new Date());
            tblCheckingTask.setvCreateUserId(getUserId());
            tblCheckingTask.setvModifyUserId(getUserId());

            int result = tblCheckingTaskService.insert(tblCheckingTask);

            if (result > 0) {
                TblTestingTask task = tblTestingTaskService.getById(generateTaskModule.getvTaskId());
                task.setiNextStep(1);
                tblTestingTaskService.update(task);

                List<TblCheckingBill> tblCheckingBills = new ArrayList<>();

                elevators.forEach(ele -> {
                    ele.setiPmStatus(TblVillageStatusEnum.Checking.getValue());
                    tblElevatorService.update(ele);

                    TblTestingBill testingBill = new TblTestingBill();
                    testingBill.setvElevatorCode(ele.getVElevatorCode());
                    testingBill = tblTestingBillService.getByEntity(testingBill);

                    TblCheckingBill bill = new TblCheckingBill();

                    bill.setvCheckingTaskId(taskId);
                    bill.setvCheckingBillId(SnowFlakeUtils.nextStrId());

                    bill.setvProjectId(village.getVProjectId());
                    bill.setvVillageId(village.getVVillageId());
                    bill.setvElevatorCode(ele.getVElevatorCode());
                    bill.setvDeviceType(testingBill.getvDeviceType());
                    bill.setiStatus(TblCheckingBillEnum.Doing.getValue());
                    bill.setDtCreateTime(new Date());
                    bill.setDtModifyTime(new Date());
                    bill.setvCreateUserId(getUserId());
                    bill.setvModifyUserId(getUserId());

                    tblCheckingBills.add(bill);
                });

                tblCheckingBillService.batchInsert(tblCheckingBills);
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有待验收的电梯才能生成验收单"));
        }
    }
}
