package com.shmashine.api.controller.elevator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import cn.dev33.satoken.annotation.SaIgnore;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.controller.elevator.VO.ElevatorRunDataStatisticsReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingConfigReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingReqVO;
import com.shmashine.api.controller.elevator.VO.InsertImageRecognitionMattingConfigListReqVO;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.TblDeviceDao;
import com.shmashine.api.entity.ElevatorForExcel;
import com.shmashine.api.entity.SysUserResourceForExcel;
import com.shmashine.api.entity.TblElevatorExtInfo;
import com.shmashine.api.entity.TblElevatorPrincipal;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.ElevatorBatchAddModule;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.module.elevator.ElevatorResourceModule;
import com.shmashine.api.module.elevator.ResolveExcelForElevator;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.elevator.UpdateFloorElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.redis.RedisService;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.elevator.TblElevatorExtInfoService;
import com.shmashine.api.service.elevatorbrand.TblElevatorBrandServiceI;
import com.shmashine.api.service.elevatorexcel.ElevatorExcelServiceI;
import com.shmashine.api.service.elevatorexcel.ElevatorExcelTelecomDigitalSetReadListener;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.api.service.file.BizFileService;
import com.shmashine.api.service.file.TblSysFileServiceI;
import com.shmashine.api.service.system.TblDeviceServiceI;
import com.shmashine.api.service.system.TblElevatorServiceI;
import com.shmashine.api.service.system.TblSysUserResourceServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.util.ExceExport2;
import com.shmashine.api.util.PojoConvertUtil;
import com.shmashine.cameratysl.client.RemoteCameraTyslClient;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.constants.CameraConstants;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.dto.ElevatorExcelTelecomDigitalSetDTO;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblSysDept;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.utils.CameraUtils;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.POIUtils;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.haierCamera.client.RemoteHaierCameraClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 电梯基本接口
 *
 * @author Liulifu
 */
@Slf4j
@RestController
@RequestMapping("/elevator")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@Tag(name = "电梯接口", description = "电梯接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class ElevatorController extends BaseRequestEntity {

    private final ElevatorExcelServiceI elevatorExcelService;
    private final BizElevatorService elevatorService;
    private final BizUserService bizUserService;
    private final BizFileService bizFileService;
    private final BizProjectService bizProjectService;
    private final BizDeptService bizDeptService;
    private final TblElevatorServiceI tblElevatorServiceI;
    private final TblVillageServiceI tblVillageService;
    private final TblSysFileServiceI tblSysFileServiceI;
    private final TblDeviceServiceI deviceService;
    private final TblSysUserResourceServiceI tblSysUserResourceService;
    private final TblElevatorBrandServiceI tblElevatorBrandServiceI;
    private final TblElevatorExtInfoService tblElevatorExtInfoService;
    private final RedisService redisService;

    private final RemoteHaierCameraClient remoteHaierCameraClient;
    private final RemoteCameraTyslClient tyslCameraClient;

    private final RedisUtils redisUtils;
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;

    private final TblDeviceDao tblDeviceDao;
    private final BizElevatorDao bizElevatorDao;


    /**
     * 获取电梯统计信息
     *
     * @param leaveFactoryNumber
     * @param equipmentCode
     * @return
     */
    @GetMapping("/getElevatorStatisticsInfo")
    public Object getElevatorStatisticsInfo(String leaveFactoryNumber,
                                            String equipmentCode) {

        if (!StringUtils.hasText(leaveFactoryNumber) && !StringUtils.hasText(equipmentCode)) {
            return ResponseResult.resultValid("出厂编号和注册码不能都为空");
        }
        var elevatorStatisticsInfo = elevatorService.getElevatorStatisticsInfo(leaveFactoryNumber, equipmentCode);
        return ResponseResult.successObj(elevatorStatisticsInfo);
    }

    /**
     * 获取电梯客流量
     *
     * @param elevatorCode
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/getElevatorPassengerFlow")
    public Object getElevatorPassengerFlow(String elevatorCode,
                                           String startTime,
                                           String endTime) {


        var elevatorRunCounts = elevatorService.getElevatorPassengerFlow(elevatorCode, startTime, endTime);
        return ResponseResult.successObj(elevatorRunCounts);
    }

    /**
     * 删除电梯信息
     *
     * @param elevatorCode
     * @return
     */
    @GetMapping("/deleteElevatorId/{elevatorCode}")
    public Object deleteElevatorInfo(@PathVariable("elevatorCode") String elevatorCode) {
        elevatorService.deleteElevatorInfoByElevatorCode(elevatorCode);
        return ResponseResult.success();
    }

    /**
     * 删除电梯统计数据和故障数据
     *
     * @param elevatorCode
     * @return
     */
    @PostMapping("/delFaultAndStatisticsByElevatorCode")
    public ResponseResult delFaultAndStatisticsByElevatorCode(@RequestParam("elevatorCode") String elevatorCode) {
        return elevatorService.delFaultAndStatisticsByElevatorCode(elevatorCode);
    }

    /**
     * 电梯列表接口(分页数据)
     *
     * @param searchElevatorModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @Operation(summary = "电梯列表接口(分页数据)", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchElevator")
    public Object search(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        PageListResultEntity userMenu = elevatorService.searchElevatorList(searchElevatorModule);
        return ResponseResult.successObj(userMenu);
    }

    /**
     * 添加电梯负责人
     *
     * @param elevatorPrincipals
     * @return
     */
    @PostMapping("/addElevatorPrincipal")
    public ResponseResult addElevatorPrincipal(@RequestBody List<TblElevatorPrincipal> elevatorPrincipals) {

        //添加负责人
        elevatorService.addElevatorPrincipal(elevatorPrincipals);
        return ResponseResult.success();
    }

    /**
     * 获取该梯的负责人
     *
     * @param elevatorId
     * @return
     */
    @GetMapping("/queryElevatorPrincipal/{elevatorId}")
    public ResponseResult queryElevatorPrincipal(@PathVariable("elevatorId") String elevatorId) {
        var principals = bizUserService.queryElevatorPrincipal(elevatorId);
        return ResponseResult.successObj(principals);
    }


    /**
     * 电梯列表接口(根据部门进行分页数据，并且已经对该账号授权)
     *
     * @param searchElevatorModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @Operation(summary = "电梯列表接口(根据部门进行分页数据，并且已经对该账号授权)", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchElevatorByUserId")
    public Object searchElevatorByUserId(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        PageListResultEntity userMenu = elevatorService.searchElevatorListByDeptId(searchElevatorModule);
        return ResponseResult.successObj(userMenu);
    }

    //获取小区电梯授权情况
    @PostMapping("/searchVillageUserElevators/{userId}/{villageId}")
    public Object searchVillageUserElevators(@PathVariable("userId") String userId, @PathVariable("villageId") String villageId) {
        return ResponseResult.successObj(elevatorService.searchVillageUserElevators(bizUserService.isAdmin(super.getUserId()), getUserId(), userId, villageId));
    }

    /**
     * 获取用户授权电梯列表
     */
    @PostMapping("/searchUserElevators/{userId}")
    public Object searchUserElevators(@PathVariable("userId") String userId) {
        return ResponseResult.successObj(elevatorService.searchUserElevators(bizUserService.isAdmin(super.getUserId()), getUserId(), userId));
    }

    /**
     * 微信小程序，手动设置检修状态
     *
     * @param elevatorICode 电梯code
     * @param status        状态 1：开启检修，0：关闭检修
     * @return
     */
    @PostMapping("/setElevatorStatus/{elevatorICode}/{status}/{time}")
    public ResponseResult setElevatorStatus(@PathVariable("elevatorICode") String elevatorICode, @PathVariable("status") Integer status, @PathVariable("time") float time) {
        //设置电梯检修状态
        try {
            if (status == 1) {
                //设置电梯检修状态
                redisService.setElevatorStatus(elevatorICode, status, time);
            }
            if (status == 0) {
                //设置检修状态120s后失效
                redisService.setElevatorStatusTimeOut(elevatorICode);
            }
            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error();
        }
    }


    /**
     * 维保平台，签到签退设置检修状态
     *
     * @param registrationCode 电梯注册码
     * @param status           状态 1：开启检修，0：关闭检修
     */
    @SaIgnore
    @PostMapping("/maintenance-platform/setElevatorStatus/{registrationCode}/{status}/{time}")
    public ResponseResult setElevatorStatusWithMaintenancePlatForm(
            @PathVariable("registrationCode") String registrationCode,
            @PathVariable("status") Integer status,
            @PathVariable("time") float time) {

        try {

            log.info("维保平台 -  请求设置检修状态，registrationCode:{},status:{},time:{}", registrationCode, status, time);

            //根据电梯注册码获取电梯
            ElevatorDetailModule elevator = elevatorService.getElevatorByEquipmentCode(registrationCode);

            if (elevator == null || !StringUtils.hasText(elevator.getElevatorCode())) {
                log.info("维保平台 -  设置检修状态 - 电梯不存在，registrationCode:{},status:{},time:{}", registrationCode, status, time);
                return ResponseResult.error();
            }

            //设置电梯检修状态
            if (status == 1) {
                //设置电梯检修状态
                redisService.setElevatorStatus(elevator.getElevatorCode(), status, time);
            }
            if (status == 0) {
                //设置检修状态120s后失效
                redisService.setElevatorStatusTimeOut(elevator.getElevatorCode());
            }
            log.error("维保平台 - 设置检修状态成功，registrationCode:{},status:{},time:{}", registrationCode, status, time);
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("维保平台 - 设置检修状态失败，registrationCode:{},status:{},time:{},error:{}",
                    registrationCode, status, time, ExceptionUtils.getStackTrace(e));
            return ResponseResult.error();
        }
    }

    /**
     * 微信小程序，获取手动检修状态
     *
     * @param elevatorICode
     * @return
     */
    @GetMapping("/getElevatorStatus/{elevatorICode}")
    public ResponseResult getElevatorStatus(@PathVariable("elevatorICode") String elevatorICode) {
        return ResponseResult.successObj(redisService.getElevatorStatus(elevatorICode));
    }


    /**
     * 电梯列表（不分页）
     *
     * @param searchElevatorModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/searchElevatorNotPage")
    public Object searchElevatorNotPage(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        var maps = elevatorService.searchElevatorListNoPage(searchElevatorModule);

        // 扩展小区信息
        tblVillageService.extendVillageInfo(maps);

        return ResponseResult.successObj(maps);
    }

    /**
     * 根据参数，导出Excel文件
     *
     * @param searchElevatorModule
     * @param response
     */
    @RequestMapping("/test")
    public void testExprotExcel(@RequestBody SearchElevatorModule searchElevatorModule, HttpServletResponse response) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());

        //创建一个数组用于设置表头
        String[] arr = new String[]{"注册编号", "编号", "省", "市", "区", "项目名称", "小区", "地址", "维保公司", "电梯类型",
                "安装状态", "在线状态", "故障状态", "服务模式", "上盒软件版本", "上盒硬件版本", "上盒固件版本号",
                "下盒软件版本", "下盒硬件版本", "下盒固件版本号"};

        //调用Excel导出工具类
        var elevatorDetailDownloadModuleMaps = elevatorService.searchElevatorListDownload(searchElevatorModule);
        ExceExport2.export(response, elevatorDetailDownloadModuleMaps, arr);

    }

    /**
     * 批量导入电梯excel模板表下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/filesdownloads")
    public void downloadLocal(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //String filePath = "D:\\Document\\maixinProject\\elevator_mario\\api\\src\\main\\resources\\resource\\批量添加电梯模板表.xlsx";
        String filePath = "resource/批量添加电梯模板表.xlsx";
        String filename = "批量添加电梯模板表.xlsx";

        //下载文件中文名乱码
        if (request.getHeader("User-Agent").contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        /*} else if (request.getHeader("User-Agent").contains("Firefox")) {
            // 火狐浏览器
            BASE64Encoder base64Encoder = new BASE64Encoder();
            filename = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";*/
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

        try {
            //打开本地文件流
            InputStream inputStream = new FileInputStream(filePath);
            //激活下载操作
            OutputStream os = response.getOutputStream();

            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            //关闭流
            os.close();
            inputStream.close();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 上传excel文件，批量添加电梯
     *
     * @param deviceMark 设备标识《0：后装设备；1：前装设备》
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/excelResolve/{deviceMark}")
    public ResponseResult excelResolve(@PathVariable String deviceMark, @RequestParam("excelfile") MultipartFile file) throws Exception {

        //拿到用户所关联的部门，若不存在则不予添加，提示先添加项目
        TblSysDept dept = bizElevatorDao.getDeptByUserId(super.getUserId());
        // 部门不存在时直接返回
        if (null == dept || !StringUtils.hasText(dept.getVDeptId())) {
            //该账号不存在部门，请先关联部门
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_01");
        }
        //1递归查询用户关联的项目list
        List<String> projectIdList = bizProjectService.getProjectIdsByParentDeptId(dept.getVDeptId());
        // 部门不存在时直接返回
        if (projectIdList.size() == 0) {
            //该账号无关联项目，请先添加项目
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_02");
        }

        //解析上传的文件
        ResolveExcelForElevator resolveExcelForElevator;
        try {
            List<String[]> lists = POIUtils.readExcel(file);
            resolveExcelForElevator = StringTOList(lists, projectIdList, super.getUserId(), deviceMark);
        } catch (IOException e) {
            e.printStackTrace();
            //上传文件解析失败，请使用提供的模板进行添加
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_03");
        }

        if (resolveExcelForElevator.getElevatorList().size() > 0) {
            //添加电梯表
            elevatorService.addElevatorByExcel(resolveExcelForElevator.getElevatorList());
            //对添加成功的梯进行授权
            tblSysUserResourceService.addElevatorByExcel(resolveExcelForElevator.getSysUserResourceList());
            //添加设备表
            deviceService.insertBatch(resolveExcelForElevator.getAddDeviceList());
        }

        if (resolveExcelForElevator.getFailElevatorList().size() > 1) {
            //部分电梯添加不成功，请检查项目是否存在或电梯Code是否重复
            return new ResponseResult(ResponseResult.CODE_OK, "msg9_05", resolveExcelForElevator.getFailElevatorList());
        }
        //添加成功
        return ResponseResult.success();
    }


    /**
     * 根据ID,获取电梯详情
     *
     * @param elevatorId ID
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.elevator.ElevatorDetailModule#
     */
    @GetMapping("/{elevatorId}")
    public Object getElevatorDetail(@PathVariable("elevatorId") @Valid @NotEmpty(message = "请输入电梯唯一标识") String elevatorId) {

        //根据电梯id或者电梯电梯统一的注册编号来查询电梯表
        var elevator = elevatorService.getElevatorByElevatorId(elevatorId);
        // 补全视频token信息
        try {
            if (elevator.getCameraType() != null && elevator.getCameraType() == CameraConstants.CameraType.HAIKANG_YS.getType()) {
                // 摄像头类型 1：海康，2：雄迈   海康萤石云需要token访问
                //            elevator.setToken(bizCameraService.getEzopenToken());
                //失效时间2分钟
                String haikangHlsUrl = getHaikangHlsUrl(elevator.getCloudNumber(), 120);
                if (haikangHlsUrl != null) {
                    elevator.setHlsUrl(haikangHlsUrl);
                }

            }

            if (elevator.getCameraType() != null && elevator.getCameraType() == CameraConstants.CameraType.XIONGMAI.getType()) {
                // 摄像头类型 1：海康，2：雄迈   雄迈hls流获取
                String hlsUrl = CameraUtils.getXiongMaiHls(elevator.getCloudNumber(), elevator.getElevatorCode());
                elevator.setHlsUrl(hlsUrl);
            }

            //海尔摄像头
            if (elevator.getCameraType() != null && elevator.getCameraType() == CameraConstants.CameraType.HAIER.getType()) {
                // 摄像头类型 1：海康，2：雄迈，3：海尔
                String rest = remoteHaierCameraClient.getCameraHlsUrlByElevatorId(elevatorId);
                JSONObject messageJSON = JSONObject.parseObject(rest);
                JSONObject data = messageJSON.getJSONObject("data");
                String hlsUrl = data.getString("playUrl");
                elevator.setHlsUrl(hlsUrl);
            }
            // 天翼视联（天翼云眼，中兴）
            if (elevator.getCameraType() != null
                    && (CameraConstants.CameraType.TYYY.getType() == elevator.getCameraType()
                    || CameraConstants.CameraType.TYBD.getType() == elevator.getCameraType())) {
                var hlsUrlRes = tyslCameraClient.getCameraStreamUrl(elevator.getElevatorCode(), "HLS");
                if (hlsUrlRes != null && HttpStatus.OK.value() == hlsUrlRes.getCode()) {
                    elevator.setHlsUrl(hlsUrlRes.getData());
                }
            }
        } catch (Exception e) {
            log.error("获取摄像头地址失败，error：{}", e.getMessage());
        }

        return ResponseResult.successObj(elevator);
    }

    /**
     * 获取海康hls播放流地址
     *
     * @param vCloudNumber 摄像头序列号
     * @param expireTime   失效时间 s
     * @return
     */
    private String getHaikangHlsUrl(String vCloudNumber, int expireTime) {

        String url = redisUtils.get(RedisConstants.CAMERA_HAIKANG_HLSURL + vCloudNumber);
        if (!StringUtils.hasText(url) || "null".equals(url)) {
            url = CameraUtils.getHaiKangUrl(vCloudNumber, expireTime);
            //redis缓存海康hlsUrl
            redisUtils.set(RedisConstants.CAMERA_HAIKANG_HLSURL + vCloudNumber, url, expireTime);
        }
        return url;
    }

    /**
     * 根据ID,获取电梯详情接口（全字段返回）
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.elevator.ElevatorDetailResModule#
     */
    @GetMapping("/getElevatorInfo/{elevatorId}")
    public Object getElevatorInfo(@PathVariable("elevatorId") @Valid @NotEmpty(message = "请输入电梯唯一标识") String elevatorId) {
        var elevatorDetail = elevatorService.getElevatorDetail(elevatorId);
        return ResponseResult.successObj(elevatorDetail);
    }

    /**
     * 统计电梯各个状态的数量
     *
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/countElevator")
    public Object countElevator(@RequestParam(value = "projectIds", required = false) String projectIdsString,
                                @RequestParam(value = "villageIds", required = false) String villageIdsString) {
        // 不可使用 stream 的 collect, jdk 不兼容
        var projectIds = new ArrayList<String>();
        if (StringUtils.hasText(projectIdsString)) {
            Arrays.stream(projectIdsString.split(",")).map(String::trim)
                    .forEach(projectIds::add);
        }
        var villageIds = new ArrayList<String>();
        if (StringUtils.hasText(villageIdsString)) {
            Arrays.stream(villageIdsString.split(",")).map(String::trim)
                    .forEach(villageIds::add);
        }
        String userId = super.getUserId();
        var map = elevatorService.countElevator(userId, bizUserService.isAdmin(userId), projectIds, villageIds);
        return ResponseResult.successObj(map);
    }

    /**
     * 调用python生成log波形图
     *
     * @return
     */
    @GetMapping("/logPicture/{elevatorCode}/{logUrl}")
    public ResponseResult getDlog(@PathVariable("elevatorCode") String elevatorCode, @PathVariable("logUrl") String logUrl) {
        // 请求路径：
        String url = "http://47.104.215.210:2017/" + elevatorCode + "/" + logUrl;
        try {
            restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            String message = e.getMessage();
            e.printStackTrace();
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_06");
        }
        return ResponseResult.success();
    }

    /**
     * 电梯图片获取
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblSysFile#
     */
    @PostMapping("/getElevatorImg/{elevatorId}")
    public Object getElevatorImg(@PathVariable("elevatorId") @Valid @NotEmpty(message = "请输入电梯唯一标识") String elevatorId) {
        var fileElevatorImg = bizFileService.getFileElevatorImg(elevatorId);
        return ResponseResult.successObj(fileElevatorImg);
    }

    /**
     * 获取电梯设备列表
     *
     * @param elevatorId 电梯id
     * @Return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/deviceList/{elevatorId}")
    public Object getElevatorDeviceList(@PathVariable("elevatorId") String elevatorId) {
        List<String> deviceList = elevatorService.getElevatorDeviceList(elevatorId);
        return ResponseResult.successObj(deviceList);
    }


    /**
     * 获取电梯设备列表详情
     *
     * @param elevatorId 电梯id
     * @Return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/deviceDetailList/{elevatorId}")
    public Object getElevatorDeviceDetailList(@PathVariable("elevatorId") String elevatorId) {
        List<TblDevice> deviceList = elevatorService.getElevatorDeviceDetailList(elevatorId);
        return ResponseResult.successObj(deviceList);
    }

    /**
     * 对外电梯列表（不反回一些敏感字段信息）
     *
     * @param searchElevatorModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity,com.shmashine.api.module.elevator.ElevatorThirdPartyList#
     */
    @PostMapping("/thirdParty/searchElevator")
    public Object thirdPartySearchElevator(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        PageListResultEntity pageListResultEntity = elevatorService.searchElevatorThirdPartyList(searchElevatorModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 对外通过注册编号获取电梯详情(不反回一些敏感字段信息)
     *
     * @param equipmentCode
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.elevator.ElevatorThirdPartyDetail#
     */
    @GetMapping("/thirdParty/getElevatorDetail/{equipmentCode}")
    public Object thirdPartyGetElevator(@PathVariable @Valid @NotNull(message = "请输入电梯社会统一编号") String equipmentCode) {
        var elevatorThirdPartyDetail = elevatorService.getElevatorThirdPartyDetail(equipmentCode);
        if (elevatorThirdPartyDetail != null && elevatorThirdPartyDetail.getInstallTime() != null) {
            var installTime = elevatorThirdPartyDetail.getInstallTime();
            var date = new Date();
            int dateInt = (int) ((date.getTime() - installTime.getTime()) / (1000 * 3600 * 24));
            elevatorThirdPartyDetail.setCumulativeOperationDays(String.valueOf(dateInt));
        }

        return ResponseResult.successObj(elevatorThirdPartyDetail);
    }

    /**
     * 编辑电梯
     *
     * @param tblElevator
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editElevatorInfo")
    public Object editElevatorInfo(@RequestBody @Valid TblElevator tblElevator) {
        int update = elevatorService.update(tblElevator);
        if (update == 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg5_06"));
        }
        return ResponseResult.success();
    }

    /**
     * 添加电梯
     *
     * @param tblElevator
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addElevatorInfo")
    public Object addElevatorInfo(@RequestBody @Valid TblElevator tblElevator) {

        List<String> codes = new ArrayList<>();
        codes.add(tblElevator.getVElevatorCode());
        List<String> existCodes = tblElevatorServiceI.checkExistsByCodes(codes);
        if (null != existCodes && !existCodes.isEmpty()) {
            String existCodesStr = String.join(",", existCodes);
            return ResponseResult.resultValid("电梯编号已存在： " + existCodesStr);
        }

        tblElevator.setVElevatorId(SnowFlakeUtils.nextStrId());
        int insert = tblElevatorServiceI.insertIsNotEmpty(tblElevator);
        if (insert == 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg5_09"));
        }

        List<TblDevice> devices = deviceService.deviceListByElevatorCode(tblElevator.getVElevatorCode());

        if (tblElevator.getEType() != null && tblElevator.getEType().contains("MX301")) {
            tblElevator.setDeviceMark("2");
        }

        //当设备标识为前装的时候
        if ("1".equals(tblElevator.getDeviceMark())) {
            TblDevice front = new TblDevice();
            front.setVDeviceId(SnowFlakeUtils.nextStrId());
            front.setVSensorType(BusinessConstants.FRONT);
            front.setVElevatorId(tblElevator.getVElevatorId());
            front.setVElevatorCode(tblElevator.getVElevatorCode());
            front.seteType(tblElevator.getEType());
            front.setDtCreateTime(new Date());
            deviceService.insert(front);
        } else if ("2".equals(tblElevator.getDeviceMark())) {
            // 设备信息 - 迅达单盒
            TblDevice liftXunDa = new TblDevice();
            liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());
            if (tblElevator.getEType() != null && tblElevator.getEType().contains("MX301")) {
                liftXunDa.setVSensorType(BusinessConstants.CAR_DOOR);
            } else {
                liftXunDa.setVSensorType(BusinessConstants.SENSOR_TYPE_SINGLEBOX);
            }
            liftXunDa.setVElevatorId(tblElevator.getVElevatorId());
            liftXunDa.setVElevatorCode(tblElevator.getVElevatorCode());
            liftXunDa.seteType(tblElevator.getEType());
            liftXunDa.setDtCreateTime(new Date());
            deviceService.insert(liftXunDa);
        } else if ("3".equals(tblElevator.getDeviceMark())) {
            // 设备信息 - 西子扶梯
            TblDevice escalator = new TblDevice();
            escalator.setVDeviceId(SnowFlakeUtils.nextStrId());
            escalator.setVSensorType(BusinessConstants.SENSOR_TYPE_ESCALATOR);
            escalator.setVElevatorId(tblElevator.getVElevatorId());
            escalator.setVElevatorCode(tblElevator.getVElevatorCode());
            escalator.seteType(tblElevator.getEType());
            escalator.setDtCreateTime(new Date());
            deviceService.insert(escalator);
        } else {
            if (devices.size() <= 0 || devices.isEmpty()) {
                // 设备信息 - 轿顶
                TblDevice carRoof = new TblDevice();
                carRoof.setVDeviceId(SnowFlakeUtils.nextStrId());
                carRoof.setVSensorType(BusinessConstants.CAR_ROOF);
                carRoof.setVElevatorId(tblElevator.getVElevatorId());
                carRoof.setVElevatorCode(tblElevator.getVElevatorCode());
                carRoof.setDtCreateTime(new Date());
                carRoof.seteType(tblElevator.getEType());
                deviceService.insert(carRoof);

                // 设备信息 - 机房
                TblDevice motorRoom = new TblDevice();
                motorRoom.setVDeviceId(SnowFlakeUtils.nextStrId());
                motorRoom.setVSensorType(BusinessConstants.MOTOR_ROOM);
                motorRoom.setVElevatorId(tblElevator.getVElevatorId());
                motorRoom.setVElevatorCode(tblElevator.getVElevatorCode());
                motorRoom.setDtCreateTime(new Date());
                motorRoom.seteType(tblElevator.getEType());
                deviceService.insert(motorRoom);
            }
        }

        //添加对应的资源权限绑定
        tblSysUserResourceService.batchByUserId(super.getUserId(), tblElevator);

        return ResponseResult.success();
    }

    /**
     * 添加电梯 - 按项目批量
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/addElevatorsByProject")
    public Object addElevatorsByProject(@RequestBody @Valid ElevatorBatchAddModule elevatorBatchAddModule) {

        // 核对电梯信息
        String projectId = elevatorBatchAddModule.getvProjectId();
        String elevatorCodesStr = elevatorBatchAddModule.getvElevatorCode();
        if (!StringUtils.hasText(projectId) || !StringUtils.hasText(elevatorCodesStr)) {
            return ResponseResult.resultValid("所属项目与电梯编号不能为空 ！");
        }

        List<String> codes = Lists.newArrayList(elevatorCodesStr.split(","));
        // 核对电梯编号是否已经存在
        List<String> existCodes = tblElevatorServiceI.checkExistsByCodes(codes);
        if (null != existCodes && existCodes.size() > 0) {
            String existCodesStr = existCodes.stream().collect(Collectors.joining(","));
            return ResponseResult.resultValid("电梯编号已存在： " + existCodesStr);
        }

        // 批量新增
        tblElevatorServiceI.batchAddElevatorAndDeviceByProject(codes, projectId, elevatorBatchAddModule.getDeviceMark());

        //添加用户——批量授权
        List<TblElevator> elevators = tblElevatorServiceI.listByCodes(codes);
        tblSysUserResourceService.batchaddElevatorsByUserId(getUserId(), elevators);

        return ResponseResult.success();
    }


    /**
     * 根据用户获取 用户的授权电梯
     */
    @RequestMapping("/getUserResourceElevators/{userId}")
    public Object getUserResourceElevators(@PathVariable @Valid @NotNull(message = "请输入电梯编号") String userId) {
        // 获取用户ID
        List<String> codes = tblSysUserResourceService.getElevatorCodesByUserId(userId);
        String codesStr = codes.stream().collect(Collectors.joining(","));
        return ResponseResult.successObj(codesStr);
    }

    /**
     * 用户批量授权电梯
     */
    @PostMapping("/batchUpdateUserResourceElevators")
    public Object batchUpdateUserResourceElevators(@RequestBody @Valid ElevatorResourceModule elevatorBatchAddModule) {
        // 当前操作用户
        String createUserId = getUserId();
        // 待授权的目标用户
        String userId = elevatorBatchAddModule.getUserId();
        // 授权的电梯code
        String elevatorCodesStr = elevatorBatchAddModule.getCodes();
        List<TblElevator> elevators = null;
        if (StringUtils.hasText(elevatorCodesStr)) {
            List<String> codes = Lists.newArrayList(elevatorCodesStr.split(","));
            // 核对电梯编号是否已经存在
            elevators = tblElevatorServiceI.listByCodes(codes);
        }
        tblSysUserResourceService.batchUpdateByUserId(userId, elevators, createUserId);
        return ResponseResult.success();
    }


    /**
     * 设置楼层信息
     *
     * @param updateFloorElevatorModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updateFloorElevator")
    public Object updateFloorElevator(@RequestBody @Valid UpdateFloorElevatorModule updateFloorElevatorModule) {
        // 1.验证楼层
        if (updateFloorElevatorModule.getiMaxFloor() <= updateFloorElevatorModule.getiMinFloor()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg6_01");
        }

        var message = new LinkedHashMap<String, Object>();
        message.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_UPDATE);
        message.put(MessageConstants.MESSAGE_STYPE, MessageConstants.STYPE_LIMIT);
        message.put(MessageConstants.ELEVATOR_CODE, updateFloorElevatorModule.getvElevatorCode());

        message.put("min", updateFloorElevatorModule.getiMinFloor());
        message.put("max", updateFloorElevatorModule.getiMaxFloor());

        message.put("doorInstall", updateFloorElevatorModule.getDoorInstall());

        List<String> deviceList = elevatorService.getElevatorDeviceList(updateFloorElevatorModule.getvElevatorId());

        for (String d : deviceList) {
            if (SocketConstants.SENSOR_TYPE_CAR_ROOF.equals(d)) {
                message.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_CAR_ROOF);
                continue;
            }
            if (SocketConstants.SENSOR_TYPE_SINGLEBOX.equals(d)) {
                message.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_SINGLEBOX);
                continue;
            }
            if (SocketConstants.SENSOR_TYPE_CAR_DOOR.equals(d)) {
                message.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_CAR_DOOR);
                continue;
            }
            if (SocketConstants.SENSOR_TYPE_FRONT.equals(d)) {
                message.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_FRONT);
            }
        }

        //向设备发送命令(设置楼层)
        restTemplateSendMessageToCube(updateFloorElevatorModule.getvElevatorCode(), (String) message.get(MessageConstants.SENSOR_TYPE), JSONObject.toJSONString(message));

        if (updateFloorElevatorModule.getdcSpeed() != null && updateFloorElevatorModule.getdcSpeed() != 0.0) {
            var speedMessage = new LinkedHashMap<String, Object>();
            speedMessage.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_UPDATE);
            speedMessage.put(MessageConstants.MESSAGE_STYPE, MessageConstants.STYPE_ACC);
            speedMessage.put(MessageConstants.ELEVATOR_CODE, updateFloorElevatorModule.getvElevatorCode());
            for (String d : deviceList) {
                if (SocketConstants.SENSOR_TYPE_CAR_ROOF.equals(d)) {
                    speedMessage.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_CAR_ROOF);
                    continue;
                }
                if (SocketConstants.SENSOR_TYPE_SINGLEBOX.equals(d)) {
                    speedMessage.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_SINGLEBOX);
                    continue;
                }
                if (SocketConstants.SENSOR_TYPE_CAR_DOOR.equals(d)) {
                    message.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_CAR_DOOR);
                    continue;
                }
                if (SocketConstants.SENSOR_TYPE_FRONT.equals(d)) {
                    speedMessage.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_FRONT);
                }
            }
            speedMessage.put("value", updateFloorElevatorModule.getdcSpeed());
            // 向设备发送命令(设置电梯额定速度)
            restTemplateSendMessageToCube(updateFloorElevatorModule.getvElevatorCode(), (String) message.get(MessageConstants.SENSOR_TYPE), JSONObject.toJSONString(speedMessage));
        }

        TblElevator tblElevator = PojoConvertUtil.convertPojo(updateFloorElevatorModule, TblElevator.class);
        int update = tblElevatorServiceI.update(tblElevator);

        if (update == 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg6_02"));
        } else {
            if (updateFloorElevatorModule.getDoorInstall() != null && updateFloorElevatorModule.getDoorInstall() != "") {
                TblDevice where = new TblDevice();
                where.setVElevatorId(updateFloorElevatorModule.getvElevatorId());
                TblDevice set = new TblDevice();
                set.setvDevicePosition(updateFloorElevatorModule.getDoorInstall() == null ? "left" : updateFloorElevatorModule.getDoorInstall().toLowerCase());
                tblDeviceDao.updateByField(where, set);
            }
        }

        return ResponseResult.success();
    }

    /**
     * 电梯图片上传
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updateElevatorImg/{elevatorId}")
    public Object updateElevatorImg(@RequestParam("files") MultipartFile[] files, @PathVariable @Valid @NotNull(message = "请输入电梯编号") String elevatorId) {

        // 3. 添加OSS文件
        var maps = OSSUtil.saveElevatorImg(files, elevatorId);
        Date date = new Date();

        // 4. 添加库记录
        for (var item : maps) {
            Object fileName = item.get("fileName");
            Object absoluterUrl = item.get("url");
            TblSysFile fileInsertTemp = new TblSysFile();
            fileInsertTemp.setVFileId(SnowFlakeUtils.nextStrId());
            fileInsertTemp.setVFileType(String.valueOf(0));
            fileInsertTemp.setVFileName(fileName.toString());
            fileInsertTemp.setVUrl(OSSUtil.OSS_URL + absoluterUrl);
            fileInsertTemp.setDtCreateTime(date);
            fileInsertTemp.setDtModifyTime(date);
            fileInsertTemp.setIBusinessType(3);
            fileInsertTemp.setVBusinessId(elevatorId);
            tblSysFileServiceI.insert(fileInsertTemp);
        }

        return ResponseResult.success();
    }

    /**
     * 电梯图片删除
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/deleteElevatorImg/{elevatorId}/{fileId}")
    public Object deleteElevatorImg(@PathVariable @Valid @NotNull(message = "请输入电梯编号") String elevatorId, @PathVariable @Valid @NotNull(message = "请输入需要删除的文件编号") String fileId) {
        // 1. 删除OSS文件
        OSSUtil.deleteElevatorImg(elevatorId);
        // 2. 删除数据库有关文件
        bizFileService.deleteElevatorDetailImages(fileId, elevatorId);
        return ResponseResult.success();
    }


    /**
     * 初始化电梯安装时间（涉及统计中累计运行天数，故障信息 工单 消除）
     *
     * @param elevatorId 电梯id
     */
    @GetMapping("/initInstallTime/{elevatorId}")
    public Object initInstallTime(@PathVariable @Valid @NotNull(message = "请输入电梯编号") String elevatorId) {
        // 删除数据库
        elevatorService.initInstallTime(elevatorId);
        return ResponseResult.success();
    }


    /**
     * 初始化电梯信息
     *
     * @param elevatorId 电梯id
     */
    @GetMapping("/initElevator/{elevatorId}")
    public Object initElevator(@PathVariable @Valid @NotNull(message = "请输入电梯编号") String elevatorId) {
        // 删除数据库
        elevatorService.initElevator(elevatorId);
        return ResponseResult.success();
    }


    @GetMapping("/refresh/camera/{elevatorCode}")
    public Object refreshCamera(@PathVariable @Valid @NotNull(message = "请输入电梯编号") String elevatorCode) {
        redisService.delXiongMaiHlsUrl(elevatorCode);
        return ResponseResult.success();
    }


    /**
     * 获取电梯每日运行数据统计
     *
     * @param reqVO 请求体
     * @return 统计数据
     */
    @SaIgnore
    @PostMapping("/getElevatorRunDataStatistics")
    public ResponseResult getElevatorEventStatistics(@RequestBody ElevatorRunDataStatisticsReqVO reqVO) {

        Integer dataDimension = reqVO.getDataDimension();
        reqVO.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        reqVO.setUserId(getUserId());

        //按时间展示
        if (dataDimension == 1) {
            var result = elevatorService.getRunDataStatisticsByDateDimension(reqVO);
            return ResponseResult.successObj(result);
        } else {
            //按电梯降序展示
            var result = elevatorService.getRunDataStatisticsByElevatorDimension(reqVO);
            return ResponseResult.successObj(result);
        }

    }

    /**
     * 电梯每日运行数据统计-导出
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/exportElevatorRunDataStatistics")
    public void exportElevatorRunDataStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule, HttpServletResponse response) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());

        elevatorService.exportElevatorRunDataStatistics(faultStatisticsModule, response);

    }

    /**
     * 新建电梯拓展信息
     */
    @PostMapping("/insertExtInfo")
    public Object insertExtInfo(@RequestBody TblElevatorExtInfo tblElevatorExtInfo) {
        tblElevatorExtInfo.setvElevatorInfoId(SnowFlakeUtils.nextStrId());
        tblElevatorExtInfoService.insert(tblElevatorExtInfo);
        return ResponseResult.success();
    }

    /**
     * 更新电梯拓展信息
     */
    @PostMapping("/updateExtInfo")
    public Object updateExtInfo(@RequestBody TblElevatorExtInfo tblElevatorExtInfo) {
        tblElevatorExtInfoService.update(tblElevatorExtInfo);
        return ResponseResult.success();
    }

    /**
     * 获取电梯拓展信息
     */
    @PostMapping("/getExtInfo")
    public Object getExtInfo(@RequestBody @Valid TblElevatorExtInfo tblElevatorExtInfo) {
        return ResponseResult.successObj(tblElevatorExtInfoService.getInfoByEntity(tblElevatorExtInfo));
    }

    /**
     * 批量新增电梯图像识别抠图区域配置
     */
    @PostMapping("/insertImageRecognitionMattingConfigList")
    public ResponseResult insertImageRecognitionMattingConfigList(@RequestBody InsertImageRecognitionMattingConfigListReqVO reqVO) {
        return ResponseResult.successObj(elevatorService.insertImageRecognitionMattingConfigList(reqVO, getUserId()));
    }

    /**
     * 配置电梯图像识别抠图区域
     */
    @PostMapping("/insertImageRecognitionMattingConfig")
    public ResponseResult insertImageRecognitionMattingConfig(@RequestBody ImageRecognitionMattingConfigReqVO imageRecognitionMattingConfig) {
        return ResponseResult.successObj(elevatorService.insertImageRecognitionMattingConfig(imageRecognitionMattingConfig, getUserId()));
    }

    /**
     * 获取配置电梯图像识别抠图区域
     */
    @GetMapping("/getImageRecognitionMattingConfig")
    public ResponseResult getImageRecognitionMattingConfig(@RequestParam("elevatorId") String elevatorId, @RequestParam("faultTypes") String faultTypes) {
        return ResponseResult.successObj(elevatorService.getImageRecognitionMattingConfig(elevatorId, faultTypes));
    }

    /**
     * 分页获取配置电梯图像识别抠图区域配置
     */
    @PostMapping("/getImageRecognitionMattingConfigPage")
    public ResponseResult getImageRecognitionMattingConfigPage(@Valid @RequestBody ImageRecognitionMattingReqVO reqVO) {
        return ResponseResult.successObj(elevatorService.getImageRecognitionMattingConfigPage(reqVO));
    }

    /**
     * 电梯二维码图片上传
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updateElevatorQRCodeImg/{elevatorId}")
    public Object updateElevatorQRCodeImg(@RequestParam("files") MultipartFile[] files, @PathVariable @Valid @NotNull(message = "请输入电梯编号") String elevatorId) {

        // 3. 添加OSS文件
        var maps = OSSUtil.saveElevatorImg(files, elevatorId);
        Date date = new Date();

        // 4. 添加库记录
        for (var item : maps) {
            Object fileName = item.get("fileName");
            Object absoluterUrl = item.get("url");
            TblSysFile fileInsertTemp = new TblSysFile();
            fileInsertTemp.setVFileId(SnowFlakeUtils.nextStrId());
            fileInsertTemp.setVFileType(String.valueOf(0));
            fileInsertTemp.setVFileName(fileName.toString());
            fileInsertTemp.setVUrl(OSSUtil.OSS_URL + absoluterUrl);
            fileInsertTemp.setDtCreateTime(date);
            fileInsertTemp.setDtModifyTime(date);
            fileInsertTemp.setIBusinessType(6);
            fileInsertTemp.setVBusinessId(elevatorId);
            tblSysFileServiceI.insert(fileInsertTemp);
        }

        return ResponseResult.success();
    }

    /**
     * 电梯图片获取
     *
     * @param elevatorId
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.TblSysFile#
     */
    @GetMapping("/getElevatorQRCodeImg")
    public Object getElevatorQRCodeImg(@RequestParam("elevatorId") String elevatorId) {
        var fileElevatorImg = bizFileService.getFileElevatorQRCodeImg(elevatorId);
        return ResponseResult.successObj(fileElevatorImg);
    }

    /**
     * @return
     */
    @PostMapping("/updateNextInspectDate")
    public Object updateNextInspectDate(@RequestBody TblElevator elevator) throws ParseException {
        String elevatorCode = elevator.getVElevatorCode();

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date nextInspectDate = fmt.parse(elevator.getDNextInspectDate().toString());

        System.out.println("nextInspectDate" + nextInspectDate);
        return ResponseResult.successObj(elevatorService.updateNextInspectDate(elevatorCode, nextInspectDate));
//        return null;
    }

    /**
     * 电梯导入 如：电信数集 excel， 目前只处理电信数集的项目
     *
     * @param file      excel文件
     * @param projectId 项目ID， 目前只有电信数集
     * @return 结果
     */
    @PostMapping(value = "/import-elevator-excel/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object importElevatorExcelForProject(@RequestParam("file") MultipartFile file,
                                                @PathVariable @Valid @NotNull(message = "请输入项目ID") String projectId) {
        var userId = getUserId();
        if (null == file) {
            return ResponseResult.error("请上传Excel文件");
        }
        if (!StringUtils.hasText(projectId)) {
            return ResponseResult.error("无项目ID");
        }
        // 电信数集 : 8364040583144865792
        if ("8364040583144865792".equals(projectId)) {
            return importElevatorExcelForProjectTelecomDigitalSet(projectId, file, userId);
        }
        return ResponseResult.error("目前只支持“电信数集”的梯导入");
    }


    /**
     * List<String[]>  ==> ResolveExcelForElevator
     */
    private ResolveExcelForElevator StringTOList(List<String[]> list, List<String> projectIdList, String userId, String deviceMark) {

        ResolveExcelForElevator resolveExcelForElevator = new ResolveExcelForElevator();
        //批量添加电梯
        List<ElevatorForExcel> tblElevators = new ArrayList<>();
        //批量添加权限
        List<SysUserResourceForExcel> sysUserResourceList = new ArrayList<>();
        //批量添加设备
        List<TblDevice> addDeviceList = Lists.newArrayList();

        //查询电梯表，获取所有的电梯code（保证全局唯一），判断code是否重复添加
        List<String> eleCodes = elevatorService.searchAllElevatorList();

        //查询维保公司,添加对应的维保公司id
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<Map<String, String>> results = Lists.newArrayList();
        results.add(bizDeptService.getDeptInfo(dept_id));
        recursion(dept_id, results, "1");
        Map<String, String> elevatorMaintain = list2Map(results, "v_dept_name", "v_dept_id");

        //查询电梯品牌,添加对应的品牌id
        List<Map<String, String>> elevatorBrandList = tblElevatorBrandServiceI.getElevatorBrands();
        Map<String, String> elevatorBrands = list2Map(elevatorBrandList, "v_elevator_brand_name", "v_elevator_brand_id");

        //记录添加失败的电梯code
        List<String> faileleCodes = new ArrayList<>();

        for (String[] strings : list) {

            //首先需要判断添加的项目id是否为该账号对应的,还要判断添加的电梯码是否已经注册过
            if (!projectIdList.contains(strings[1]) || eleCodes.contains(strings[0]) || !StringUtils.hasText(strings[0])) {
                faileleCodes.add(strings[0]);
                //结束本次循环，不添加
                continue;
            }

            //雪花算法生成电梯的主键id
            String eleId = SnowFlakeUtils.nextStrId();

            //需要添加的电梯
            tblElevators.add(ElevatorForExcel.builder()
                    //电梯主键id
                    .vElevatorId(eleId)
                    //电梯编码*
                    .vElevatorCode(strings[0])
                    //项目id*
                    .vProjectId(strings[1])
                    //地址
                    .vAddress(strings[2])
                    //电梯品牌
                    .vElevatorBrandId(elevatorBrands.get(strings[3]) == null ? "1274206935163473920" : elevatorBrands.get(strings[3]))
                    //电梯类型
                    .iElevatorType(string2Int(strings[4]))
                    //安装状态
                    .iInstallStatus(string2Int(strings[5]))
                    //维保公司
                    .vMaintainCompanyId(elevatorMaintain.get(strings[6]))
                    //创建人
                    .vCreateUserId(userId)
                    //创建时间
                    .dtCreateTime(new Date())
                    .build());

            //需要对这些梯进行授权
            sysUserResourceList.add(SysUserResourceForExcel.builder()
                    //用户id
                    .vUserId(userId)
                    //资源id
                    .vResourceId(eleId)
                    //电梯code
                    .vResourceCode(strings[0])
                    //创建时间
                    .dtCreateTime(new Date())
                    //创建人
                    .vCreateUserId(userId)
                    .build());

            //添加设备
            if ("1".equals(deviceMark)) {
                // 设备信息 - 前装
                TblDevice front = new TblDevice();
                front.setVDeviceId(SnowFlakeUtils.nextStrId());
                front.setVSensorType(BusinessConstants.FRONT);
                front.setVElevatorId(eleId);
                front.setVElevatorCode(strings[0]);
                front.setDtCreateTime(new Date());
                addDeviceList.add(front);
            } else if ("2".equals(deviceMark)) {
                // 设备信息 - 单盒
                TblDevice liftXunDa = new TblDevice();
                liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());
                liftXunDa.setVSensorType(BusinessConstants.SENSOR_TYPE_SINGLEBOX);
                liftXunDa.setVElevatorId(eleId);
                liftXunDa.setVElevatorCode(strings[0]);
                liftXunDa.setDtCreateTime(new Date());
                deviceService.insert(liftXunDa);
            } else if ("3".equals(deviceMark)) {
                // 设备信息 - 扶梯（单盒）
                TblDevice escalator = new TblDevice();
                escalator.setVDeviceId(SnowFlakeUtils.nextStrId());
                escalator.setVSensorType(BusinessConstants.SENSOR_TYPE_ESCALATOR);
                escalator.setVElevatorId(eleId);
                escalator.setVElevatorCode(strings[0]);
                escalator.setDtCreateTime(new Date());
                deviceService.insert(escalator);
            } else {
                // 设备信息 - 轿顶
                TblDevice carRoof = new TblDevice();
                carRoof.setVDeviceId(SnowFlakeUtils.nextStrId());
                carRoof.setVSensorType(BusinessConstants.CAR_ROOF);
                carRoof.setVElevatorId(eleId);
                carRoof.setVElevatorCode(strings[0]);
                carRoof.setDtCreateTime(new Date());
                addDeviceList.add(carRoof);
                // 设备信息 - 机房
                TblDevice motorRoom = new TblDevice();
                motorRoom.setVDeviceId(SnowFlakeUtils.nextStrId());
                motorRoom.setVSensorType(BusinessConstants.MOTOR_ROOM);
                motorRoom.setVElevatorId(eleId);
                motorRoom.setVElevatorCode(strings[0]);
                motorRoom.setDtCreateTime(new Date());
                addDeviceList.add(motorRoom);
            }

        }
        resolveExcelForElevator.setElevatorList(tblElevators);
        resolveExcelForElevator.setSysUserResourceList(sysUserResourceList);
        resolveExcelForElevator.setFailElevatorList(faileleCodes);
        resolveExcelForElevator.setAddDeviceList(addDeviceList);
        return resolveExcelForElevator;
    }

    /**
     * String ====> Int
     *
     * @param s 数字型字符串
     * @return 数字
     */
    public Integer string2Int(String s) {
        try {
            if (!StringUtils.hasText(s)) {
                return null;
            } else {
                return Integer.valueOf(s);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 递归查询 下级部门的用户
     *
     * @param dept_id
     * @param strings
     */
    public void recursion(String dept_id, List<Map<String, String>> strings, String deptType) {

        if (null != dept_id) {
            List<Map<String, String>> userDeptSelectList = bizUserService.getUserDeptList(dept_id, deptType);
            if (null != userDeptSelectList && userDeptSelectList.size() > 0) {
                userDeptSelectList.forEach(id -> recursion(id.get("v_dept_id"), strings, deptType));
            }
            strings.addAll(userDeptSelectList);
        }
    }

    /**
     * List<map> ====> <map>
     *
     * @param list
     * @return
     */
    public Map<String, String> list2Map(List<Map<String, String>> list, String key, String value) {

        var maps = new HashMap<String, String>();

        list.forEach(it -> {

            String k = null;
            String v = null;

            for (Map.Entry<String, String> map : it.entrySet()) {

                if (key.equals(map.getKey())) {
                    k = String.valueOf(map.getValue());
                }
                if (value.equals(map.getKey())) {
                    v = String.valueOf(map.getValue());
                }
            }
            maps.put(k, v);
        });

        return maps;
    }

    /**
     * 调用socket服务向盒子下发消息
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   传感器类型
     * @param message      消息
     */
    private void restTemplateSendMessageToCube(String elevatorCode, String sensorType, String message) {

        String key = "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
        Map<Object, Object> deviceStatus = redisTemplate.opsForHash().entries(key);
        String nodeIp = String.valueOf(deviceStatus.get("server_ip"));
        nodeIp = nodeIp.replace("47.104.215.210", "172.31.183.100");
        String nodePort = String.valueOf(deviceStatus.get("server_port"));

        // 请求路径：
        String url = String.format("http://%s:%d/cube/sendMessage", nodeIp, Integer.valueOf(nodePort));

        // 发出一个post请求
        restTemplate.postForObject(url, message, String.class);

    }


    /**
     * 电信数集梯导入
     *
     * @param projectId 项目ID
     * @param file      文件
     * @return 结果
     */
    private ResponseResult importElevatorExcelForProjectTelecomDigitalSet(String projectId, MultipartFile file, String userId) {
        try (var inputStream = file.getInputStream()) {
            var elevatorTelecomDigitalSetReadListener = new ElevatorExcelTelecomDigitalSetReadListener(elevatorExcelService, projectId, userId);

            var excelReader = EasyExcel.read(inputStream, ElevatorExcelTelecomDigitalSetDTO.class,
                    elevatorTelecomDigitalSetReadListener).build();
            var readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
            excelReader.close();
            // 获取总行数
            return ResponseResult.successObj(elevatorTelecomDigitalSetReadListener.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

}
