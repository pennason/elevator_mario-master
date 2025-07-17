package com.shmashine.api.controller.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
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
import cn.hutool.core.exceptions.ExceptionUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.device.SearchDeviceFileModule;
import com.shmashine.api.module.device.UploadDeviceFileModule;
import com.shmashine.api.module.device.UploadDeviceFileModuleBatch;
import com.shmashine.api.module.device.UploadFileModule;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.service.device.TblDeviceFileServiceI;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.entity.TblDeviceFile;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.TimeUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备升级文件管理
 *
 * @author little.li
 */

@Slf4j
@RestController
@RequestMapping("/device/file")
@Tag(name = "设备升级文件管理", description = "设备升级文件管理 - powered by chenxue")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DeviceFileController extends BaseRequestEntity {
    private final TblDeviceFileServiceI tblDeviceFileService;
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;
    private final BizElevatorService elevatorService;
    private final BizUserService bizUserService;

    /**
     * 设备软件列表
     *
     * @param deviceFileModule 分页
     */
    @PostMapping("/list")
    public Object list(@RequestBody SearchDeviceFileModule deviceFileModule) {
        var deviceFileList = tblDeviceFileService.searchDeviceFileList(deviceFileModule);
        return ResponseResult.successObj(deviceFileList);
    }


    /**
     * 上传设备升级软件
     *
     * @param file             升级软件
     * @param uploadFileModule 升级明细
     */
    @PostMapping("/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile file, UploadFileModule uploadFileModule) {
        try {
            // 文件存储至服务器
            String uuid = FileUtil.renameToUUID(Objects.requireNonNull(file.getOriginalFilename()));
            String fileUrl = FileUtil.uploadFile(file.getBytes(), MessageConstants.DEVICE_UPLOAD_URL, uuid);
            String crc = FileUtil.getCRC32(file.getInputStream());
            tblDeviceFileService.saveByUploadFile(fileUrl, uuid, crc, uploadFileModule);
        } catch (Exception e) {
            log.error("uploadFile {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
            return new ResponseResult("0001", "msg1_05");
        }
        return ResponseResult.success();
    }

    /**
     * 升级文件信息修改
     */
    @PostMapping("/modifyOrDeleteFileInformation")
    public ResponseResult modifyOrDeleteFileInformation(@RequestBody UploadFileModule uploadFileModule) {

        try {
            tblDeviceFileService.modifyOrDeleteFileInformation(uploadFileModule);
        } catch (Exception e) {
            log.error("modifyOrDeleteFileInformation {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
            //文件信息修改失败
            return new ResponseResult("0001", "msg1_010");
        }
        return ResponseResult.success();
    }


    /**
     * 设备升级
     *
     * @param module 设备信息
     */
    @PostMapping("/uploadFile")
    public Object uploadDevice(@RequestBody UploadDeviceFileModule module) {
        String message = null;
        try {
            TblDeviceFile deviceFile = tblDeviceFileService.getByDeviceFileId(module.getDeviceFileId());

            Map<String, String> map = new HashMap<>();
            map.put(MessageConstants.MESSAGE_TYPE, "Maintenance");
            map.put(MessageConstants.MESSAGE_STYPE, "upgrade");
            map.put(MessageConstants.ELEVATOR_CODE, module.getElevatorCode());
            map.put(MessageConstants.SENSOR_TYPE, module.getSensorType());
            map.put("module", "master");
            map.put("url", deviceFile.getRequestFileUrl());
            map.put("signature", deviceFile.getSignature());
            map.put("crc", deviceFile.getCrc());
            // 补充本地文件用于获取文件大小
            map.put("localFilePath", deviceFile.getFileUrl());
            message = JSON.toJSONString(map);
            //直接通过restTemplate调用Socket模块
            restTemplateSendMessageToCube(map);

        } catch (Exception e) {
            log.error("uploadDevice {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
            return new ResponseResult("0001", "msg1_05");
        }
        return ResponseResult.successObj(message);
    }

    /**
     * 根据设备版本号、电梯编号、小区id搜索对应的可升级的梯
     *
     * @return 可以升级的设备
     */
    @PostMapping("/searchDeviceByHWVersion")
    public ResponseResult searchDeviceByHWVersion(@RequestBody SearchElevatorModule searchElevatorModule) {

        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());

        String masterVersion = searchElevatorModule.getvMasterVersion();

        if (masterVersion != null && (masterVersion.startsWith("v") || masterVersion.startsWith("V"))) {
            searchElevatorModule.setvMasterVersion(masterVersion.substring(1));
        }

        //搜索用户授权的梯，以及对应的设备
        var pageListResultEntity = elevatorService.searchDeviceByHWVersion(searchElevatorModule);

        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 批量升级
     *
     * @param uploadDeviceFileModuleBatch 设备批量升级
     */
    @PostMapping("/batchEquipmentUpgrade")
    public ResponseResult batchEquipmentUpgrade(@RequestBody UploadDeviceFileModuleBatch uploadDeviceFileModuleBatch) {

        //查询升级文件信息
        var deviceFile = tblDeviceFileService.getByDeviceFileId(uploadDeviceFileModuleBatch.getDeviceFileId());

        //请先指定升级文件
        if (deviceFile == null) {
            return new ResponseResult("0001", "msg1_011");
        }

        //创建一个10个大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch counter = new CountDownLatch(uploadDeviceFileModuleBatch.getUploadDeviceFileModules().size());

        uploadDeviceFileModuleBatch.getUploadDeviceFileModules().forEach(it -> {

            //提交一个线程任务，发起一次升级请求
            executorService.submit(() -> {

                //保证下发消息的线程安全
                ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

                map.put(MessageConstants.MESSAGE_TYPE, "Maintenance");
                map.put(MessageConstants.MESSAGE_STYPE, "upgrade");
                map.put(MessageConstants.ELEVATOR_CODE, it.getElevatorCode());
                map.put(MessageConstants.SENSOR_TYPE, it.getSensorType());
                map.put("module", "master");
                map.put("url", deviceFile.getRequestFileUrl());
                map.put("signature", deviceFile.getSignature());
                map.put("crc", deviceFile.getCrc());
                // 补充本地文件用于获取文件大小
                map.put("localFilePath", deviceFile.getFileUrl());

                //不用openfeign调用socket模块对外接口，多个模块启动会导致轮询调用，导致部分设备不成功情况
                //直接使用连接调用，分别调用起的注册的socket
                try {
                    //restTemplate请求服务调用
                    restTemplateSendMessageToCube(map);
                } catch (RestClientException e) {
                    log.error("batchEquipmentUpgrade restTemplateSendMessageToCube {} {}", e.getMessage(),
                            ExceptionUtil.stacktraceToString(e));
                } finally {
                    counter.countDown();
                }
            });

        });

        //等待所有线程执行完毕
        try {
            counter.await();
        } catch (InterruptedException e) {
            log.error("batchEquipmentUpgrade {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        }

        executorService.shutdown();

        return ResponseResult.success();
    }

    /**
     * 设备请求升级文件
     */
    @SaIgnore
    @GetMapping(value = "/wgetFile/{fileName}")
    public void wgetFile(HttpServletResponse response, @PathVariable String fileName) {
        var deviceFile = tblDeviceFileService.getByDeviceFileName(fileName);
        // 设置相应类型,告诉设备输出的内容为zip
        response.setContentType("application/zip");
        // 设置响应头信息，告诉设备不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);

        try (var inputStream = new FileInputStream(deviceFile.getFileUrl())) {
            // 接收每一个读取进来的数据
            byte[] b = new byte[8192];
            int temp;
            while ((temp = inputStream.read(b)) != -1) {
                response.getOutputStream().write(b, 0, temp);
            }
        } catch (IOException e) {
            log.error("wgetFile调用失败,入参为 {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        }
    }

    /**
     * 调用socket服务向盒子下发消息
     */
    private void restTemplateSendMessageToCube(Map<String, String> map) {


        // 补充本地文件用于获取文件大小
        map.put("size", "0");
        if (map.containsKey("localFilePath") && StringUtils.hasText(map.get("localFilePath"))) {
            File file = new File(map.get("localFilePath"));
            if (file.exists()) {
                map.put("size", String.valueOf(file.length()));
            }
            map.remove("localFilePath");
        }

        String message = JSON.toJSONString(map);

        String elevatorCode = map.get("elevatorCode");
        String sensorType = map.get("sensorType");

        String key = "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
        var deviceStatus = redisTemplate.opsForHash().entries(key);
        String nodeIp = String.valueOf(deviceStatus.get("server_ip"));
        String nodePort = String.valueOf(deviceStatus.get("server_port"));

        // 请求路径：
        String url = String.format("http://%s:%d/cube/sendMessage", nodeIp, Integer.valueOf(nodePort));

        // 发出一个post请求
        try {
            String s = restTemplate.postForObject(url, message, String.class);
            log.info("[{}] ---升级指令下发成功，下发地址：[{}]，下发消息：[{}]，返回信息：[{}]\n", TimeUtils.nowTime(), url, message, s);
        } catch (RestClientException e) {
            log.error("接口调用失败,入参为 {} {} {}", message, e.getMessage(), ExceptionUtil.stacktraceToString(e));
        }

    }

    @GetMapping("/getHwVersions/{eType}")
    public Object getHwVersions(@PathVariable @NotNull String eType) {
        List<String> versions = tblDeviceFileService.getHwVersions(eType);
        String res = String.join(",", versions);
        List<String> material = Arrays.asList(res.split(","));

        List<String> result = material.stream().distinct().sorted().collect(Collectors.toList());
        return ResponseResult.successObj(result);
    }
}