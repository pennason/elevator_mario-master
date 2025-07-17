package com.shmashine.pm.api.controller.distributionBill;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.enums.TblDistributionBillStatusEnum;
import com.shmashine.pm.api.enums.TblDistributionTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.distributionBill.DistributionBillModule;
import com.shmashine.pm.api.service.distributionBill.TblDistributionBillService;
import com.shmashine.pm.api.service.distributionTask.BizDistributionTaskService;
import com.shmashine.pm.api.service.distributionTask.TblDistributionTaskService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;

/**
 * 配货单
 */
@RequestMapping("distributionBill")
@RestController
public class DistributionBillController extends BaseRequestEntity {

    @Autowired
    private TblDistributionBillService tblDistributionBillService;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblDistributionTaskService tblDistributionTaskService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private BizDistributionTaskService bizDistributionTaskService;

    /**
     * 列表
     *
     * @param module
     * @return
     */
    @PostMapping("/searchList")
    public Object getList(@RequestBody DistributionBillModule module) {
        return ResponseResult.successObj(tblDistributionBillService.selectByBillModule(module));
    }

    /**
     * 添加记录
     *
     * @param tblDistributionBill
     * @return
     */
    @PostMapping("/insertRecord")
    public Object addRecord(@RequestBody TblDistributionBill tblDistributionBill) {
        return ResponseResult.successObj(tblDistributionBillService.insert(tblDistributionBill));
    }


    /**
     * 获取传感器配置子选项
     *
     * @param elevatorId 电梯id
     * @return 1:烟杆 2:小平层 3:U型光电 4:门磁
     */
    @GetMapping("/getFloorSensorRemark/{elevatorId}")
    public ResponseResult getDeviceParamConf(@PathVariable("elevatorId") String elevatorId) {
        return ResponseResult.successObj(tblDistributionBillService.getFloorSensorRemark(elevatorId));
    }

    /**
     * 配货单编辑
     *
     * @param tblDistributionBill 配货单信息
     * @return 处理结果
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updateRecord")
    public Object updateRecord(@RequestBody @Valid TblDistributionBill tblDistributionBill) {

        TblDistributionTask tblDistributionTask = tblDistributionTaskService.getById(tblDistributionBill.getvDistributionTaskId());

        if (tblDistributionTask.getiStatus() == TblDistributionTaskStatusEnum.Completed.getValue()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01");
        }

        if (StringUtils.hasText(tblDistributionBill.getvElevatorCode())) {
            Integer existCode = bizDistributionTaskService.existsElevatorCode(Arrays.asList(new String[]{tblDistributionBill.getvElevatorCode()}));

            if (existCode != null && existCode == 1) {
                throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "麦信电梯ID已经存在"));
            }

            DistributionBillModule module = new DistributionBillModule();
            module.setvElevatorCode(tblDistributionBill.getvElevatorCode());
            module.setiStatus(TblDistributionBillStatusEnum.Done.getValue());
            List<Map> rs = tblDistributionBillService.selectByBillModule(module);

            if (rs != null && rs.size() > 0) {
                throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "麦信电梯ID已经存在"));
            }

            tblDistributionBill.setiStatus(TblDistributionBillStatusEnum.Done.getValue());
        }

        int result = tblDistributionBillService.update(tblDistributionBill);

        if (result > 0) {

            DistributionBillModule module = new DistributionBillModule();
            module.setvDistributionTaskId(tblDistributionBill.getvDistributionTaskId());

            List<Map> list = tblDistributionBillService.selectByBillModule(module);

            list = list.stream().filter(item -> (int) item.get("i_status") != TblDistributionBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

            if (list.stream().allMatch(item -> (int) item.get("i_status") == TblDistributionBillStatusEnum.Done.getValue())) {

                tblDistributionTask.setvDistributionTaskId(tblDistributionBill.getvDistributionTaskId());
                tblDistributionTask.setiStatus(TblDistributionTaskStatusEnum.Completed.getValue());
                tblDistributionTask.setvCreateUserId(getUserId());
                tblDistributionTask.setDtModifyTime(new Date());

                int res = tblDistributionTaskService.update(tblDistributionTask);

                if (res > 0) {
                    DistributionBillModule distributionBillModule = new DistributionBillModule();
                    distributionBillModule.setvVillageId(tblDistributionBill.getvVillageId());

                    List<Integer> billStatuses = tblDistributionBillService.getAllStatus(distributionBillModule);

                    billStatuses = billStatuses.stream().filter(st -> st != TblDistributionBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());
                    TblVillage village = tblVillageServiceI.getById(tblDistributionBill.getvVillageId());

                    if (billStatuses.size() == village.getiElevatorCount()) {
                        if (billStatuses.stream().allMatch(bt -> bt == TblDistributionBillStatusEnum.Done.getValue())) {
                            village.setiStatus(TblVillageStatusEnum.InstallLess.getValue());
                            tblVillageServiceI.update(village);
                        }
                    }

                }
            }
        }

        return ResponseResult.success();
    }

    /**
     * 得到相关消息
     *
     * @return
     */
    @GetMapping("/getRelatedInfo")
    public Object getRelatedInfo(@RequestParam("vDistributionBillId") @NotNull String vDistributionBillId) {
        return ResponseResult.successObj(tblDistributionBillService.getRelatedInfo(vDistributionBillId));
    }

    /**
     * 发货单子详情
     *
     * @param vDistributionBillId
     * @return
     */
    @GetMapping("/getDetail")
    public Object getDetail(@RequestParam("vDistributionBillId") @NotNull String vDistributionBillId) {
        return ResponseResult.successObj(tblDistributionBillService.selectByBillId(vDistributionBillId));
    }

    /**
     * 取消单子
     *
     * @return
     */
    @PostMapping("/cancelBill")
    @Transactional(rollbackFor = Exception.class)
    public Object cancelBill(@RequestBody TblDistributionBill tblDistributionBill) {

        tblDistributionBill = tblDistributionBillService.selectByBillId(tblDistributionBill.getvDistributionBillId());

        if (tblDistributionBill.getiStatus() == TblDistributionBillStatusEnum.Doing.getValue()) {

            tblDistributionBill.setiStatus(TblDistributionBillStatusEnum.Canceled.getValue());
            tblDistributionBillService.update(tblDistributionBill);

            TblElevator tblElevator = tblElevatorService.getById(tblDistributionBill.getvElevatorId());
            tblElevator.setiPmStatus(TblVillageStatusEnum.DistributeLess.getValue());
            tblElevatorService.update(tblElevator);

            TblDistributionTask tblDistributionTask = tblDistributionTaskService.getById(tblDistributionBill.getvDistributionTaskId());

            DistributionBillModule distributionBillModule = new DistributionBillModule();
            distributionBillModule.setvDistributionTaskId(tblDistributionBill.getvDistributionTaskId());

            List<Integer> statuses = tblDistributionBillService.getAllStatus(distributionBillModule);

            statuses = statuses.stream().filter(st -> st != TblDistributionBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

            tblDistributionTask.setiElevatorCount(statuses.size());

            if (statuses.size() == 0) {
                tblDistributionTask.setiStatus(TblDistributionTaskStatusEnum.Canceled.getValue());
            } else {
                if (statuses.stream().allMatch(st -> st == TblDistributionBillStatusEnum.Done.getValue())) {
                    tblDistributionTask.setiStatus(TblDistributionTaskStatusEnum.Completed.getValue());
                }
            }
            tblDistributionTask.setDtModifyTime(new Date());
            tblDistributionTaskService.update(tblDistributionTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有未配货的单子才能取消"));
        }


    }
}
