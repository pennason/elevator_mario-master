package com.shmashine.pm.api.controller.installingBill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.contants.BusinessConstants;
import com.shmashine.pm.api.entity.TblDevice;
import com.shmashine.pm.api.entity.TblDeviceSensor;
import com.shmashine.pm.api.entity.TblInstallingBill;
import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.entity.TblPmImage;
import com.shmashine.pm.api.entity.TblSensorConfig;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.entity.dto.TblInstallingBillDto;
import com.shmashine.pm.api.enums.TblInstallingBillStatusEnum;
import com.shmashine.pm.api.enums.TblInstallingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.module.installingBill.InstallingBillModule;
import com.shmashine.pm.api.service.deviceSensor.TblDeviceSensorService;
import com.shmashine.pm.api.service.elevator.TblDeviceService;
import com.shmashine.pm.api.service.elevator.TblElevatorService;
import com.shmashine.pm.api.service.installingBill.BizInstallingBillService;
import com.shmashine.pm.api.service.installingBill.TblInstallingBillService;
import com.shmashine.pm.api.service.installingTask.TblInstallingTaskService;
import com.shmashine.pm.api.service.pmImage.TblPmImageService;
import com.shmashine.pm.api.service.sensorConfig.TblSensorConfigService;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.util.PojoConvertUtil;

/**
 * 安装单接口
 */
@RequestMapping("installingBill")
@RestController
public class InstallBillController extends BaseRequestEntity {

    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private TblInstallingBillService tblInstallingBillService;
    @Autowired
    private BizInstallingBillService bizInstallingBillService;
    @Autowired
    private TblInstallingTaskService tblInstallingTaskService;
    @Autowired
    private TblPmImageService tblPmImageService;
    @Autowired
    private TblElevatorService tblElevatorService;
    @Autowired
    private TblDeviceService tblDeviceService;
    @Autowired
    private TblSensorConfigService tblSensorConfigService;
    @Autowired
    private TblDeviceSensorService tblDeviceSensorService;

    /**
     * 安装单任务
     *
     * @param module
     * @return
     */
    @PostMapping("/searchList")
    public Object getList(@RequestBody InstallingBillModule module) {
        return ResponseResult.successObj(bizInstallingBillService.selectByBillModule(module));
    }

    @PostMapping("/getDetailInfo")
    public Object getDetailInfo(@RequestBody TblInstallingBill tblInstallingBill) {
        return ResponseResult.successObj(bizInstallingBillService.getBizInfoById(tblInstallingBill.getvInstallingBillId()));
    }

    //    @Transactional(rollbackFor = Exception.class)
    @PostMapping("editInstallingBill")
    public Object editInstallingBill(@RequestBody TblInstallingBillDto tblInstallingBillDto) {

        TblInstallingBill tblInstallingBill = PojoConvertUtil.convertPojo(tblInstallingBillDto, TblInstallingBill.class);
        TblInstallingTask tblInstallingTask = tblInstallingTaskService.getById(tblInstallingBill.getvInstallingTaskId());

        if (tblInstallingTask.getiStatus() == TblInstallingTaskStatusEnum.Installed.getValue()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg10_01");
        }

        tblInstallingBill.setvVillageId(tblInstallingTask.getvVillageId());
        tblInstallingBill.setiStatus(TblInstallingBillStatusEnum.Done.getValue());

        int success = tblInstallingBillService.update(tblInstallingBill);

        if (success > 0) {
            // 更新电梯状态
            TblElevator tblElevator = tblElevatorService.getByElevatorCode(tblInstallingBill.getvElevatorCode());
            tblElevator.setiPmStatus(TblVillageStatusEnum.Testing.getValue());
            tblElevatorService.update(tblElevator);

            // 图片相关操作
            tblPmImageService.deleteByTargetId(tblInstallingBill.getvInstallingBillId());
            List<TblPmImage> carInsideImages = tblInstallingBillDto.getCarInsideImages();
            List<TblPmImage> machRoomBoxImages = tblInstallingBillDto.getMachRoomBoxImages();
            List<TblPmImage> carRoofBoxImages = tblInstallingBillDto.getCarRoofBoxImages();
            List<TblPmImage> images = Stream.concat(carInsideImages.stream(), machRoomBoxImages.stream()).collect(Collectors.toList());

            images = Stream.concat(images.stream(), carRoofBoxImages.stream()).filter(img -> img.getvPmImageId() == null).collect(Collectors.toList());
            images.forEach(item -> {
                String imageId = SnowFlakeUtils.nextStrId();
                item.setvPmImageId(imageId);
                item.setvTargetId(tblInstallingBillDto.getvInstallingBillId());
            });

            if (images.size() > 0) {
                tblPmImageService.insertBatch(images);
            }

            InstallingBillModule module = new InstallingBillModule();
            module.setvInstallingTaskId(tblInstallingTask.getvInstallingTaskId());
            List<Integer> installingStatuses = bizInstallingBillService.getAllStatus(module);

            installingStatuses = installingStatuses.stream().filter(is -> is != TblInstallingBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

            if (installingStatuses.stream().allMatch(st -> st == TblInstallingBillStatusEnum.Done.getValue())) {
                tblInstallingTask.setiStatus(TblInstallingTaskStatusEnum.Installed.getValue());
            } else {
                tblInstallingTask.setiStatus(TblInstallingTaskStatusEnum.Installing.getValue());
            }

            tblInstallingTaskService.update(tblInstallingTask);

            TblDevice deviceSearch = new TblDevice();
            deviceSearch.setVElevatorCode(tblInstallingBill.getvElevatorCode());
            List<TblDevice> tblDevices = tblDeviceService.selectByEntity(deviceSearch);

            for (TblDevice device : tblDevices) {
                String sensorType = "";

                if (device.geteType().equals("MX301"))
                    sensorType = BusinessConstants.CAR_DOOR;
                else
                    sensorType = device.getVSensorType();

                List<TblSensorConfig> tblSensors = tblSensorConfigService.selectBySensorType(sensorType);
//

                TblDeviceSensor tblDeviceSensorSearchModule = new TblDeviceSensor();
                tblDeviceSensorSearchModule.setvElevatorCode(tblInstallingBillDto.getvElevatorCode());
                List<TblDeviceSensor> list = tblDeviceSensorService.selectByEntity(tblDeviceSensorSearchModule);

                List<TblDeviceSensor> tblDeviceSensors = new ArrayList<>();

                for (TblSensorConfig tblSensorConfig : tblSensors) {
                    TblDeviceSensor tblDeviceSensor = null;
                    Optional<TblDeviceSensor> first = list.stream()
                            .filter(item -> item.getvSensorConfigId().equals(tblSensorConfig.getvSensorConfigId()))
                            .findFirst();
                    if (first.isPresent()) {
                        tblDeviceSensor = first.get();
                    } else {
                        tblDeviceSensor = new TblDeviceSensor();

                        tblDeviceSensor.setvDeviceSensorId(SnowFlakeUtils.nextStrId());
                        tblDeviceSensor.setvElevatorCode(tblInstallingBill.getvElevatorCode());
                        tblDeviceSensor.setvSensorConfigId(tblSensorConfig.getvSensorConfigId());
                        tblDeviceSensor.setDtCreateTime(new Date());
                        tblDeviceSensor.setvCreateUserId(getUserId());
                        tblDeviceSensor.setvDeviceId(device.getVDeviceId());
                    }
                    tblDeviceSensor.setDtModifyTime(new Date());
                    tblDeviceSensor.setvModifyUserId(getUserId());

                    // Todo 数据结构后面优化
                    switch (tblSensorConfig.getvSensorConfigId()) {
                        case "1":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomOverhaulSensor() == null ? 0 : Integer.min(1, tblInstallingBill.getiMachRoomOverhaulSensor()));
                            break;
                        case "2":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomSwitchSensor() == null ? 0 : tblInstallingBill.getiMachRoomOverhaulSensor());
                            break;
                        case "3":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomSwitchCircuitSensor() == null ? 0 : tblInstallingBill.getiMachRoomSwitchCircuitSensor());
                            break;
                        case "4":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomHallCircuitSensor() == null ? 0 : tblInstallingBill.getiMachRoomHallCircuitSensor());
                            break;
                        case "5":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomCircuitSensor() == null ? 0 : tblInstallingBill.getiMachRoomCircuitSensor());
                            break;
                        case "6":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomCallSensor() == null ? 0 : tblInstallingBill.getiMachRoomCallSensor());
                            break;
                        case "7":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomServiceSensor() == null ? 0 : tblInstallingBill.getiMachRoomServiceSensor());
                            break;
                        case "8":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMachRoomElectricitySensor() == null ? 0 : tblInstallingBill.getiMachRoomElectricitySensor());
                            break;
                        case "9":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiTempSensor() == null ? 0 : tblInstallingBill.getiTempSensor());
                            break;
                        case "10":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiMagnetSensor() == null ? 0 : tblInstallingBill.getiMagnetSensor());
                            break;
                        case "11":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiFloorSensor() == null ? 0 : tblInstallingBill.getiFloorSensor());
                            break;
                        case "12":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiCollateFloorSensor() == null ? 0 : tblInstallingBill.getiCollateFloorSensor());
                            break;
                        case "13":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiCarroofDoorSensor() == null ? 0 : tblInstallingBill.getiCarroofDoorSensor());
                            break;
                        case "14":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiCarroofLockSensor() == null ? 0 : tblInstallingBill.getiCarroofLockSensor());
                            break;
                        case "15":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiBodySensor() == null ? 0 : tblInstallingBill.getiBodySensor());
                            break;
                        case "16":
                            tblDeviceSensor.setiSensorChose(tblInstallingBill.getiCamera() == null ? 0 : tblInstallingBill.getiCamera());
                            break;
                    }
                    tblDeviceSensors.add(tblDeviceSensor);
                }
                if (list == null || list.size() == 0) {
                    tblDeviceSensorService.batchInsert(tblDeviceSensors);
                } else {
                    tblDeviceSensors.forEach(record -> tblDeviceSensorService.update(record));
                }
            }

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("安装单编辑失败"));
        }
    }

    @PostMapping("/checkerVerifyCode")
    public Object checkerVerifyCode(@RequestBody TblInstallingBill installingBill) {
        TblInstallingBill tblInstallingBill = tblInstallingBillService.getById(installingBill.getvInstallingBillId());

        if (tblInstallingBill.getvVerifyCode().equals(installingBill.getvVerifyCode())) {
            return ResponseResult.successObj("1");
        } else {
            return ResponseResult.successObj("0");
        }
    }

    /**
     * 上传图片
     */

    @PostMapping("/uploadImage")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseResult.successObj(OSSUtil.savePmTaskFile(file));
    }

    /**
     * 删除图片
     *
     * @param vInstallingBillImageId
     * @return
     */
    @PostMapping("/deleteImage")
    public Object deleteFile(String vInstallingBillImageId) {
        return ResponseResult.successObj(tblPmImageService.deleteById(vInstallingBillImageId));
    }

    /**
     * 取消单子
     *
     * @return
     */
    @PostMapping("/cancelBill")
    @Transactional(rollbackFor = Exception.class)
    public Object cancelBill(@RequestBody TblInstallingBillDto tblInstallingBillDto) {

        TblInstallingBill tblInstallingBill = tblInstallingBillService.getById(tblInstallingBillDto.getvInstallingBillId());

        if (tblInstallingBill.getiStatus() == TblInstallingTaskStatusEnum.InstallLess.getValue()) {

            tblInstallingBill.setiStatus(TblInstallingTaskStatusEnum.Canceled.getValue());
            tblInstallingBillService.update(tblInstallingBill);

            TblElevator tblElevator = tblElevatorService.getByElevatorCode(tblInstallingBill.getvElevatorCode());
            if (tblElevator.getIInstallStatus() == 0) {
                tblElevatorService.deleteById(tblElevator.getVElevatorId());
            } else {
                tblElevator.setiPmStatus(TblVillageStatusEnum.InstallLess.getValue());
                tblElevatorService.update(tblElevator);
            }

            TblInstallingTask tblInstallingTask = tblInstallingTaskService.getById(tblInstallingBill.getvInstallingTaskId());

            InstallingBillModule installingBillModule = new InstallingBillModule();
            installingBillModule.setvInstallingTaskId(tblInstallingBill.getvInstallingTaskId());
            List<Integer> statuses = bizInstallingBillService.getAllStatus(installingBillModule);

            statuses = statuses.stream().filter(st -> st != TblInstallingBillStatusEnum.Canceled.getValue()).collect(Collectors.toList());

            tblInstallingTask.setiElevatorCount(statuses.size());

            if (statuses.size() == 0) {
                tblInstallingTask.setiStatus(TblInstallingTaskStatusEnum.Canceled.getValue());
            } else {
                if (statuses.stream().allMatch(st -> st == TblInstallingBillStatusEnum.Done.getValue())) {
                    tblInstallingTask.setiStatus(TblInstallingTaskStatusEnum.Installed.getValue());
                }
            }

            tblInstallingTaskService.update(tblInstallingTask);

            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error("只有未安装的单子才能取消"));
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
