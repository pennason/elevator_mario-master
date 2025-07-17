package com.shmashine.pm.api.controller.investigateTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.entity.TblPmFile;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblInvestigateTaskDto;
import com.shmashine.pm.api.enums.TblInvestigateBillStatusEnum;
import com.shmashine.pm.api.enums.TblInvestigateTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.genTaskModule.GenerateTaskModule;
import com.shmashine.pm.api.module.investigateBill.input.SearchTaskBillModule;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskListModule;
import com.shmashine.pm.api.service.elevator.TblElevatorBrandService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.investigateBill.BizInvestigateBillService;
import com.shmashine.pm.api.service.investigateBill.TblInvestigateBillService;
import com.shmashine.pm.api.service.investigateTask.BizInvestigateTaskService;
import com.shmashine.pm.api.service.investigateTask.TblInvestigateTaskService;
import com.shmashine.pm.api.service.pmFile.TblPmFileService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;
import com.shmashine.pm.api.service.villageDeviceBill.TblVillageDeviceBillService;
import com.shmashine.pm.api.util.ExcelUtil;

/**
 * 现勘接口
 */
@RequestMapping("investigateTask")
@RestController
public class InvestigateTaskController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private TblInvestigateTaskService tblInvestigateTaskService;
    @Autowired
    private BizInvestigateTaskService bizInvestigateTaskService;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblPmFileService tblPmFileService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private BizInvestigateBillService bizInvestigateBillService;
    @Autowired
    private TblInvestigateBillService tblInvestigateBillService;
    @Autowired
    private TblVillageDeviceBillService tblVillageDeviceBillService;
    @Autowired
    private TblElevatorBrandService tblElevatorBrandService;

    /**
     * 获取任务列表
     *
     * @return
     */
    @PostMapping("/searchInvestigateTaskList")
    public Object searchInvestigateTaskList(@RequestBody SearchInvestigateTaskListModule searchInvestigateTaskListModule) {
        searchInvestigateTaskListModule.setAdminFlag(bizUserService.isAdminOrPm(getUserId()));
        searchInvestigateTaskListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizInvestigateTaskService.searchInvestigateTaskList(searchInvestigateTaskListModule));
    }

    /**
     * 查看
     *
     * @return
     */
    @PostMapping("/getInvestigateTaskInfo")
    public Object getTaskInfo(@RequestBody TblInvestigateTask tblInvestigateTask) {
        Map villageInfo = bizInvestigateTaskService.getBizVillageInfo(tblInvestigateTask.getvInvestigateTaskId());
        List<Map> billInfo = bizInvestigateBillService.getAllBillByInvestigateTaskId(tblInvestigateTask.getvInvestigateTaskId());

        TblInvestigateTaskDto taskDto = new TblInvestigateTaskDto();
        taskDto.setVillageInfo(villageInfo);
        taskDto.setInvestigateBillInfo(billInfo);
//        return ResponseResult.successObj(tblInvestigateTaskService.getById(vInvestigateTaskId));
        return ResponseResult.successObj(taskDto);
    }

    /**
     * 添加任务
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertInvestigateTask")
    public Object insertTask(@RequestBody GenerateTaskModule generateTaskModule) {

        TblVillage village = tblVillageServiceI.getById(generateTaskModule.getvVillageId());
        List<String> ids = Arrays.asList(generateTaskModule.getvElevatorIds().split(","));
        List<TblElevator> elevators = tblElevatorService.listByIds(ids);

        if (elevators.stream().allMatch(elevator ->
                elevator.getiPmStatus() == TblVillageStatusEnum.InvestigateLess.getValue())) {
            TblInvestigateTask tblInvestigateTask = new TblInvestigateTask();

            String taskId = SnowFlakeUtils.nextStrId();
            tblInvestigateTask.setvInvestigateTaskId(taskId);
            tblInvestigateTask.setvProjectId(village.getVProjectId());
            tblInvestigateTask.setvVillageId(village.getVVillageId());
            tblInvestigateTask.setvAddress(village.getVAddress());

            tblInvestigateTask.setiElevatorCount(elevators.size());
            tblInvestigateTask.setdInvestigateDate(village.getdInvestigateDate());
            tblInvestigateTask.setiStatus(TblInvestigateTaskStatusEnum.UploadLess.getValue());
            tblInvestigateTask.setDtCreateTime(new Date());
            tblInvestigateTask.setDtModifyTime(new Date());

            tblInvestigateTask.setvPrincipalId(generateTaskModule.getvPrincipalId());

            if (tblInvestigateTask.getvPrincipalId() == null) {
                tblInvestigateTask.setvPrincipalId(village.getvInvestigatorId());
            }
            tblInvestigateTask.setiDelFlag(0);

            int result = tblInvestigateTaskService.insert(tblInvestigateTask);

            if (result > 0) {

                var tblInvestigateBills = new ArrayList<TblInvestigateBill>();

                var tblVillageDeviceBill = tblVillageDeviceBillService.getByVillageId(village.getVVillageId());

                elevators.forEach(ele -> {
                    ele.setiPmStatus(TblVillageStatusEnum.Investigateing.getValue());
                    tblElevatorService.update(ele);

                    var tblInvestigateBill = new TblInvestigateBill();

                    tblInvestigateBill.setvInvestigateTaskId(taskId);
                    tblInvestigateBill.setvInvestigateBillId(SnowFlakeUtils.nextStrId());
                    tblInvestigateBill.setvProjectId(village.getVProjectId());
                    tblInvestigateBill.setvVillageId(village.getVVillageId());
                    tblInvestigateBill.setvElevatorId(ele.getVElevatorId());
                    tblInvestigateBill.setvDevicePosition(null == tblVillageDeviceBill
                            ? null : tblVillageDeviceBill.getVDevicePosition());

                    tblInvestigateBill.setvCreateUserId(getUserId());
                    tblInvestigateBill.setiStatus(TblInvestigateBillStatusEnum.ImproveLess.getValue());
                    tblInvestigateBill.setiElevatorCount(1);
                    tblInvestigateBill.setDtCreateTime(new Date());
                    tblInvestigateBill.setDtModifyTime(new Date());
                    tblInvestigateBill.setiDelFlag(0);

                    tblInvestigateBills.add(tblInvestigateBill);
                });

                tblInvestigateBillService.insertBatch(tblInvestigateBills);
            } else {
                throw new BizException(ResponseResult.error("新建现勘任务失败"));
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有待现勘的电梯才能生成现勘单"));
        }
    }

    /**
     * 各个状态数量
     *
     * @return
     */
    @GetMapping("/getCountByStatus")
    public Object getCountByStatus() {
        String principalId = null;

        if (!bizUserService.isAdminOrPm(getUserId())) {
            principalId = getUserId();
        }
        return ResponseResult.successObj(tblInvestigateTaskService.countByStatus(principalId));
    }


    /**
     * 编辑任务
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editInvestigateTaskInfo")
    public Object editTaskInfo(@RequestBody TblInvestigateTask tblInvestigateTask) {
        int success = tblInvestigateTaskService.update(tblInvestigateTask);
        if (success > 0) {
            if (tblInvestigateTask.getiStatus() == TblInvestigateTaskStatusEnum.Completed.getValue()) {

                TblVillage village = tblVillageServiceI.getById(tblInvestigateTask.getvVillageId());
                village.setiStatus(TblVillageStatusEnum.DistributeLess.getValue());
                tblVillageServiceI.update(village);
            }
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    @GetMapping("/investigateTaskStatusMap")
    public Object getInvestigateTaskStatus() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TblInvestigateTaskStatusEnum em : TblInvestigateTaskStatusEnum.values()) {
            map.put(em.getValue(), em.getName());
        }
        return ResponseResult.successObj(map);
    }

    @PostMapping("/downloadTemplate")
    public Object downLoadTemplate(@RequestBody TblInvestigateTask tblInvestigateTask) {

        TblInvestigateTask entityTask = tblInvestigateTaskService.getById(tblInvestigateTask.getvInvestigateTaskId());

        String fileUrl = entityTask.getvTemplateFileUrl();

        List<Map> relativeInfos = bizInvestigateBillService.selectRelatedInfosByTaskId(tblInvestigateTask.getvInvestigateTaskId());

        List<List> dataList = new ArrayList<>();

        for (int i = 0; i < relativeInfos.size(); i++) {
            Map relativeInfo = relativeInfos.get(i);

            List<String> attrs = new ArrayList<>();
            attrs.add(relativeInfo.get("deptName").toString());
            attrs.add(relativeInfo.get("projectName").toString());
            attrs.add(relativeInfo.get("villageName").toString());
            attrs.add(relativeInfo.get("elevatorId").toString());
            attrs.add(relativeInfo.get("elevatorName") == null ? "-" : relativeInfo.get("elevatorName").toString());
            dataList.add(attrs);
        }

        fileUrl = ExcelUtil.createExcel(dataList, "investigateTask", tblInvestigateTask.getvInvestigateTaskId());
        tblInvestigateTask.setvInvestigateTaskId(tblInvestigateTask.getvInvestigateTaskId());
        tblInvestigateTask.setvTemplateFileUrl(fileUrl);
        int success = tblInvestigateTaskService.update(tblInvestigateTask);

        if (success > 0) {
            return ResponseResult.successObj(fileUrl);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    @PostMapping("/downloadUploadedFile")
    public Object downloadUploadedFile(@RequestBody TblInvestigateTask tblInvestigateTask) {

        TblPmFile pmFile = new TblPmFile();
        pmFile.setvTargetId(tblInvestigateTask.getvInvestigateTaskId());
        pmFile = tblPmFileService.getByEntity(pmFile);


        String fileUrl = pmFile.getvFileUrl();

        if (fileUrl != null && !fileUrl.equals("")) {
            return ResponseResult.successObj(fileUrl);
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 上传文件
     */

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("vInvestigateTaskId") String vInvestigateTaskId) {

        if (file == null) {
            throw new BizException(ResponseResult.error("上传文件不能为空"));
        }

        List<List<String>> data = ExcelUtil.readXlsxData(file, 1);

        TblInvestigateTask tblInvestigateTask = tblInvestigateTaskService.getById(vInvestigateTaskId);
        if (tblInvestigateTask.getiStatus() == TblInvestigateTaskStatusEnum.Completed.getValue()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01");
        }

        TblVillage village = tblVillageServiceI.getById(tblInvestigateTask.getvVillageId());

        if (!village.getVVillageName().equals(data.get(0).get(2))) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg10_03");
        }

        data = data.stream().filter(item -> (item.get(5) != "" && item.get(5) != null)).collect(Collectors.toList());

        String fileUrl = OSSUtil.savePmTaskFile(file);

        TblPmFile taskFile = new TblPmFile();
        taskFile.setvTargetId(tblInvestigateTask.getvInvestigateTaskId());

        taskFile = tblPmFileService.getByEntity(taskFile);

        if (taskFile == null) {
            taskFile = new TblPmFile();
            String fileId = SnowFlakeUtils.nextStrId();
            taskFile.setvPmFileId(fileId);
            taskFile.setvFileUrl(fileUrl);
            taskFile.setvTargetId(tblInvestigateTask.getvInvestigateTaskId());
            taskFile.setvCreateUserId(getUserId());
            taskFile.setvModifyUserId(getUserId());

            tblPmFileService.insert(taskFile);
        } else {
            taskFile.setvFileUrl(fileUrl);
            taskFile.setvModifyUserId(getUserId());
            tblPmFileService.update(taskFile);
        }

        if (tblInvestigateTask.getiElevatorCount() == data.size()) {

            TblInvestigateBill searchModule = new TblInvestigateBill();
            searchModule.setvInvestigateTaskId(tblInvestigateTask.getvInvestigateTaskId());

            List<TblInvestigateBill> list = tblInvestigateBillService.listByEntity(searchModule);

            for (int i = 0; i < list.size(); i++) {
                TblInvestigateBill bill = list.get(i);

                if (bill.getiStatus() == TblInvestigateBillStatusEnum.Completed.getValue() || bill.getiStatus() == TblInvestigateBillStatusEnum.Canceled.getValue())
                    continue;
                List<String> item = data.get(i);
                bill.setiStraightDoorElevator(item.get(9).equals("是") ? 1 : 0);
                bill.setiControlElevator(item.get(9).equals("是") ? 1 : 0);
                bill.setiStatus(TblInvestigateBillStatusEnum.Completed.getValue());
                bill.setvElevatorBrand(item.get(7));
                tblInvestigateBillService.update(bill);

            }
            tblInvestigateTask.setDtModifyTime(new Date());
            tblInvestigateTask.setiStatus(TblInvestigateTaskStatusEnum.Completed.getValue());
        }

        int success = tblInvestigateTaskService.update(tblInvestigateTask);

        if (success > 0) {
            if (tblInvestigateTask.getiStatus() == TblInvestigateTaskStatusEnum.Completed.getValue()) {

                for (List<String> item : data) {
                    if (item.get(5).length() != 18 && item.get(5).length() != 20) {
                        throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_04"));
                    }

                    String elevatorId = item.get(3);
                    TblElevator elevator = tblElevatorService.getById(elevatorId);

                    elevator.setVEquipmentCode(item.get(5));
                    elevator.setVAddress(item.get(6));
                    elevator.setIInstallStatus(0);
                    elevator.setDtModifyTime(new Date());
                    elevator.setIDelFlag(0);
                    elevator.setiPmStatus(TblVillageStatusEnum.DistributeLess.getValue());

                    if (item.get(7) != null && !"".equals(item.get(7))) {
                        HashMap elevatorBrand = tblElevatorBrandService.selectBrandByName(item.get(7));
                        if (elevatorBrand != null) {
                            elevator.setVElevatorBrandId((String) elevatorBrand.get("vElevatorBrandId"));
                        }
                    }

                    if (item.get(8) != null && !"".equals(item.get(8))) {
                        elevator.setVElevatorName(item.get(8));
                    }

                    tblElevatorService.update(elevator);
                }

                // 更新小区状态
                SearchTaskBillModule module = new SearchTaskBillModule();
                module.setvVillageId(tblInvestigateTask.getvVillageId());
                List<Integer> billStatuses = bizInvestigateBillService.getAllStatus(module);

                billStatuses = billStatuses.stream().filter(bs -> bs != TblInvestigateBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

                if (billStatuses.size() == village.getiElevatorCount()) {
                    if (billStatuses.stream().allMatch(st -> st == TblInvestigateBillStatusEnum.Completed.getValue())) {
                        village.setiStatus(TblVillageStatusEnum.DistributeLess.getValue());
                        village.setDtModifyTime(new Date());
                        tblVillageServiceI.update(village);
                    }
                }
            }
        }
        return ResponseResult.success();
    }
}
