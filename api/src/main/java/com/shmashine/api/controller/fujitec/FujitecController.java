package com.shmashine.api.controller.fujitec;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.fujitec.ExportElevatorInfoExcelService;
import com.shmashine.api.service.fujitec.TblElevatorFujitecService;
import com.shmashine.api.service.fujitec.TblSendDataLogService;
import com.shmashine.api.service.fujitec.UploadElevatorExcelService;
import com.shmashine.common.entity.TblElevatorFujitec;
import com.shmashine.common.entity.TblSendDataLog;

@RestController
public class FujitecController {

    @Resource
    private UploadElevatorExcelService uploadElevatorExcelService;

    @Resource
    private TblSendDataLogService sendDataLogService;

    @Resource
    private TblElevatorFujitecService elevatorFujitecService;

    @Resource
    private ExportElevatorInfoExcelService exportElevatorInfoExcelService;

    @GetMapping("/test")
    public String invokeLocalMethod() {
        return "service is health";
    }

    /**
     * 查询电梯
     *
     * @param elevatorFujitec 查询条件封装到实体
     * @return
     */
    @PostMapping("/getElevatorByCondition")
    public ResponseResult getElevatorByCondition(@RequestBody TblElevatorFujitec elevatorFujitec) {
        return ResponseResult.successObj(elevatorFujitecService.getElevatorByConditionWithPage(elevatorFujitec));
    }

    /**
     * 删除单个电梯
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteElevatorById/{id}")
    public ResponseResult deleteElevatorById(@PathVariable("id") String id) {
        int num = 0;
        if (null != id && !id.equals("")) {
            num = elevatorFujitecService.deleteElevatorById(id);
        }

        if (num > 0) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }

    }

    /**
     * 新增电梯
     *
     * @param elevatorFujitec
     * @return
     */
    @PostMapping("/insertElevator")
    public ResponseResult insertElevator(@RequestBody TblElevatorFujitec elevatorFujitec) {
        int num = elevatorFujitecService.insertElevatorFujitecInfo(elevatorFujitec);
        if (num > 0) {
            return ResponseResult.success();

        } else {
            return ResponseResult.error();
        }
    }

    /**
     * 更新电梯
     *
     * @param elevatorFujitec
     * @return
     */
    @PostMapping("/updateElevator")
    public ResponseResult updateElevator(@RequestBody TblElevatorFujitec elevatorFujitec) {
        int num = elevatorFujitecService.updateElevatorFujitecInfo(elevatorFujitec);

        if (num > 0) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }

    }


    /**
     * 查询推送日志信息
     *
     * @param sendDataLog 查询条件封装到实体
     * @return
     */
    @PostMapping("/getSendDataLogByCondition")
    public ResponseResult getSendDataLogByCondition(@RequestBody TblSendDataLog sendDataLog) {
        return ResponseResult.successObj(sendDataLogService.getSendDataLogByConditionWithPage(sendDataLog));
    }

    /**
     * 导入Excel中的电梯信息
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadElevatorExcel")//@RequestParam
    public ResponseResult uploadElevatorExcel(MultipartFile file) throws Exception {
        Workbook workbook = null;
        String fileFullName = file.getOriginalFilename();
        InputStream fileInputStream = file.getInputStream();

        if (fileFullName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(fileInputStream);
        } else if (fileFullName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(fileInputStream);

        } else {
            return ResponseResult.error();
        }

        return uploadElevatorExcelService.importExcel(workbook);
    }

    /**
     * 导出电梯信息到Excel
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @GetMapping("/exportElevatorInfoExcel")
    public void exportElevatorInfoExcel(HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<TblElevatorFujitec> elevatorFujitecs = elevatorFujitecService.getAllElevator();
        if (null == elevatorFujitecs || elevatorFujitecs.size() <= 0) {
            return;
        }
        exportElevatorInfoExcelService.exportElevatorInfoExcel(elevatorFujitecs, request, response);
    }
}

