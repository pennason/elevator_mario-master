package com.shmashine.pm.api.controller.investigateBill;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import com.shmashine.pm.api.entity.TblPmImage;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblInvestigateBillDto;
import com.shmashine.pm.api.enums.TblInvestigateBillStatusEnum;
import com.shmashine.pm.api.enums.TblInvestigateTaskStatusEnum;
import com.shmashine.pm.api.enums.TblPmImageTypeEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.investigateBill.InvestigateBillModule;
import com.shmashine.pm.api.module.investigateBill.input.SearchTaskBillModule;
import com.shmashine.pm.api.service.elevator.TblElevatorBrandService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.investigateBill.BizInvestigateBillService;
import com.shmashine.pm.api.service.investigateBill.TblInvestigateBillService;
import com.shmashine.pm.api.service.investigateTask.TblInvestigateTaskService;
import com.shmashine.pm.api.service.pmImage.TblPmImageService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;
import com.shmashine.pm.api.util.PojoConvertUtil;

/**
 * 现勘单接口
 */
@RequestMapping("investigateBill")
@RestController
public class InvestigateBillController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private TblInvestigateBillService tblInvestigateBillService;
    @Autowired
    private BizInvestigateBillService bizInvestigateBillService;
    @Autowired
    private TblInvestigateTaskService tblInvestigateTaskService;
    @Autowired
    private TblPmImageService tblPmImageService;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblElevatorBrandService tblElevatorBrandService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;

    /**
     * 获取任务列表
     *
     * @return
     */
    @PostMapping("/searchInvestigateBillList")
    public Object searchInvestigateBillList(@RequestBody SearchTaskBillModule searchTaskBillModule) {
        return ResponseResult.successObj(bizInvestigateBillService.searchInvestigateBillList(searchTaskBillModule));
    }

    /**
     * 查看
     *
     * @return
     */
    @PostMapping("/getInvestigateBillInfo")
    public Object getTaskInfo(@RequestBody @Valid @NotNull(message = "请输入任务Id") String vInvestigateBillId) {
        return ResponseResult.successObj(tblInvestigateBillService.getById(vInvestigateBillId));
    }

    @PostMapping("/searchList")
    public Object searchList(@RequestBody InvestigateBillModule investigateBillModule) {
        return ResponseResult.successObj(bizInvestigateBillService.selectByBillModule(investigateBillModule));
    }

    /**
     * 查看
     *
     * @return
     */
    @GetMapping("/getBillBizInfo")
    public Object getBillBizInfo(@RequestParam("vInvestigateBillId") @NotNull(message = "现勘单id不能为空") String vInvestigateBillId) {
        TblInvestigateBill tblInvestigateBill = tblInvestigateBillService.getById(vInvestigateBillId);

        TblVillage village = tblVillageServiceI.getById(tblInvestigateBill.getvVillageId());

        Map<String, Object> result = new HashMap<>();
        result.put("investigateBillInfo", bizInvestigateBillService.getBillInfoByInvestigateBillId(tblInvestigateBill.getvInvestigateBillId()));
        result.put("relatedInfo", bizInvestigateBillService.selectRelatedInfo(tblInvestigateBill.getvInvestigateBillId()));
        result.put("elevatorCount", village.getiElevatorCount());

        List<Map> statuses = bizInvestigateBillService.statusCountByVillageId(village.getVVillageId());

        long hadInvestigatedCount = 0;

        for (Map st : statuses) {
            int stat = (int) st.get("i_status");
            if (stat > TblVillageStatusEnum.InvestigateLess.getValue() && stat != TblVillageStatusEnum.Investigateing.getValue()) {
                hadInvestigatedCount += (long) st.get("count");
            }
        }

        result.put("investigatedCount", hadInvestigatedCount);
        result.put("unInvestigatedCount", village.getiElevatorCount() - hadInvestigatedCount);
        return ResponseResult.successObj(result);
    }

    /**
     * 添加任务
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertInvestigateBill")
    public Object insertBill(@RequestBody TblInvestigateBillDto tblInvestigateBillDto) {

        String billId = SnowFlakeUtils.nextStrId();
        TblInvestigateBill tblInvestigateBill = PojoConvertUtil.convertPojo(tblInvestigateBillDto, TblInvestigateBill.class);

        Integer elevatorCount = bizInvestigateBillService.getElevatorCountByTaskId(tblInvestigateBillDto.getvInvestigateTaskId(), tblInvestigateBillDto.getvInvestigateBillId());
        TblInvestigateTask investigateTask = tblInvestigateTaskService.getById(tblInvestigateBillDto.getvInvestigateTaskId());

        if (investigateTask.isCompleted()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01"));
        }

        if ((elevatorCount + tblInvestigateBillDto.getiElevatorCount()) > investigateTask.getiElevatorCount()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_02"));
        }

        tblInvestigateBill.setvInvestigateBillId(billId);
        tblInvestigateBill.setiDelFlag(0);

        int insert = tblInvestigateBillService.insert(tblInvestigateBill);
        if (insert > 0) {

            List<TblPmImage> greenCodeImages = tblInvestigateBillDto.getGreenCodeImages();
            List<TblPmImage> floorImages = tblInvestigateBillDto.getFloorImages();
            List<TblPmImage> machRoomSignalImages = tblInvestigateBillDto.getMachRoomSignalImages();
            List<TblPmImage> equipmentCodeImages = tblInvestigateBillDto.getEquipmentCodeImages();
            List<TblPmImage> elevatorDoorBtnInsideImages = tblInvestigateBillDto.getElevatorDoorBtnInsideImages();

            List<TblPmImage> images = Stream.concat(greenCodeImages.stream(), floorImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), machRoomSignalImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), equipmentCodeImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), elevatorDoorBtnInsideImages.stream()).collect(Collectors.toList());

            images.forEach(item -> {
                String imageId = SnowFlakeUtils.nextStrId();
                item.setvPmImageId(imageId);
                item.setvTargetId(billId);
                item.setDtCreateTime(new Date());
                item.setDtModifyTime(new Date());
                item.setiDelFlag(0);
            });
            if (images.size() > 0) {
                tblPmImageService.insertBatch(images);
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 编辑任务
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editInvestigateBillInfo")
    public Object editInvestigateBillInfo(@RequestBody TblInvestigateBillDto tblInvestigateBillDto) {

        TblInvestigateBill tblInvestigateBill = PojoConvertUtil.convertPojo(tblInvestigateBillDto, TblInvestigateBill.class);
        tblInvestigateBill.setiStatus(TblInvestigateBillStatusEnum.Completed.getValue());

        TblInvestigateTask investigateTask =
                tblInvestigateTaskService.getById(tblInvestigateBillDto.getvInvestigateTaskId());

        int success = tblInvestigateBillService.update(tblInvestigateBill);
        if (success > 0) {
            tblPmImageService.deleteByTargetId(tblInvestigateBill.getvInvestigateBillId());
            List<TblPmImage> greenCodeImages = tblInvestigateBillDto.getGreenCodeImages();
            List<TblPmImage> floorImages = tblInvestigateBillDto.getFloorImages();
            List<TblPmImage> machRoomSignalImages = tblInvestigateBillDto.getMachRoomSignalImages();
            List<TblPmImage> equipmentCodeImages = tblInvestigateBillDto.getEquipmentCodeImages();
            List<TblPmImage> elevatorDoorBtnInsideImages = tblInvestigateBillDto.getElevatorDoorBtnInsideImages();

            List<TblPmImage> images = Stream.concat(greenCodeImages.stream(), floorImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), machRoomSignalImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), equipmentCodeImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), elevatorDoorBtnInsideImages.stream()).collect(Collectors.toList());

            images.forEach(item -> {
                String imageId = SnowFlakeUtils.nextStrId();
                item.setvPmImageId(imageId);
                item.setvTargetId(tblInvestigateBill.getvInvestigateBillId());
                item.setDtCreateTime(new Date());
                item.setDtModifyTime(new Date());
                item.setiDelFlag(0);
            });
            if (images.size() > 0) {
                tblPmImageService.insertBatch(images);
            }

            // 更新电梯
            TblElevator elevator = tblElevatorService.getById(tblInvestigateBillDto.getvElevatorId());

            elevator.setVEquipmentCode(tblInvestigateBillDto.getvEquipmentCode());
            elevator.setVAddress(tblInvestigateBillDto.getvAddress());
            elevator.setDtModifyTime(new Date());
            elevator.setIInstallStatus(0);
            elevator.setIDelFlag(0);

            if (StringUtils.hasText(tblInvestigateBillDto.getvEquipmentCode())) {
                elevator.setiPmStatus(TblVillageStatusEnum.DistributeLess.getValue());
            }

            if (StringUtils.hasText(tblInvestigateBillDto.getvElevatorBrand())) {
                HashMap elevatorBrand = tblElevatorBrandService.selectBrandByName(tblInvestigateBillDto.getvElevatorBrand());
                if (elevatorBrand != null) {
                    elevator.setVElevatorBrandId((String) elevatorBrand.get("vElevatorBrandId"));
                }
            }

            tblElevatorService.update(elevator);

            // 更新任务状态
            SearchTaskBillModule module = new SearchTaskBillModule();
            module.setvInvestigateTaskId(tblInvestigateBill.getvInvestigateTaskId());

            List<Integer> billStatus = bizInvestigateBillService.getAllStatus(module);
            billStatus = billStatus.stream().filter(bs -> bs != TblInvestigateBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

            // 手动填写电梯信息和文件上传电梯信息只能选择一个
            if (billStatus.stream().allMatch(item -> item == TblInvestigateBillStatusEnum.Completed.getValue())) {
                if (!StringUtils.hasText(tblInvestigateBillDto.getvEquipmentCode())) {
                    investigateTask.setiStatus(TblInvestigateTaskStatusEnum.ImproveLess.getValue());
                } else {
                    SearchTaskBillModule searchTaskBillModule = new SearchTaskBillModule();
                    searchTaskBillModule.setvInvestigateTaskId(investigateTask.getvInvestigateTaskId());

                    List<Map> investigateBills = bizInvestigateBillService.searchAllInvestigateBill(searchTaskBillModule);
                    List<String> elevatorIds = investigateBills.stream().map(bill -> (String) bill.get("vElevatorId")).collect(Collectors.toList());
                    List<TblElevator> elevators = tblElevatorService.listByIds(elevatorIds);

                    if (elevators.stream().allMatch(ele -> ele.getiPmStatus() == TblVillageStatusEnum.DistributeLess.getValue())) {
                        investigateTask.setiStatus(TblInvestigateTaskStatusEnum.Completed.getValue());
                    } else {
                        investigateTask.setiStatus(TblInvestigateTaskStatusEnum.ImproveLess.getValue());
                    }
                }

                tblInvestigateTaskService.update(investigateTask);

                if (investigateTask.getiStatus() == TblInvestigateTaskStatusEnum.Completed.getValue()) {
                    // 更新小区状态
                    TblVillage village = tblVillageServiceI.getById(investigateTask.getvVillageId());

                    module.setvInvestigateTaskId(null);
                    module.setvVillageId(investigateTask.getvVillageId());
                    List<Integer> billStatuses = bizInvestigateBillService.getAllStatus(module);

                    billStatuses = billStatuses.stream().filter(item -> item != TblInvestigateBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

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
        } else {
            throw new BizException(ResponseResult.error(""));
        }
    }

    /**
     * 上传现勘图片
     */

    @PostMapping("/uploadImage")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseResult.successObj(OSSUtil.savePmTaskFile(file));
    }

    /**
     * 删除图片
     *
     * @param vInvestigateBillImageId
     * @return
     */
    @PostMapping("/deleteImage")
    public Object deleteFile(String vInvestigateBillImageId) {
        return ResponseResult.successObj(tblPmImageService.deleteById(vInvestigateBillImageId));
    }

    @GetMapping("/billImageTypeMap")
    public Object getBillImageTypeMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TblPmImageTypeEnum em : TblPmImageTypeEnum.values()) {
            map.put(em.getValue(), em.getName());
        }
        return ResponseResult.successObj(map);
    }

    /**
     * @param elevatorId
     * @return
     */
    @GetMapping("/getRelatedInfoById")
    public Object getRelatedInfoById(@RequestParam("elevatorId") @NotNull String elevatorId) {
        return ResponseResult.successObj(tblElevatorService.getRelatedInfoById(elevatorId));
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/cancelBill")
    public Object cancelBill(@RequestBody TblInvestigateBillDto tblInvestigateBillDto) {

        TblInvestigateBill tblInvestigateBill = tblInvestigateBillService.getById(tblInvestigateBillDto.getvInvestigateBillId());

        if (tblInvestigateBill.getiStatus() == TblInvestigateBillStatusEnum.ImproveLess.getValue()) {

            tblInvestigateBill.setiStatus(TblInvestigateBillStatusEnum.Canceled.getValue());
            tblInvestigateBillService.update(tblInvestigateBill);

            TblElevator tblElevator = tblElevatorService.getById(tblInvestigateBill.getvElevatorId());
            tblElevator.setiPmStatus(TblVillageStatusEnum.InvestigateLess.getValue());
            tblElevatorService.update(tblElevator);

            TblInvestigateTask tblInvestigateTask = tblInvestigateTaskService.getById(tblInvestigateBill.getvInvestigateTaskId());

            SearchTaskBillModule module = new SearchTaskBillModule();
            module.setvInvestigateTaskId(tblInvestigateBill.getvInvestigateTaskId());

            List<Integer> billStatus = bizInvestigateBillService.getAllStatus(module);

            billStatus = billStatus.stream().filter(bs -> bs != TblInvestigateBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

            tblInvestigateTask.setiElevatorCount(billStatus.size());

            if (billStatus.size() == 0) {
                tblInvestigateTask.setiStatus(TblInvestigateTaskStatusEnum.Canceled.getValue());
            } else {
                if (billStatus.stream().allMatch(st -> st == TblInvestigateBillStatusEnum.Completed.getValue())) {
                    tblInvestigateTask.setiStatus(TblInvestigateTaskStatusEnum.ImproveLess.getValue());
                }
            }

            tblInvestigateTaskService.update(tblInvestigateTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有现勘中状态才能取消"));
        }
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
}
