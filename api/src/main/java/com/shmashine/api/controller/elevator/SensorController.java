package com.shmashine.api.controller.elevator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.service.elevator.TblSensorServiceI;
import com.shmashine.common.entity.TblSensor;

/**
 * 传感器接口
 *
 * @author chenx
 */

@RestController
@RequestMapping("/tblSensor")
public class SensorController {

    @Autowired
    private TblSensorServiceI tblSensorService;

    @GetMapping("/get/{vSensorId}")
    public TblSensor getById(@PathVariable String vSensorId) {
        TblSensor tblSensor = tblSensorService.getById(vSensorId);
        return tblSensor != null ? tblSensor : new TblSensor();
    }

    @PostMapping("/get")
    public List<TblSensor> getByEntity(@RequestBody TblSensor tblSensor) {
        return tblSensorService.getByEntity(tblSensor);
    }

    @PostMapping("/list")
    public List<TblSensor> list(@RequestBody TblSensor tblSensor) {
        return tblSensorService.listByEntity(tblSensor);
    }

    @PostMapping("/insert")
    public TblSensor insert(@RequestBody TblSensor tblSensor) {
        tblSensorService.insert(tblSensor);
        return tblSensor;
    }

    @PutMapping("/update")
    public int update(@RequestBody TblSensor tblSensor) {
        return tblSensorService.update(tblSensor);
    }

    @DeleteMapping("/delete/{vSensorId}")
    public int deleteOne(@PathVariable String vSensorId) {
        return tblSensorService.deleteById(vSensorId);
    }

    @DeleteMapping("/delete")
    public int deleteBatch(@RequestBody List<String> vSensorIds) {
        int result = 0;
        if (vSensorIds != null && vSensorIds.size() > 0) {
            result = tblSensorService.deleteByIds(vSensorIds);
        }
        return result;
    }

}