package com.shmashine.pm.api.controller.distributionTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.entity.TblVillageDeviceBill;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.contants.BusinessConstants;
import com.shmashine.pm.api.entity.TblDevice;
import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.entity.TblInstallingBill;
import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.entity.TblPmFile;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.enums.TblDistributionBillStatusEnum;
import com.shmashine.pm.api.enums.TblDistributionTaskStatusEnum;
import com.shmashine.pm.api.enums.TblInstallingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.distributionBill.DistributionBillModule;
import com.shmashine.pm.api.module.distributionTask.input.SearchDistributionTaskListModule;
import com.shmashine.pm.api.module.genTaskModule.GenerateTaskModule;
import com.shmashine.pm.api.service.dept.BizDeptService;
import com.shmashine.pm.api.service.distributionBill.TblDistributionBillService;
import com.shmashine.pm.api.service.distributionTask.BizDistributionTaskService;
import com.shmashine.pm.api.service.distributionTask.TblDistributionTaskService;
import com.shmashine.pm.api.service.elevator.TblDeviceService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.installingBill.TblInstallingBillService;
import com.shmashine.pm.api.service.installingTask.TblInstallingTaskService;
import com.shmashine.pm.api.service.investigateTask.TblInvestigateTaskService;
import com.shmashine.pm.api.service.pmFile.TblPmFileService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;
import com.shmashine.pm.api.service.villageDeviceBill.TblVillageDeviceBillService;
import com.shmashine.pm.api.util.ExcelUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 分配任务接口
 */
@RequestMapping("distributionTask")
@RestController
@Slf4j
public class DistributionTaskController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private BizDistributionTaskService bizDistributionTaskService;
    @Autowired
    private TblDistributionTaskService tblDistributionTaskService;
    @Autowired
    private TblPmFileService tblPmFileService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private TblInstallingTaskService tblInstallingTaskService;
    @Autowired
    private TblInstallingBillService tblInstallingBillService;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblVillageDeviceBillService tblVillageDeviceBillService;
    @Autowired
    private TblDeviceService tblDeviceService;
    @Autowired
    private TblDistributionBillService tblDistributionBillService;
    @Autowired
    private TblInvestigateTaskService tblInvestigateTaskService;


    /**
     * 获取配货单任务列表
     */
    @PostMapping("/searchDistributionTaskList")
    public ResponseResult searchDistributionTaskList(@RequestBody SearchDistributionTaskListModule
                                                             searchDistributionTaskListModule) {

        searchDistributionTaskListModule.setAdminFlag(bizUserService.isAdminOrPm(getUserId()));
        searchDistributionTaskListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizDistributionTaskService
                .searchDistributionTaskList(searchDistributionTaskListModule));
    }

    /**
     * 查看
     *
     * @return
     */
    @PostMapping("/getDistributionTaskInfo")
    public Object getTaskInfo(@RequestBody TblDistributionTask tblDistributionTask) {
        return ResponseResult.successObj(bizDistributionTaskService.getBizInfoById(tblDistributionTask.getvDistributionTaskId()));
    }

    /**
     * 状态各个数量
     *
     * @return
     */
    @GetMapping("/getCountByStatus")
    public Object getCountByStatus() {
        return ResponseResult.successObj(tblDistributionTaskService.countByStatus());
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertDistributionTaskInfo")
    public Object addTask(@RequestBody GenerateTaskModule generateTaskModule) {

        TblVillage village = tblVillageServiceI.getById(generateTaskModule.getvVillageId());

        List<String> ids = Arrays.asList(generateTaskModule.getvElevatorIds().split(","));
        List<TblElevator> elevators = tblElevatorService.listByIds(ids);

        if (elevators.stream().allMatch(elevator -> elevator.getiPmStatus() == TblVillageStatusEnum.DistributeLess.getValue())) {

            TblDistributionTask tblDistributionTask = new TblDistributionTask();

            String taskId = SnowFlakeUtils.nextStrId();
            tblDistributionTask.setvDistributionTaskId(taskId);
            tblDistributionTask.setvProjectId(village.getVProjectId());
            tblDistributionTask.setvVillageId(village.getVVillageId());
            tblDistributionTask.setiElevatorCount(elevators.size());
            tblDistributionTask.setvAddress(village.getVAddress());
            tblDistributionTask.setvCreateUserId(getUserId());
            tblDistributionTask.setDtCreateTime(new Date());
            tblDistributionTask.setDtModifyTime(new Date());
            tblDistributionTask.setiStatus(TblDistributionTaskStatusEnum.Uploaded.getValue());
            tblDistributionTask.setvPrincipalId(generateTaskModule.getvPrincipalId());
            tblDistributionTask.setiDelFlag(0);

            int result = tblDistributionTaskService.insert(tblDistributionTask);

            if (result > 0) {

                TblInvestigateTask task = tblInvestigateTaskService.getById(generateTaskModule.getvTaskId());
                task.setiNextStep(1);
                tblInvestigateTaskService.update(task);

                elevators.forEach(ele -> {
                    ele.setiPmStatus(TblVillageStatusEnum.Distributeing.getValue());
                    tblElevatorService.update(ele);

                    TblVillageDeviceBill tblVillageDeviceBill = tblVillageDeviceBillService.getByVillageId(village.getVVillageId());

                    TblDistributionBill tblDistributionBill = new TblDistributionBill();
                    tblDistributionBill.setvDistributionTaskId(taskId);
                    tblDistributionBill.setvProjectId(village.getVProjectId());
                    tblDistributionBill.setvVillageId(village.getVVillageId());
                    tblDistributionBill.setvDistributionBillId(SnowFlakeUtils.nextStrId());
                    tblDistributionBill.setvElevatorId(ele.getVElevatorId());
                    tblDistributionBill.setiStatus(TblDistributionBillStatusEnum.Doing.getValue());
                    tblDistributionBill.setvDeviceType(tblVillageDeviceBill.getVDeviceType());
                    tblDistributionBill.setvCreateUserId(getUserId());
                    tblDistributionBill.setDtCreateTime(new Date());
                    tblDistributionBill.setDtModifyTime(new Date());

                    tblDistributionBill.setiCamera(tblVillageDeviceBill.getICamera());
                    tblDistributionBill.setiBodySensor(tblVillageDeviceBill.getIBodySensor());
                    tblDistributionBill.setiCarroofElectricitySensor(tblVillageDeviceBill.getICarroofDoorSensor());
                    tblDistributionBill.setiCarroofLockSensor(tblVillageDeviceBill.getICarroofLockSensor());
                    tblDistributionBill.setiRoomElectricitySensor(tblVillageDeviceBill.getIMachRoomServiceSensor());
                    tblDistributionBill.setiRoomServiceSensor(tblVillageDeviceBill.getIMachRoomOverhaulSensor());
                    tblDistributionBill.setiRoomSwitchSensor(tblVillageDeviceBill.getIMachRoomSwitchSensor());
                    tblDistributionBill.setiRoomCallSensor(tblVillageDeviceBill.getIMachRoomCallSensor());
                    tblDistributionBill.setiRoomSafeSensor(tblVillageDeviceBill.getIMachRoomCircuitSensor());
                    tblDistributionBill.setiRoomHallSensor(tblVillageDeviceBill.getIMachRoomHallCircuitSensor());
                    tblDistributionBill.setiRoomSwitchCircuitSensor(tblVillageDeviceBill.getIMachRoomSwitchCircuitSensor());
                    tblDistributionBill.setiRoomCutElectricitySensor(tblVillageDeviceBill.getIMachRoomSwitchCircuitSensor());

                    tblDistributionBill.setiTempSensor(tblVillageDeviceBill.getITempSensor());
                    tblDistributionBill.setiFloorSensor(tblVillageDeviceBill.getIFloorSensor());
                    tblDistributionBill.setiMagnetSensor(tblVillageDeviceBill.getIMagnetSensor());
                    tblDistributionBill.setiCollateFloorSensor(tblVillageDeviceBill.getICollateFloorSensor());

                    tblDistributionBill.setvCameraMode(tblVillageDeviceBill.getVCameraMode());
                    tblDistributionBill.setCameraType(tblVillageDeviceBill.getICameraType());
                    if (tblVillageDeviceBill.getISingleBoxCount() == 1) {
                        tblDistributionBill.setiCubeCount(1);
                    }

                    if (tblVillageDeviceBill.getIDoubleBoxCount() == 1) {
                        tblDistributionBill.setiCubeCount(2);
                    }
                    tblDistributionBillService.insert(tblDistributionBill);
                });
            }
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有待配货的电梯才能生成配货单"));
        }
    }

    /**
     * 编辑任务
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editDistributionTaskInfo")
    public Object editTaskInfo(@RequestBody TblDistributionTask tblDistributionTask) {
        int success = tblDistributionTaskService.update(tblDistributionTask);
        if (success > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    @GetMapping("/distributionTaskStatusMap")
    public Object getDistributionTaskStatus() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TblDistributionTaskStatusEnum em : TblDistributionTaskStatusEnum.values()) {
            map.put(em.getValue(), em.getName());
        }
        return ResponseResult.successObj(map);
    }

    @PostMapping("/batchUpdateBill")
    @Transactional(rollbackFor = Exception.class)
    public Object batchUpdateBill(@RequestBody TblDistributionTask tblDistributionTask) {

        if (tblDistributionTask.getvInitElevatorCode() == null || tblDistributionTask.getvInitElevatorCode() == "")
            throw new BizException(ResponseResult.error("请输入初始麦信编码"));
        if (!tblDistributionTask.getvInitElevatorCode().matches("^(MX|VT)\\d+"))
            throw new BizException(ResponseResult.error("初始麦信编码格式不正确"));

        var consistEntity = tblDistributionTaskService.getById(tblDistributionTask.getvDistributionTaskId());
        if (consistEntity.getiStatus() == TblDistributionTaskStatusEnum.Completed.getValue())
            throw new BizException(ResponseResult.error("该任务已经完成"));
        if (consistEntity.getiShortcut() == 1)
            throw new BizException(ResponseResult.error("该任务已经一键生成了"));

        Integer existCode = bizDistributionTaskService
                .existsElevatorCode(Arrays.asList(new String[]{tblDistributionTask.getvInitElevatorCode()}));
        if (existCode != null && existCode == 1) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "麦信电梯ID已经存在"));
        }

        DistributionBillModule distributionBillModule = new DistributionBillModule();
        distributionBillModule.setvElevatorCode(tblDistributionTask.getvInitElevatorCode());
        distributionBillModule.setiStatus(TblDistributionBillStatusEnum.Done.getValue());
        List<Map> rs = tblDistributionBillService.selectByBillModule(distributionBillModule);

        if (rs != null && rs.size() > 0)
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "麦信电梯ID已经存在"));

        DistributionBillModule module = new DistributionBillModule();
        module.setvDistributionTaskId(tblDistributionTask.getvDistributionTaskId());
        List<Map> list = tblDistributionBillService.selectByBillModule(module);
        if (list.stream().anyMatch(item -> (int) item.get("i_status") == TblDistributionBillStatusEnum.Done.getValue()))
            throw new BizException(ResponseResult.error("该任务已经有手动完成的配货单，不能一键生成"));

        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(tblDistributionTask.getvInitElevatorCode());
        String formatString = m.replaceAll("").trim();
        Integer codeNum = Integer.valueOf(formatString);

        for (int i = 0; i < list.size(); i++) {
            if ((Integer) list.get(i).get("i_status") == TblDistributionBillStatusEnum.Done.getValue())
                continue;

            TblDistributionBill tblDistributionBill = new TblDistributionBill();

            String billId = (String) list.get(i).get("v_distribution_bill_id");
            tblDistributionBill.setvDistributionBillId(billId);
            if (tblDistributionTask.getiCubeCount() == 1 || tblDistributionTask.getiCubeCount() == 2)

                tblDistributionBill.setiCubeCount(tblDistributionTask.getiCubeCount());
            tblDistributionBill.setvDistributerName(tblDistributionTask.getvDistributerName());

            if (formatString.startsWith("0")) {
                tblDistributionBill.setvElevatorCode(tblDistributionTask.getvInitElevatorCode().substring(0, 2)
                        + String.format("%0" + formatString.length() + "d", (codeNum + i)));
            } else {
                tblDistributionBill.setvElevatorCode(tblDistributionTask.getvInitElevatorCode().substring(0, 2)
                        + (codeNum + i));
            }

            tblDistributionBillService.update(tblDistributionBill);
        }

        tblDistributionTask.setiShortcut(1);
        tblDistributionTaskService.update(tblDistributionTask);

        return ResponseResult.success();
    }

    @PostMapping("/batchFinishBill")
    @Transactional(rollbackFor = Exception.class)
    public Object batchFinishBill(@RequestBody TblDistributionTask tblDistributionTask) {
        TblDistributionTask task = tblDistributionTaskService.getById(tblDistributionTask.getvDistributionTaskId());

        if (task.getiShortcut() == null || task.getiShortcut() != 1) {
            throw new BizException(ResponseResult.error("任务必须为一键配货后任务"));
        }

        int res = tblDistributionBillService.updateByTaskId(task.getvDistributionTaskId());

        if (res > 0) {
            task.setiStatus(TblDistributionTaskStatusEnum.Completed.getValue());
            tblDistributionTaskService.update(task);
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }
    }

    /**
     * 上传文件
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("vDistributionTaskId") String vDistributionTaskId) {
        if (file == null) {
            throw new BizException(ResponseResult.error());
        }
        String currentUserId = getUserId();
        List<List<String>> data = ExcelUtil.readXlsxData(file, 2);

        TblDistributionTask tblDistributionTask = tblDistributionTaskService.getById(vDistributionTaskId);
        TblVillage village = tblVillageServiceI.getById(tblDistributionTask.getvVillageId());

        if (!village.getVVillageName().equals(data.get(0).get(2))) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg10_03");
        }

        if (tblDistributionTask.getiStatus() == TblDistributionTaskStatusEnum.Completed.getValue()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01");
        }

        data = data.stream().filter(item -> (item.get(7) != "" && item.get(7) != null)).collect(Collectors.toList());

        String fileUrl = OSSUtil.savePmTaskFile(file);
        TblPmFile taskFile = new TblPmFile();
        taskFile.setvTargetId(tblDistributionTask.getvDistributionTaskId());
        taskFile = tblPmFileService.getByEntity(taskFile);

        if (taskFile == null) {
            taskFile = new TblPmFile();
            String fileId = SnowFlakeUtils.nextStrId();
            taskFile.setvPmFileId(fileId);
            taskFile.setvFileUrl(fileUrl);
            taskFile.setvTargetId(tblDistributionTask.getvDistributionTaskId());
            taskFile.setvCreateUserId(currentUserId);
            taskFile.setvModifyUserId(currentUserId);
            tblPmFileService.insert(taskFile);
        } else {
            taskFile.setvFileUrl(fileUrl);
            taskFile.setvModifyUserId(currentUserId);
            tblPmFileService.update(taskFile);
        }

        if (tblDistributionTask.getiElevatorCount() > data.size()) {
            tblDistributionTask.setiStatus(TblDistributionTaskStatusEnum.Uploaded.getValue());
        } else {
            tblDistributionTask.setiStatus(TblDistributionTaskStatusEnum.Completed.getValue());
        }
        tblDistributionTask.setDtModifyTime(new Date());
        tblDistributionTask.setvModifyUserId(currentUserId);
        int success = tblDistributionTaskService.update(tblDistributionTask);

        if (success > 0) {
            if (tblDistributionTask.getiStatus() == TblDistributionTaskStatusEnum.Completed.getValue()) {
                village.setVVillageId(tblDistributionTask.getvVillageId());
                village.setiStatus(TblVillageStatusEnum.InstallLess.getValue());
                village.setDtModifyTime(new Date());
                tblVillageServiceI.update(village);
                //新建安装任务
                TblInstallingTask tblInstallingTask = new TblInstallingTask();
                String taskId = SnowFlakeUtils.nextStrId();
                tblInstallingTask.setvInstallingTaskId(taskId);
                tblInstallingTask.setvProjectId(village.getVProjectId());
                tblInstallingTask.setvVillageId(village.getVVillageId());
                tblInstallingTask.setvPrincipalId(village.getvInstallerId());
                tblInstallingTask.setiElevatorCount(village.getiElevatorCount());
                tblInstallingTask.setiStatus(TblInstallingTaskStatusEnum.InstallLess.getValue());
                tblInstallingTask.setdInstallingDate(village.getdInstallDate());
                tblInstallingTask.setvAddress(village.getVAddress());
                tblInstallingTaskService.insert(tblInstallingTask);

                //新建安装单
                List<TblInstallingBill> bills = generateInstallingBills(data, tblInstallingTask);
                tblInstallingBillService.insertBatch(bills);
            }
        }

        return ResponseResult.successObj(tblDistributionTask);
    }

    @PostMapping("/downloadTemplate")
    public Object downLoadTemplate(@RequestBody TblDistributionTask tblDistributionTask) {
        TblPmFile pmFile = new TblPmFile();
        pmFile.setvTargetId(tblDistributionTask.getvDistributionTaskId());
        pmFile = tblPmFileService.getByEntity(pmFile);

        if (pmFile != null && pmFile.getvFileUrl() != null && !(pmFile.getvFileUrl()).equals("")) {
            log.info("pmFile => " + pmFile.toString());
            return ResponseResult.successObj(pmFile.getvFileUrl());
        } else {
            var relativeInfo = bizDistributionTaskService
                    .getRelativeInfo(tblDistributionTask.getvDistributionTaskId());

            List<List> dataList = new ArrayList<>();
            for (int i = 0; i < relativeInfo.size(); i++) {
                HashMap info = relativeInfo.get(i);
                Integer serialnum = i + 1;
                List<String> attrs = new ArrayList<>();
                attrs.add(info.get("deptName").toString());
                attrs.add(info.get("projectName").toString());
                attrs.add(info.get("villageName").toString());
                attrs.add(serialnum.toString());
                attrs.add(info.get("equipmentCode").toString());
                attrs.add(info.get("address").toString());
                dataList.add(attrs);
            }

            String fileUrl = ExcelUtil.createExcel(dataList, "distributionTask",
                    tblDistributionTask.getvDistributionTaskId());
            log.info("fileUrl => " + fileUrl);
            tblDistributionTask.setvDistributionTaskId(tblDistributionTask.getvDistributionTaskId());
            tblDistributionTask.setvTemplateFileUrl(fileUrl);
            int success = tblDistributionTaskService.update(tblDistributionTask);

            if (success > 0) {
                return ResponseResult.successObj(fileUrl);
            } else {
                throw new BizException(ResponseResult.error());
            }
        }
    }

    /**
     * 递归查询 下级部门的编号
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

    private List<TblInstallingBill> generateInstallingBills(List<List<String>> data, TblInstallingTask installingTask) {
        List<TblInstallingBill> billList = new ArrayList<TblInstallingBill>();
        String compareVariable = "有";
        List<TblElevator> elevatorList = new ArrayList<>();
        List<String> elevatorCodes = data.stream().map(item -> item.get(8)).collect(Collectors.toList());

        if (elevatorCodes.stream().distinct().count() != elevatorCodes.size()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_05"));
        }

        Integer existCode = bizDistributionTaskService.existsElevatorCode(elevatorCodes);
        if (existCode != null && existCode == 1) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_07"));
        }

        TblVillageDeviceBill tblVillageDeviceBill = tblVillageDeviceBillService.getByVillageId(installingTask.getvVillageId());
        List<Integer> boxCount = data.stream().map(item -> Integer.valueOf(item.get(9).split("\\.")[0])).collect(Collectors.toList());
        Integer count = boxCount.stream().reduce(0, Integer::sum);

        if (count != (tblVillageDeviceBill.getISingleBoxCount() + tblVillageDeviceBill.getIDoubleBoxCount())) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_06"));
        }

        for (List<String> item : data) {
            TblElevator elevator = new TblElevator();
            elevator.setVEquipmentCode(item.get(4));
            elevator.setVElevatorCode(item.get(8));
            elevatorList.add(elevator);

            TblInstallingBill installingBill = new TblInstallingBill();

            String billId = SnowFlakeUtils.nextStrId();
            installingBill.setvInstallingBillId(billId);
            installingBill.setvProjectId(installingTask.getvProjectId());
            installingBill.setvVillageId(installingTask.getvVillageId());
            installingBill.setvInstallingTaskId(installingTask.getvInstallingTaskId());
            installingBill.setvElevatorCode(item.get(8));

            //            if(item.get(7) != null && item.get(7) != "" && item.get(7).contains(".")) {
            //                String[] codes = item.get(7).split("\\.");
            //                installingBill.setvVerifyCode(codes[0]);
            //            } else {
            //                installingBill.setvVerifyCode(item.get(7));
            //            }

            String boxType = item.get(9);
            if (boxType.equals("1.0")) {
                installingBill.setiSingleBox(1);
            } else {
                installingBill.setiDoubleBox(1);
            }

            if (!item.get(16).equals("")) {
                installingBill.setI4gRouter(1);
            }

            if (!item.get(20).equals("")) {
                installingBill.setiRouter(1);
            }

            if (!item.get(25).equals("")) {
                installingBill.setiCamera(1);
            }

            installingBill.setvConnectMode(item.get(10));
            String[] cables = {item.get(13), item.get(39), item.get(41)};
            for (String c : cables) {
                if (c != null && !c.equals("") && !c.equals("0.0")) {
                    installingBill.setiCable(1);
                } else {
                    installingBill.setiCable(0);
                }
            }
            if (!item.get(43).equals("") && !item.get(43).equals("0.0")) {
                installingBill.setiPowerSupply(1);
            }
            installingBill.setiBodySensor(item.get(33).equals(compareVariable) ? 1 : 0);
            installingBill.setiCarroofDoorSensor(item.get(50).equals(compareVariable) ? 1 : 0);
            installingBill.setiCarroofLockSensor(item.get(52).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomServiceSensor(item.get(54).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomOverhaulSensor(item.get(56).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomSwitchSensor(item.get(58).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomCallSensor(item.get(60).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomCircuitSensor(item.get(62).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomHallCircuitSensor(item.get(64).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomSwitchCircuitSensor(item.get(66).equals(compareVariable) ? 1 : 0);
            installingBill.setiMachRoomElectricitySensor(item.get(68).equals(compareVariable) ? 1 : 0);
            installingBill.setiStatus(TblDistributionTaskStatusEnum.DistributeLess.getValue());
            billList.add(installingBill);

            TblElevator tblElevator = new TblElevator();
            tblElevator.setVEquipmentCode(item.get(4));
            List<TblElevator> tblElevators = tblElevatorService.getByEntity(tblElevator);

            if (tblElevators != null && tblElevators.size() > 0) {
                tblElevator = tblElevators.get(0);

                if (installingBill.getiSingleBox() != null && installingBill.getiSingleBox() == 1) {
                    // 设备信息 - 迅达单盒
                    TblDevice liftXunDa = new TblDevice();
                    liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());
                    liftXunDa.setVSensorType(BusinessConstants.SENSOR_TYPE_SINGLEBOX);
                    liftXunDa.setVElevatorId(tblElevator.getVElevatorId());
                    liftXunDa.setVElevatorCode(tblElevator.getVElevatorCode());
                    liftXunDa.setDtCreateTime(new Date());
                    tblDeviceService.insertDevice(liftXunDa);
                }

                if (installingBill.getiDoubleBox() != null && installingBill.getiDoubleBox() == 1) {
                    // 设备信息 - 轿顶
                    TblDevice carRoof = new TblDevice();
                    carRoof.setVDeviceId(SnowFlakeUtils.nextStrId());
                    carRoof.setVSensorType(BusinessConstants.CAR_ROOF);
                    carRoof.setVElevatorId(tblElevator.getVElevatorId());
                    carRoof.setVElevatorCode(tblElevator.getVElevatorCode());
                    carRoof.setDtCreateTime(new Date());
                    tblDeviceService.insertDevice(carRoof);

                    // 设备信息 - 机房
                    TblDevice motorRoom = new TblDevice();
                    motorRoom.setVDeviceId(SnowFlakeUtils.nextStrId());
                    motorRoom.setVSensorType(BusinessConstants.MOTOR_ROOM);
                    motorRoom.setVElevatorId(tblElevator.getVElevatorId());
                    motorRoom.setVElevatorCode(tblElevator.getVElevatorCode());
                    motorRoom.setDtCreateTime(new Date());
                    tblDeviceService.insertDevice(motorRoom);
                }
            }
        }

        tblElevatorService.updateElevatorCodeBatch(elevatorList);

        return billList;
    }

}
