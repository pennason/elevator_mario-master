package com.shmashine.api.controller.elevatorconfig;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.kafka.KafkaProducer;
import com.shmashine.api.kafka.KafkaTopicConstants;
import com.shmashine.api.service.elevatorconfig.TblElevatorConfigServiceI;
import com.shmashine.common.entity.TblElevatorConfig;
import com.shmashine.socketClients.SocketClient;


/**
 * 电梯配置接口
 *
 * @author little.li
 */
@RestController
@RequestMapping("/elevator/config")
public class ElevatorConfigController {


    private final TblElevatorConfigServiceI elevatorConfigService;

    private final KafkaProducer kafkaProducer;

    @Autowired
    public ElevatorConfigController(TblElevatorConfigServiceI elevatorConfigService, KafkaProducer kafkaProducer) {
        this.elevatorConfigService = elevatorConfigService;
        this.kafkaProducer = kafkaProducer;
    }

    @Resource
    private SocketClient socketClient;

    @GetMapping("/senior/{elevatorId}")
    public Object getSeniorConfig(@PathVariable String elevatorId) {
        TblElevatorConfig elevatorConfig = elevatorConfigService.getByElevatorId(elevatorId);
        return ResponseResult.successObj(elevatorConfig);
    }


    @GetMapping("/screen/{elevatorId}")
    public Object getScreenConfig(@PathVariable String elevatorId) {
        TblElevatorConfig elevatorConfig = elevatorConfigService.getByElevatorId(elevatorId);
        return ResponseResult.successObj(elevatorConfig);
    }


    @PostMapping("/updateScreen/{elevatorId}/{elevatorCode}")
    public Object updateScreenConfig(@PathVariable String elevatorId, @PathVariable String elevatorCode, @RequestBody TblElevatorConfig elevatorConfig) {
        // 广告屏配置下发到设备
        String message = convertScreenConfig(elevatorConfig, elevatorCode);
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.SOCKET_TOPIC, message);

        //openfeign调用socket模块对外接口
        socketClient.sendMessageToCube(message);

        elevatorConfigService.updateConfig(elevatorId, elevatorCode, elevatorConfig);
        return ResponseResult.successObj(message);
    }

    private String convertScreenConfig(TblElevatorConfig elevatorConfig, String elevatorCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("volume", elevatorConfig.getVolume());
        jsonObject.addProperty("video_mask", elevatorConfig.getVideoMask());
        jsonObject.addProperty("voice_mask", elevatorConfig.getVoiceMask());
        jsonObject.addProperty("video_jump", elevatorConfig.getVideoJump());
        jsonObject.addProperty("voice_jump", elevatorConfig.getVoiceJump());
        jsonObject.addProperty("video_door", elevatorConfig.getVideoDoor());
        jsonObject.addProperty("voice_door", elevatorConfig.getVoiceDoor());
        jsonObject.addProperty("TY", "Relay");
        jsonObject.addProperty("ST", "C4S");
        jsonObject.addProperty("TA", "screen");
        jsonObject.addProperty("elevatorCode", elevatorCode);
        jsonObject.addProperty("sensorType", "CarRoof");
        return jsonObject.toString();
    }


    @PostMapping("/updateSenior/{elevatorId}/{elevatorCode}")
    public Object updateSeniorConfig(@PathVariable String elevatorId, @PathVariable String elevatorCode, @RequestBody TblElevatorConfig elevatorConfig) {
        elevatorConfigService.updateConfig(elevatorId, elevatorCode, elevatorConfig);
        return ResponseResult.success();
    }

//
//    @GetMapping("/get/{elevatorConfigId}")
//    public TblElevatorConfig getById(@PathVariable String elevatorConfigId) {
//        TblElevatorConfig tblElevatorConfig = tblElevatorConfigService.getById(elevatorConfigId);
//        return tblElevatorConfig != null ? tblElevatorConfig : new TblElevatorConfig();
//    }
//
//    @PostMapping("/get")
//    public List<TblElevatorConfig> getByEntity(@RequestBody TblElevatorConfig tblElevatorConfig) {
//        return tblElevatorConfigService.getByEntity(tblElevatorConfig);
//    }
//
//    @PostMapping("/list")
//    public List<TblElevatorConfig> list(@RequestBody TblElevatorConfig tblElevatorConfig) {
//        return tblElevatorConfigService.listByEntity(tblElevatorConfig);
//    }
//
//    @PostMapping("/insert")
//    public TblElevatorConfig insert(@RequestBody TblElevatorConfig tblElevatorConfig) {
//        tblElevatorConfigService.insert(tblElevatorConfig);
//        return tblElevatorConfig;
//    }
//
//    @PutMapping("/update")
//    public int update(@RequestBody TblElevatorConfig tblElevatorConfig) {
//        return tblElevatorConfigService.update(tblElevatorConfig);
//    }
//
//    @DeleteMapping("/delete/{elevatorConfigId}")
//    public int deleteOne(@PathVariable String elevatorConfigId) {
//        return tblElevatorConfigService.deleteById(elevatorConfigId);
//    }
//
//    @DeleteMapping("/delete")
//    public int deleteBatch(@RequestBody List<String> elevatorConfigIds) {
//        int result = 0;
//        if (elevatorConfigIds != null && elevatorConfigIds.size() > 0) {
//            result = tblElevatorConfigService.deleteByIds(elevatorConfigIds);
//        }
//        return result;
//    }

}