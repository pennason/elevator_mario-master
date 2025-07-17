package com.shmashine.api.controller.iotCardManage;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.shmashine.api.entity.IotCard;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.util.IotCardInfoDataListener;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/8/6 14:52
 * <p>
 * 流量卡管理
 */
@RestController
@RequestMapping("/iotCard")
public class iotCardController extends BaseRequestEntity {

    private final BizElevatorService elevatorService;

    @Resource
    IotCardInfoDataListener listener;

    public iotCardController(BizElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    /**
     * 查询电梯对应电话卡列表
     *
     * @param iotCard
     * @return
     */
    @PostMapping("/searchElevatorIotCardByUserId")
    public ResponseEntity searchElevatorIotCardByUserId(@RequestBody IotCard iotCard) {

        return ResponseEntity.ok(elevatorService.searchElevatorIotCardByUserId(iotCard));
    }

    /**
     * 上传excel文件，批量添加电话卡
     */
    @PostMapping("/excelResolve")
    public ResponseEntity excelResolve(@RequestParam("excelfile") MultipartFile file) throws IOException {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, IotCard.class, listener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }

            inputStream.close();
        }
        return ResponseEntity.ok("导入成功");
    }

    /**
     * 手动更新流量卡信息
     *
     * @return
     */
    @GetMapping("/updateIotCardInfo/{iccid}")
    public ResponseEntity updateIotCardInfo(@PathVariable("iccid") String iccid) {
        return elevatorService.updateIotCardInfoById(iccid);
    }

    /**
     * 手动更新流量信息
     *
     * @param iotCard
     * @return
     */
    @PostMapping("/updateIotCardInfo")
    public ResponseEntity updateIotCard(@RequestBody IotCard iotCard) {
        return ResponseEntity.ok(elevatorService.updateIotCard(iotCard));
    }

    /**
     * 删除流量卡
     *
     * @param iccid
     * @return
     */
    @GetMapping("/delIotCardInfo/{iccid}")
    public ResponseEntity delIotCardInfo(@PathVariable("iccid") String iccid) {
        elevatorService.delIotCardInfoById(iccid);
        return ResponseEntity.ok("success");
    }

    /**
     * 自动更新流量套餐信息
     *
     * @param iccid
     * @return
     */
    @GetMapping("/updateIotCardPackage/{iccid}")
    public ResponseEntity updateIotCardPackage(@PathVariable("iccid") String iccid) {
        elevatorService.updateIotCardPackage(iccid);
        return ResponseEntity.ok("success");
    }

}