package com.shmashine.pm.api.controller.village;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.entity.TblVillageDeviceBill;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.controller.village.vo.AddElevatorReqVO;
import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblVillageDto;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.genTaskModule.GenerateTaskModule;
import com.shmashine.pm.api.module.village.input.SearchVillaListModule;
import com.shmashine.pm.api.module.village.input.SearchVillaSelectListModule;
import com.shmashine.pm.api.service.dept.BizDeptService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.project.TblProjectServiceI;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.service.village.BizVillageService;
import com.shmashine.pm.api.service.village.TblVillageServiceI;
import com.shmashine.pm.api.service.villageDeviceBill.TblVillageDeviceBillService;
import com.shmashine.pm.api.util.PojoConvertUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * 小区接口
 */
@RequestMapping("village")
@RestController
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class VillageController extends BaseRequestEntity {

    @Autowired
    private BizVillageService bizVillageService;
    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;
    @Autowired
    private TblVillageDeviceBillService tblVillageDeviceBillService;
    @Autowired
    private TblProjectServiceI tblProjectServiceI;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TblElevatorService tblElevatorService;

    /**
     * 获取小区列表
     */
    @Operation(summary = "获取小区列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchVillageList")
    public Object searchVillageList(@RequestBody SearchVillaListModule searchVillaListModule) {
        //1. 查找有权查看的项目
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号

        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + deptId;
        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(deptId, results);
            if (!results.contains(deptId)) {
                results.add(deptId);
            }
            redisTemplate.opsForValue().set(key, results);

        }
        // 4. 查找小区 级联找
        searchVillaListModule.setPermissionDeptIds((ArrayList<String>) results);
        //        searchVillaListModule.setAdminFlag(bizUserService.isAdminOrPm(getUserId()));

        searchVillaListModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        searchVillaListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizVillageService.searchVillageList(searchVillaListModule));
    }

    /**
     * 根据项目获取小区下拉框
     */
    @PostMapping("/searchVillageSelectList")
    public ResponseResult searchVillageSelectList(
            @RequestBody @Valid SearchVillaSelectListModule searchVillaSelectListModule) {
        return ResponseResult.successObj(bizVillageService.searchVillageSelectList(searchVillaSelectListModule));
    }

    /**
     * 获取所有小区结果
     */
    @PostMapping("searchAllVillage")
    public ResponseResult searchAllVillage() {
        //1. 查找有权查看的项目
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(deptId);
        recursion(deptId, results);
        // 4. 查找小区 级联找
        SearchVillaListModule searchVillaListModule = new SearchVillaListModule();
        searchVillaListModule.setPermissionDeptIds((ArrayList<String>) results);
        searchVillaListModule.setIDelFlag(0);
        return ResponseResult.successObj(bizVillageService.searchAllVillage(searchVillaListModule));
    }


    /**
     * 递归查询 下级部门的编号
     */
    public void recursion(String deptId, List<String> strings) {

        if (StringUtils.hasText(deptId)) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(deptId);
            if (null != userDeptIds && !userDeptIds.isEmpty()) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            if (userDeptIds != null) {
                strings.addAll(userDeptIds);
            }
        }
    }

    /**
     * 查看小区
     */
    @PostMapping("/getVillageInfo")
    public Object getVillageInfo(@RequestBody TblVillage tblVillage) {
        TblVillageDto villageDto = bizVillageService.getBizInfoById(tblVillage.getVVillageId());
        TblVillageDeviceBill deviceBill = tblVillageDeviceBillService.getByVillageId(villageDto.getVVillageId());

        TblElevator elevatorModule = new TblElevator();
        elevatorModule.setVVillageId(tblVillage.getVVillageId());

        List<TblElevator> elevators = tblElevatorService.listByEntity(elevatorModule);
        villageDto.setElevatorList(elevators);

        // 扩展电梯群租系数，楼层群租系数
        //bizVillageService.extendGroupLeasingElevatorAndFloorCoefficient(villageDto);

        if (deviceBill != null) {
            villageDto.setDeviceBill(deviceBill);
            return ResponseResult.successObj(villageDto);
        } else {
            return ResponseResult.successObj(villageDto);
        }
    }

    /**
     * 添加小区
     */
    @Operation(summary = "添加小区", security = {@SecurityRequirement(name = "token")})
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertVillageInfo")
    public Object insertVillageInfo(@RequestBody TblVillageDto tblVillageDto) {

        int existed = tblVillageServiceI.existsByName(tblVillageDto.getVVillageName(), tblVillageDto.getVProjectId());

        if (existed > 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "小区名已存在"));
        }

        if (tblVillageDto.getdInstallDate().compareTo(tblVillageDto.getdInvestigateDate()) < 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_08"));
        }

        TblVillageDeviceBill bill = tblVillageDto.getDeviceBill();

        if (bill != null && null == bill.getIDoubleBoxCount() && null == bill.getISingleBoxCount()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_09"));
        }

        String villageId = SnowFlakeUtils.nextStrId();
        tblVillageDto.setVVillageId(villageId);
        tblVillageDto.setIDelFlag(0);

        TblVillage tblVillage = PojoConvertUtil.convertPojo(tblVillageDto, TblVillage.class);
        int insert = tblVillageServiceI.insert(tblVillage);

        if (insert > 0) {
            if (tblVillageDto.getiElevatorCount() > 0) {
                int count = 0;

                List<TblElevator> elevatorList = new ArrayList<>();
                do {
                    count++;

                    TblElevator elevator = new TblElevator();

                    String elevatorId = SnowFlakeUtils.nextStrId();
                    elevator.setVElevatorId(elevatorId);
                    elevator.setVProjectId(tblVillageDto.getVProjectId());
                    elevator.setVVillageId(villageId);

                    elevator.setVElevatorName(tblVillage.getVVillageName() + "#" + count);
                    elevator.setVAddress(tblVillage.getVVillageName() + "#" + count);
                    elevator.setVElevatorCode(SnowFlakeUtils.nextStrId());
                    elevator.setVEquipmentCode("pm-initEquipmentCode");

                    elevator.setIInstallStatus(0);
                    elevator.setDtCreateTime(new Date());
                    elevator.setDtModifyTime(new Date());
                    elevator.setIDelFlag(0);

                    elevatorList.add(elevator);
                } while (count < tblVillageDto.getiElevatorCount());

                tblElevatorService.insertBatchParty(elevatorList);
            }

            TblVillageDeviceBill tblVillageDeviceBill = tblVillageDto.getDeviceBill();
            if (tblVillageDeviceBill == null) {
                tblVillageDeviceBill = new TblVillageDeviceBill();
            }

            String billId = SnowFlakeUtils.nextStrId();
            tblVillageDeviceBill.setVVillageDeviceBillId(billId);
            tblVillageDeviceBill.setDtModifyTime(new Date());
            tblVillageDeviceBill.setDtCreateTime(new Date());
            tblVillageDeviceBill.setVVillageId(tblVillageDto.getVVillageId());
            tblVillageDeviceBill.setIDelFlag(0);
            tblVillageDeviceBillService.insert(tblVillageDeviceBill);

            TblProject project = tblProjectServiceI.getById(tblVillage.getVProjectId());

            project.setiVillageCount(project.getiVillageCount() + 1);
            tblProjectServiceI.update(project);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 编辑小区
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editVillageInfo")
    public Object editVillageInfo(@RequestBody TblVillageDto tblVillageDto) {

        int existed = tblVillageServiceI.existsByName(tblVillageDto.getVVillageName(), tblVillageDto.getVProjectId());

        if (existed > 1) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "小区名已存在"));
        }

        TblVillageDeviceBill bill = tblVillageDto.getDeviceBill();

        if (tblVillageDto.getdInstallDate().compareTo(tblVillageDto.getdInvestigateDate()) < 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_08"));
        }

        if (null == bill.getIDoubleBoxCount() && null == bill.getISingleBoxCount()) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg10_09"));
        }

        TblVillage tblVillage = PojoConvertUtil.convertPojo(tblVillageDto, TblVillage.class);
        int update = tblVillageServiceI.update(tblVillage);

        if (update > 0) {
            if (bill != null) {
                tblVillageDeviceBillService.update(bill);
            } else {
                bill.setVVillageDeviceBillId(SnowFlakeUtils.nextStrId());
                bill.setVVillageId(tblVillage.getVVillageId());
                tblVillageDeviceBillService.insert(bill);
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 删除小区
     */
    @PostMapping("/deleteVillageInfo")
    public Object deleteVillageInfo(@RequestBody TblVillage tblVillage) {
        tblVillage = tblVillageServiceI.getById(tblVillage.getVVillageId());
        tblVillage.setIDelFlag(1);
        int update = tblVillageServiceI.update(tblVillage);
        if (update > 0) {
            TblProject project = tblProjectServiceI.getById(tblVillage.getVProjectId());
            project.setiVillageCount(project.getiVillageCount() < 0 ? 0 : project.getiVillageCount() - 1);

            tblProjectServiceI.update(project);
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    @PostMapping("/clearVillageAllInfo")
    public Object clearVillageAllInfo(@RequestParam("vVillageId") @NotNull String vVillageId) {
        return ResponseResult.successObj(bizVillageService.clearVillageAllInfo(vVillageId));
    }

    @GetMapping("/villageStatusMap")
    public Object getVillageStatus() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TblVillageStatusEnum em : TblVillageStatusEnum.values()) {
            map.put(em.getValue(), em.getName());
        }
        return ResponseResult.successObj(map);
    }

    @PostMapping("/getVillageCount")
    public Object getVillageCount(@RequestBody TblVillage tblVillage) {
        return ResponseResult.successObj(tblVillageServiceI.countByEntity(tblVillage));
    }

    @PostMapping("/addElevator")
    @Transactional
    public Object addElevator(@RequestBody AddElevatorReqVO tblVillage) {

        for (Integer i = 0; i < tblVillage.getElevatorNumber(); i++) {
            TblVillage village = tblVillageServiceI.getById(tblVillage.getVVillageId());

            int count = village.getiElevatorCount() == null ? 0 : village.getiElevatorCount();

            TblElevator elevator = new TblElevator();
            String elevatorId = SnowFlakeUtils.nextStrId();
            elevator.setVElevatorId(elevatorId);
            elevator.setVProjectId(village.getVProjectId());
            elevator.setVVillageId(village.getVVillageId());

            elevator.setVElevatorName(village.getVVillageName() + "#" + (count + 1));
            elevator.setVAddress(village.getVVillageName() + "#" + (count + 1));
            elevator.setVElevatorCode(SnowFlakeUtils.nextStrId());
            elevator.setVEquipmentCode("pm-initEquipmentCode");

            elevator.setIElevatorType(1);
            elevator.setIInstallStatus(0);
            elevator.setDtCreateTime(new Date());
            elevator.setDtModifyTime(new Date());
            elevator.setIDelFlag(0);

            tblElevatorService.insert(elevator);

            village.setiElevatorCount(count + 1);
            village.setiStatus(TblVillageStatusEnum.InvestigateLess.getValue());
            tblVillageServiceI.update(village);
        }

        return ResponseResult.success();
    }


    @PostMapping("/delElevator")
    @Transactional
    public Object delElevator(@RequestBody GenerateTaskModule module) {

        var elevatorIds = Arrays.asList(module.getvElevatorIds().split(","));

        List<TblElevator> elevators = tblElevatorService.listByIds(elevatorIds);

        if (elevators.stream().allMatch(elevator -> elevator.getiPmStatus()
                .equals(TblVillageStatusEnum.InvestigateLess.getValue()))) {
            tblElevatorService.deleteByIds(elevatorIds);
        } else {
            throw new BizException(ResponseResult.error("只有待现勘电梯能删除"));
        }

        TblVillage village = tblVillageServiceI.getById(module.getvVillageId());
        int count = village.getiElevatorCount() == null ? 0 : village.getiElevatorCount();

        village.setiElevatorCount((count == 0 ? 0 : (count - 1)));
        tblVillageServiceI.update(village);

        return ResponseResult.success();
    }

    /**
     * 电梯状态
     *
     * @param vVillageId 小区ID
     * @return 结果
     */
    @GetMapping("/getElevatorCountByStatus")
    public Object getElevatorCount(@RequestParam("vVillageId") @NotNull String vVillageId) {
        return ResponseResult.successObj(tblElevatorService.countWithPmStatus(vVillageId));
    }

    /**
     * 手动完成pm任务
     */
    @PostMapping("/completePmTask")
    public Object completePmTask(@RequestParam("vVillageId") @NotNull String vVillageId) {
        TblElevator searchModule = new TblElevator();
        searchModule.setVVillageId(vVillageId);

        List<TblElevator> list = tblElevatorService.listByEntity(searchModule);

        for (TblElevator elevator : list) {
            elevator.setiPmStatus(TblVillageStatusEnum.Runing.getValue());
            tblElevatorService.update(elevator);
        }

        return ResponseResult.success();
    }

    /**
     * 手动完成pm任务
     */
    @PostMapping("/completeElevatorPmTask")
    public Object completeElevatorPmTask(@RequestParam("vElevatorId") @NotNull String vElevatorId) {
        TblElevator elevator = new TblElevator();
        elevator.setVElevatorId(vElevatorId);
        elevator.setiPmStatus(TblVillageStatusEnum.Runing.getValue());
        return ResponseResult.successObj(tblElevatorService.update(elevator));
    }
}
