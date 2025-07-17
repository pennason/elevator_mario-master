package com.shmashine.pm.api.controller.installingTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.contants.BusinessConstants;
import com.shmashine.pm.api.entity.TblDevice;
import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.entity.TblInstallingBill;
import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblInstallingTaskDto;
import com.shmashine.pm.api.enums.TblInstallingBillStatusEnum;
import com.shmashine.pm.api.enums.TblInstallingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblTestingBillStatusEnum;
import com.shmashine.pm.api.enums.TblTestingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.genTaskModule.GenerateTaskModule;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskListModule;
import com.shmashine.pm.api.service.distributionBill.TblDistributionBillService;
import com.shmashine.pm.api.service.distributionTask.TblDistributionTaskService;
import com.shmashine.pm.api.service.elevator.TblDeviceService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.installingBill.BizInstallingBillService;
import com.shmashine.pm.api.service.installingBill.TblInstallingBillService;
import com.shmashine.pm.api.service.installingTask.BizInstallingTaskService;
import com.shmashine.pm.api.service.installingTask.TblInstallingTaskService;
import com.shmashine.pm.api.service.investigateBill.TblInvestigateBillService;
import com.shmashine.pm.api.service.testingBill.TblTestingBillService;
import com.shmashine.pm.api.service.testingTask.TblTestingTaskService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;

/**
 * 安装接口
 */
@RequestMapping("installingTask")
@RestController
public class InstallingTaskController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private BizInstallingTaskService bizInstallingTaskService;
    @Autowired
    private TblInstallingTaskService tblInstallingTaskService;
    @Autowired
    private BizInstallingBillService bizInstallingBillService;
    @Autowired
    private TblInstallingBillService tblInstallingBillService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblDistributionBillService tblDistributionBillService;
    @Autowired
    private TblDeviceService tblDeviceService;
    @Autowired
    private TblDistributionTaskService tblDistributionTaskService;
    @Autowired
    private TblInvestigateBillService tblInvestigateBillService;
    @Autowired
    private TblTestingTaskService tblTestingTaskService;
    @Autowired
    private TblTestingBillService tblTestingBillService;


    /**
     * 获取安装单任务列表
     *
     * @return
     */
    @PostMapping("/searchInstallingTaskList")
    public Object searchInstallingTaskList(@RequestBody SearchInstallingTaskListModule searchInstallingTaskListModule) {
        searchInstallingTaskListModule.setAdminFlag(bizUserService.isAdminOrPm(getUserId()));
        searchInstallingTaskListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizInstallingTaskService
                .searchInstallingTaskList(searchInstallingTaskListModule));

    }


    @PostMapping("/getInstallingTaskInfo")
    public Object getInstallingTaskInfo(@RequestBody TblInstallingTask tblInstallingTask) {
        TblInstallingTaskDto tblInstallingTaskDto = bizInstallingTaskService.getTaskDetail(tblInstallingTask.getvInstallingTaskId());
        List<Map> installingBillStatusInfo = bizInstallingBillService.getStatusInfo(tblInstallingTaskDto.getvVillageId());
        tblInstallingTaskDto.setInstallBillInfo(installingBillStatusInfo);

        return ResponseResult.successObj(tblInstallingTaskDto);
    }

    @PostMapping("/getElevatorsInfo")
    public Object getElevatorsInfo(@RequestBody TblInstallingTask tblInstallingTask) {
        return ResponseResult.successObj(bizInstallingTaskService.getElevatorsInfo(tblInstallingTask.getvInstallingTaskId()));
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertInstallingTask")
    public Object insertInstallingTask(@RequestBody GenerateTaskModule generateTaskModule) {
        TblVillage village = tblVillageServiceI.getById(generateTaskModule.getvVillageId());

        List<String> ids = Arrays.asList(generateTaskModule.getvElevatorIds().split(","));
        List<TblElevator> elevators = tblElevatorService.listByIds(ids);

        if (elevators.stream().allMatch(elevator -> elevator.getiPmStatus() == TblVillageStatusEnum.InstallLess.getValue())) {
            TblInstallingTask tblInstallingTask = new TblInstallingTask();

            String taskId = SnowFlakeUtils.nextStrId();
            tblInstallingTask.setvInstallingTaskId(taskId);
            tblInstallingTask.setvProjectId(village.getVProjectId());
            tblInstallingTask.setvVillageId(village.getVVillageId());

            tblInstallingTask.setiStatus(TblInstallingTaskStatusEnum.Installing.getValue());
            tblInstallingTask.setiElevatorCount(elevators.size());
            tblInstallingTask.setDtCreateTime(new Date());
            tblInstallingTask.setDtModifyTime(new Date());
            tblInstallingTask.setvCreateUserId(getUserId());
            tblInstallingTask.setvModifyUserId(getUserId());

            tblInstallingTask.setvPrincipalId(generateTaskModule.getvPrincipalId());

            if (tblInstallingTask.getvPrincipalId() == null) {
                tblInstallingTask.setvPrincipalId(village.getvInstallerId());
            }
            tblInstallingTask.setiDelFlag(0);

            tblInstallingTask.setvAddress(village.getVAddress());
            tblInstallingTask.setdInstallingDate(village.getdInstallDate());

            TblTestingTask tblTestingTask = addTestingTask(village, generateTaskModule, elevators.size());

            tblInstallingTask.setiNextStep(1);
            int result = tblInstallingTaskService.insert(tblInstallingTask);

            if (result > 0) {
                TblDistributionTask task = tblDistributionTaskService.getById(generateTaskModule.getvTaskId());
                task.setiNextStep(1);
                tblDistributionTaskService.update(task);

                List<TblInstallingBill> tblInstallingBills = new ArrayList<>();
                List<TblTestingBill> testingBills = new ArrayList<>();

                elevators.forEach(ele -> {
                    ele.setiPmStatus(TblVillageStatusEnum.Installing.getValue());
                    tblElevatorService.update(ele);

                    TblInstallingBill installingBill = new TblInstallingBill();
                    TblDistributionBill distributionBill = tblDistributionBillService.selectByElevatorId(ele.getVElevatorId());

                    String billId = SnowFlakeUtils.nextStrId();
                    installingBill.setvInstallingBillId(billId);
                    installingBill.setvProjectId(tblInstallingTask.getvProjectId());
                    installingBill.setvVillageId(tblInstallingTask.getvVillageId());
                    installingBill.setvInstallingTaskId(tblInstallingTask.getvInstallingTaskId());
                    installingBill.setvElevatorCode(distributionBill.getvElevatorCode());
                    installingBill.setvVerifyCode(distributionBill.getvVerifyCode());
                    installingBill.setvDeviceType(distributionBill.getvDeviceType());

                    if (distributionBill.getiCubeCount() != null) {
                        if (distributionBill.getiCubeCount() == 1)
                            installingBill.setiSingleBox(1);
                        else
                            installingBill.setiDoubleBox(1);
                    } else {
                        installingBill.setiSingleBox(1);
                    }

                    if (distributionBill.getI4gRouterCount() != null && distributionBill.getI4gRouterCount() > 0)
                        installingBill.setI4gRouter(1);
                    if (distributionBill.getiRouterCount() != null && distributionBill.getiRouterCount() > 0)
                        installingBill.setiRouter(1);
                    if (distributionBill.getvCameraMode() != null && distributionBill.getvCameraMode() != "")
                        installingBill.setiCamera(1);
                    installingBill.setvConnectMode(distributionBill.getvConnectMode());

                    if (distributionBill.getvShortCableCount() != null && distributionBill.getvShortCableCount() != "") {
                        installingBill.setiCable(1);
                    } else {
                        installingBill.setiCable(0);
                    }

                    if (distributionBill.getiPowerSupplyCount() != null && distributionBill.getiPowerSupplyCount() > 0) {
                        installingBill.setiPowerSupply(1);
                    }

                    installingBill.setiBodySensor(distributionBill.getiBodySensor());
                    installingBill.setiCarroofDoorSensor(distributionBill.getiCarroofElectricitySensor());
                    installingBill.setiCarroofLockSensor(distributionBill.getiCarroofLockSensor());
                    installingBill.setiMachRoomServiceSensor(distributionBill.getiRoomElectricitySensor());
                    installingBill.setiMachRoomOverhaulSensor(distributionBill.getiRoomServiceSensor());
                    installingBill.setiMachRoomSwitchSensor(distributionBill.getiRoomSwitchSensor());
                    installingBill.setiMachRoomCallSensor(distributionBill.getiRoomCallSensor());
                    installingBill.setiMachRoomCircuitSensor(distributionBill.getiRoomSafeSensor());
                    installingBill.setiMachRoomHallCircuitSensor(distributionBill.getiRoomHallSensor());
                    installingBill.setiMachRoomSwitchCircuitSensor(distributionBill.getiRoomSwitchCircuitSensor());
                    installingBill.setiMachRoomElectricitySensor(distributionBill.getiRoomCutElectricitySensor());
                    installingBill.setiStatus(TblInstallingBillStatusEnum.Doing.getValue());
                    installingBill.setiPlcMode(distributionBill.getiPlcCable() != null && distributionBill.getiPlcCable() == 1 ? 1 : 0);
                    installingBill.setiNetworkMode(distributionBill.getiNetworkBridge());

                    installingBill.setiTempSensor(distributionBill.getiTempSensor());
                    installingBill.setiMagnetSensor(distributionBill.getiMagnetSensor());
                    installingBill.setI4gModule(distributionBill.getI4gModule());
                    installingBill.setiCollateFloorSensor(distributionBill.getiCollateFloorSensor());
                    installingBill.setiFloorSensor(distributionBill.getiFloorSensor());
                    installingBill.setiPatchBoard(StringUtils.hasText(distributionBill.getiPatchBoradCount()) ? 1 : 0);

                    tblInstallingBills.add(installingBill);

                    testingBills.add(addTestingBill(ele, tblTestingTask, installingBill, village));

                    TblInvestigateBill tblInvestigateBill = tblInvestigateBillService.getByElevatorId(ele.getVElevatorId());

                    if ("camera".equals(installingBill.getvDeviceType())) {
                        // 设备信息 - 迅达单盒
                        TblDevice liftXunDa = new TblDevice();
                        liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());
                        liftXunDa.setVSensorType(BusinessConstants.CAMERA);
                        liftXunDa.seteType(installingBill.getvDeviceType());
                        liftXunDa.setVElevatorId(ele.getVElevatorId());
                        liftXunDa.setVElevatorCode(ele.getVElevatorCode());
                        liftXunDa.setDtCreateTime(new Date());
                        liftXunDa.setvDevicePosition(tblInvestigateBill.getvDevicePosition());
                        tblDeviceService.insertDevice(liftXunDa);

                    } else {

                        if (installingBill.getiSingleBox() != null && installingBill.getiSingleBox() == 1) {
                            // 设备信息 - 迅达单盒
                            TblDevice liftXunDa = new TblDevice();
                            liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());

                            if (installingBill.getvDeviceType() != null
                                    && installingBill.getvDeviceType().contains("MX301")) {
                                liftXunDa.setVSensorType(BusinessConstants.CAR_DOOR);
                            } else {
                                liftXunDa.setVSensorType(BusinessConstants.SENSOR_TYPE_SINGLEBOX);
                            }

                            liftXunDa.seteType(installingBill.getvDeviceType());
                            liftXunDa.setVElevatorId(ele.getVElevatorId());
                            liftXunDa.setVElevatorCode(ele.getVElevatorCode());
                            liftXunDa.setDtCreateTime(new Date());
                            liftXunDa.setvDevicePosition(tblInvestigateBill.getvDevicePosition());
                            tblDeviceService.insertDevice(liftXunDa);
                        }

                        if (installingBill.getiDoubleBox() != null && installingBill.getiDoubleBox() == 1) {
                            // 设备信息 - 轿顶
                            TblDevice carRoof = new TblDevice();
                            carRoof.setVDeviceId(SnowFlakeUtils.nextStrId());
                            carRoof.setVSensorType(BusinessConstants.CAR_ROOF);
                            carRoof.seteType(installingBill.getvDeviceType());
                            carRoof.setVElevatorId(ele.getVElevatorId());
                            carRoof.setvDevicePosition(tblInvestigateBill.getvDevicePosition());
                            carRoof.setVElevatorCode(ele.getVElevatorCode());
                            carRoof.setDtCreateTime(new Date());
                            tblDeviceService.insertDevice(carRoof);

                            // 设备信息 - 机房
                            TblDevice motorRoom = new TblDevice();
                            motorRoom.setVDeviceId(SnowFlakeUtils.nextStrId());
                            motorRoom.setVSensorType(BusinessConstants.MOTOR_ROOM);
                            motorRoom.seteType(installingBill.getvDeviceType());
                            motorRoom.setVElevatorId(ele.getVElevatorId());
                            motorRoom.setvDevicePosition(tblInvestigateBill.getvDevicePosition());
                            motorRoom.setVElevatorCode(ele.getVElevatorCode());
                            motorRoom.setDtCreateTime(new Date());
                            tblDeviceService.insertDevice(motorRoom);
                        }

                    }
                });

                tblInstallingBillService.insertBatch(tblInstallingBills);
                tblTestingBillService.insertBatch(testingBills);
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有待安装的电梯才能生成安装单"));
        }
    }

    /**
     * 获得各个状态数量
     *
     * @return
     */
    @GetMapping("/getCountByStatus")
    public Object getCountByStatus() {
        return ResponseResult.successObj(tblInstallingTaskService.countByStatus());
    }


    /**
     * 递归查询 下级部门的编号
     *
     * @param dept_id
     * @param strings
     */
    private void recursion(String dept_id, List<String> strings) {

        if (null != dept_id) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(dept_id);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }

    /**
     * 生成调测任务
     */
    private TblTestingTask addTestingTask(TblVillage village, GenerateTaskModule generateTaskModule,
                                          int elevatorSize) {
        TblTestingTask tblTestingTask = new TblTestingTask();

        String taskId = SnowFlakeUtils.nextStrId();
        tblTestingTask.setvTestingTaskId(taskId);

        tblTestingTask.setvProjectId(village.getVProjectId());
        tblTestingTask.setvVillageId(village.getVVillageId());
        tblTestingTask.setiStatus(TblTestingTaskStatusEnum.Testing.getValue());
        tblTestingTask.setvAddress(village.getVAddress());
        tblTestingTask.setdTestingDate(village.getdInstallDate());
        tblTestingTask.setiElevatorCount(elevatorSize);
        tblTestingTask.setvCreateUserId(getUserId());
        tblTestingTask.setvModifyUserId(getUserId());
        tblTestingTask.setDtCreateTime(new Date());
        tblTestingTask.setDtModifyTime(new Date());
        tblTestingTask.setvPrincipalId(generateTaskModule.getvPrincipalId());
        int result = tblTestingTaskService.insert(tblTestingTask);

        if (result > 0) {
            return tblTestingTask;
        } else {
            return null;
        }

        //        return result;
    }

    /**
     * @param elevator
     * @param tblTestingTask
     * @param tblInstallingBill
     * @param tblVillage
     * @return
     */
    private TblTestingBill addTestingBill(TblElevator elevator, TblTestingTask tblTestingTask,
                                          TblInstallingBill tblInstallingBill, TblVillage tblVillage) {
        TblTestingBill testingBill = new TblTestingBill();

        testingBill.setvTestingTaskId(tblTestingTask.getvTestingTaskId());
        testingBill.setvTestingBillId(SnowFlakeUtils.nextStrId());
        testingBill.setvProjectId(tblVillage.getVProjectId());
        testingBill.setvVillageId(tblVillage.getVVillageId());
        testingBill.setiStatus(TblTestingBillStatusEnum.Doing.getValue());
        testingBill.setvElevatorCode(elevator.getVElevatorCode());
        testingBill.setvDeviceType(tblInstallingBill.getvDeviceType());
        testingBill.setDtCreateTime(new Date());
        testingBill.setDtModifyTime(new Date());
        testingBill.setvCreateUserId(getUserId());
        testingBill.setvModifyUserId(getUserId());
        testingBill.setiDelFlag(0);

        return testingBill;
    }
}
