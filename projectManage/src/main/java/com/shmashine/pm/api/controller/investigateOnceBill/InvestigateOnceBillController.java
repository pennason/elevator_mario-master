package com.shmashine.pm.api.controller.investigateOnceBill;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.entity.TblInvestigateOnceBill;
import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.entity.TblPmImage;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.enums.TblInvestigateBillStatusEnum;
import com.shmashine.pm.api.enums.TblInvestigateTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.service.investigateBill.BizInvestigateBillService;
import com.shmashine.pm.api.service.investigateBill.TblInvestigateBillService;
import com.shmashine.pm.api.service.investigateOnceBill.TblInvestigateOnceBillService;
import com.shmashine.pm.api.service.investigateTask.BizInvestigateTaskService;
import com.shmashine.pm.api.service.investigateTask.TblInvestigateTaskService;
import com.shmashine.pm.api.service.pmImage.TblPmImageService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;
import com.shmashine.pm.api.util.PojoConvertUtil;

/**
 * 现勘单接口
 */
@RequestMapping("investigateOnceBill")
@RestController
public class InvestigateOnceBillController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private TblPmImageService tblPmImageService;
    @Autowired
    private TblInvestigateOnceBillService tblInvestigateOnceBillService;
    @Autowired
    private TblInvestigateBillService tblInvestigateBillService;
    @Autowired
    private TblInvestigateTaskService tblInvestigateTaskService;
    @Autowired
    private BizInvestigateTaskService bizInvestigateTaskService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private BizInvestigateBillService bizInvestigateBillService;

    /**
     * 查看
     *
     * @return
     */
    @PostMapping("/getInvestigateOnceBillInfo")
    public Object getBillInfo(@RequestBody @Valid @NotNull(message = "请输入任务Id") String vInvestigateOnceBillId) {
        return ResponseResult.successObj(tblInvestigateOnceBillService.getById(vInvestigateOnceBillId));
    }

    /**
     * 查看现勘单详情
     *
     * @return
     */
    @PostMapping("/getInvestigateOnceBillInfoByTaskId")
    public Object getBillInfoByTaskId(@RequestBody @Valid @NotNull(message = "请输入任务Id") TblInvestigateOnceBill tblInvestigateOnceBill) {
        HashMap<String, Object> result = new HashMap<>();
        TblInvestigateOnceBill onceBill = tblInvestigateOnceBillService.getByTaskId(tblInvestigateOnceBill.getvInvestigateTaskId());

        if (onceBill == null) {
            result.put("investigateOnceBillInfo", new TblInvestigateOnceBill());
        } else {
            result.put("investigateOnceBillInfo", onceBill);
        }

        String taskId = tblInvestigateOnceBill.getvInvestigateTaskId();
        Map relatedInfo = bizInvestigateTaskService.getBizVillageInfo(taskId);
        result.put("relatedInfo", relatedInfo);

        TblInvestigateTask task = tblInvestigateTaskService.getById(taskId);
        TblVillage village = tblVillageServiceI.getById(task.getvVillageId());

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
    @PostMapping("/insertInvestigateOnceBill")
    public Object insertOnceBill(@RequestBody TblInvestigateOnceBill tblInvestigateOnceBill) {
        String billId = SnowFlakeUtils.nextStrId();

        TblInvestigateTask investigateTask = tblInvestigateTaskService.getById(tblInvestigateOnceBill.getvInvestigateTaskId());

        if (investigateTask.isCompleted()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01"));
        }

        tblInvestigateOnceBill.setvInvestigateOnceBillId(billId);
        tblInvestigateOnceBill.setiDelFlag(0);

        int insert = tblInvestigateOnceBillService.insert(tblInvestigateOnceBill);

        if (insert > 0) {
            List<TblPmImage> greenCodeImages = tblInvestigateOnceBill.getGreenCodeImages();
            List<TblPmImage> floorImages = tblInvestigateOnceBill.getFloorImages();
            List<TblPmImage> machRoomSignalImages = tblInvestigateOnceBill.getMachRoomSignalImages();
            List<TblPmImage> equipmentCodeImages = tblInvestigateOnceBill.getEquipmentCodeImages();
            List<TblPmImage> elevatorDoorBtnInsideImages = tblInvestigateOnceBill.getElevatorDoorBtnInsideImages();

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

            TblInvestigateBill searchModule = new TblInvestigateBill();
            searchModule.setvInvestigateTaskId(investigateTask.getvInvestigateTaskId());
            List<TblInvestigateBill> list = tblInvestigateBillService.listByEntity(searchModule);

            for (TblInvestigateBill b : list) {
                b = PojoConvertUtil.convertPojo(tblInvestigateOnceBill, TblInvestigateBill.class);
                b.setiStatus(TblInvestigateBillStatusEnum.Completed.getValue());
                tblInvestigateBillService.update(b);
            }

            // 更新任务状态
            investigateTask.setiStatus(TblInvestigateTaskStatusEnum.ImproveLess.getValue());
            tblInvestigateTaskService.update(investigateTask);

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
    @PostMapping("/editInvestigateOnceBillInfo")
    public Object editInvestigateOnceBillInfo(@RequestBody TblInvestigateOnceBill tblInvestigateOnceBill) {

        TblInvestigateTask investigateTask = tblInvestigateTaskService.getById(tblInvestigateOnceBill.getvInvestigateTaskId());

        if (investigateTask.isCompleted()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01"));
        }

        int success = tblInvestigateOnceBillService.update(tblInvestigateOnceBill);

        if (success > 0) {
            tblPmImageService.deleteByTargetId(tblInvestigateOnceBill.getvInvestigateOnceBillId());
            List<TblPmImage> greenCodeImages = tblInvestigateOnceBill.getGreenCodeImages();
            List<TblPmImage> floorImages = tblInvestigateOnceBill.getFloorImages();
            List<TblPmImage> machRoomSignalImages = tblInvestigateOnceBill.getMachRoomSignalImages();
            List<TblPmImage> equipmentCodeImages = tblInvestigateOnceBill.getEquipmentCodeImages();
            List<TblPmImage> elevatorDoorBtnInsideImages = tblInvestigateOnceBill.getElevatorDoorBtnInsideImages();

            List<TblPmImage> images = Stream.concat(greenCodeImages.stream(), floorImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), machRoomSignalImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), equipmentCodeImages.stream()).collect(Collectors.toList());
            images = Stream.concat(images.stream(), elevatorDoorBtnInsideImages.stream()).collect(Collectors.toList());

            images.forEach(item -> {
                String imageId = SnowFlakeUtils.nextStrId();
                item.setvPmImageId(imageId);
                item.setvTargetId(tblInvestigateOnceBill.getvInvestigateOnceBillId());
                item.setDtCreateTime(new Date());
                item.setDtModifyTime(new Date());
                item.setiDelFlag(0);
            });

            if (images.size() > 0) {
                tblPmImageService.insertBatch(images);
            }

            TblInvestigateBill searchModule = new TblInvestigateBill();
            searchModule.setvInvestigateTaskId(investigateTask.getvInvestigateTaskId());
            List<TblInvestigateBill> list = tblInvestigateBillService.listByEntity(searchModule);

            for (TblInvestigateBill b : list) {
                b = PojoConvertUtil.convertPojo(tblInvestigateOnceBill, TblInvestigateBill.class);
                b.setiStatus(TblInvestigateBillStatusEnum.Completed.getValue());
                tblInvestigateBillService.update(b);
            }

            // 更新任务状态
            investigateTask.setiStatus(TblInvestigateTaskStatusEnum.ImproveLess.getValue());
            tblInvestigateTaskService.update(investigateTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error(""));
        }
    }
}
