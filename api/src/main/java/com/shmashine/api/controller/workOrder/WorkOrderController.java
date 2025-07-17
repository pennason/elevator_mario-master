package com.shmashine.api.controller.workOrder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import cn.dev33.satoken.annotation.SaIgnore;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.entity.MaintenanceExcel;
import com.shmashine.api.entity.RepairExcel;
import com.shmashine.api.entity.RescueExcel;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.user.input.SearchUserListModule;
import com.shmashine.api.module.workOrder.SearchAgencyWorkOrderListModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.file.BizFileService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.workOrder.BizWorkOrderService;
import com.shmashine.common.entity.TblSysSysteminfo;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.POIUtils;

import lombok.RequiredArgsConstructor;

/**
 * 工单接口
 *
 * @author little.li
 */

@SaIgnore
@RestController
@RequestMapping("/workOrder")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WorkOrderController extends BaseRequestEntity {
    private final BizWorkOrderService tblWorkOrderService;
    private final BizUserService bizUserService;
    private final BizDeptService bizDeptService;
    private final BizFileService bizFileService;
    private final RestTemplate restTemplate;

    private static final String BASE_URI = "http://www.smartelevator.net/platform/api/v1";

    private static final String TOKEN_URI = BASE_URI + "/login";
    private static final String REPAIR_URL = BASE_URI + "/eventReports/repair/excel";
    private static final String RESCUE_URL = BASE_URI + "/eventReports/rescue/excel";
    private static final String MAINTENANCE_URL = BASE_URI + "/maintenanceRecords/maintenance/excel";




    /**
     * 首页展示 查询代办工单列表
     */
    @PostMapping("/searchAgencyWorkOrderList")
    public Object searchAgencyWorkOrderList(
            @RequestBody SearchAgencyWorkOrderListModule searchAgencyWorkOrderListModule) {
        // 1. 判断是否是管理员权限
        searchAgencyWorkOrderListModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchAgencyWorkOrderListModule.setUserId(super.getUserId());
        // 2. 查询代办工单列表
        return ResponseResult.successObj(
                tblWorkOrderService.searchAgencyWorkOrderList(searchAgencyWorkOrderListModule));
    }

    /**
     * 工单处理页面按钮权限控制
     */
    @GetMapping("/handle/{handleStatus}")
    public Object getHandlePower(@PathVariable Integer handleStatus) {
        Map<String, Object> map = tblWorkOrderService.getHandlePower(handleStatus);
        return ResponseResult.successObj(map);
    }

    /**
     * 急修工单删除
     */
    @PostMapping("/delEventOrderById")
    public ResponseResult delEventOrderById(@RequestParam("eventNumber") String eventNumber) {

        return ResponseResult.successObj(tblWorkOrderService.delEventOrderById(super.getUserId(), eventNumber));
    }


    /**
     * 上传文件
     */
    @PostMapping("/saveFile/{businessId}")
    public Object saveFile(@RequestParam("files") MultipartFile[] files, @PathVariable String businessId) {
        // 文件上传OSS 落库处理
        List<String> fileList = OSSUtil.saveWorkOrderFile(files, businessId);
        if (!CollectionUtils.isEmpty(fileList)) {
            bizFileService.insertWorkOrderBatch(fileList, businessId);
        }
        return ResponseResult.success();
    }


    /**
     * 获取用户列表
     */
    @PostMapping("/searchUser")
    public Object searchUser(@RequestBody SearchUserListModule searchUserListModule) {
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        ArrayList<String> results = Lists.newArrayList();
        results.add(deptId);
        recursion(deptId, results);
        searchUserListModule.setPermissionDeptIds(results);
        // 2. 判断有没有下级部门 有添加到list
        // 3. 拿出所有部门编号 去 查询 符合条件的用户列表
        return bizUserService.userList(searchUserListModule);
    }


    /**
     * 递归查询 下级部门的用户
     */
    public void recursion(String deptId, List<String> strings) {

        if (null != deptId) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(deptId);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }


    /**
     * 获取维保工单当前维保项
     */
    @GetMapping("/maintenance/{workOrderId}")
    public Object getMaintenanceDetail(@PathVariable("workOrderId") String workOrderId) {
        var maintenanceDetailList = tblWorkOrderService.getMaintenanceDetail(workOrderId);
        return ResponseResult.successObj(maintenanceDetailList);
    }


    /**
     * 获取维保类型
     */
    @GetMapping("/maintenanceType")
    public Object getMaintenanceType() {
        List<TblSysSysteminfo> maintenanceDefinitionList = tblWorkOrderService.getMaintenanceType();
        return ResponseResult.successObj(maintenanceDefinitionList);
    }

    /**
     * 导出瑞金急修工单excel
     */
    @PostMapping("/repair/excel")
    public void repairExcel(@RequestBody HashMap hashMap, HttpServletResponse response) {

        ExcelWriter excelWriter = null;
        HashMap res;

        try {
            //获取token
            String token = getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<HashMap> request = new HttpEntity<>(hashMap, headers);

            res = restTemplate.postForObject(REPAIR_URL, request, HashMap.class);

            HashMap result = (HashMap) res.get("result");

            String fileName = (String) result.get("fileName");

            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //此处指定了文件类型为xls，如果是xlsx的，请自行替换修改
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");

            excelWriter = EasyExcel.write(response.getOutputStream(), RepairExcel.class).build();

            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

            String fileUrl = (String) result.get("fileUrl");
            List<String[]> lists = urlExcelToList(fileUrl, fileName);
            //String[]转list
            var dataRepairExcel = stringsToListRepairExcel(lists);
            excelWriter.write(dataRepairExcel, writeSheet);
        } catch (Exception e) {
            response.setStatus(500);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 导出瑞金救援工单excel
     */
    @PostMapping("/rescue/excel")
    public void rescueExcel(@RequestBody HashMap hashMap, HttpServletResponse response) {

        ExcelWriter excelWriter = null;
        try {

            //获取token
            String token = getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<HashMap> request = new HttpEntity<>(hashMap, headers);

            HashMap res = restTemplate.postForObject(RESCUE_URL, request, HashMap.class);

            HashMap result = (HashMap) res.get("result");
            String fileName = (String) result.get("fileName");

            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //此处指定了文件类型为xls，如果是xlsx的，请自行替换修改
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");

            excelWriter = EasyExcel.write(response.getOutputStream(), RescueExcel.class).build();

            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

            String fileUrl = (String) result.get("fileUrl");
            List<String[]> lists = urlExcelToList(fileUrl, fileName);
            //String[]转list
            var dataRescueExcel = stringsToListRescueExcel(lists);
            excelWriter.write(dataRescueExcel, writeSheet);
        } catch (Exception e) {
            response.setStatus(500);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 导出瑞金维保工单excel
     */
    @PostMapping("/maintenance/excel")
    public void maintenanceExcel(@RequestBody HashMap hashMap, HttpServletResponse response) {

        ExcelWriter excelWriter = null;

        try {
            //获取token
            String token = getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);
            HttpEntity<HashMap> request = new HttpEntity<>(hashMap, headers);

            HashMap res = restTemplate.postForObject(MAINTENANCE_URL, request, HashMap.class);

            HashMap result = (HashMap) res.get("result");
            String fileName = (String) result.get("fileName");

            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //此处指定了文件类型为xls，如果是xlsx的，请自行替换修改
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");

            excelWriter = EasyExcel.write(response.getOutputStream(), MaintenanceExcel.class).build();

            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

            String fileUrl = (String) result.get("fileUrl");
            List<String[]> lists = urlExcelToList(fileUrl, fileName);

            //String[]转list
            var dataMaintenanceExcel = stringsToListMaintenanceExcel(lists);
            excelWriter.write(dataMaintenanceExcel, writeSheet);
        } catch (Exception e) {
            response.setStatus(500);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 维保工单
     */
    private List<MaintenanceExcel> stringsToListMaintenanceExcel(List<String[]> lists) {

        ArrayList<MaintenanceExcel> maintenanceExcelList = new ArrayList<>();

        for (String[] list : lists) {

            String registerCode = list[0];
            String eleName = getEleNameByRegisterCode(registerCode);
            MaintenanceExcel maintenanceExcel = new MaintenanceExcel();
            maintenanceExcel.setRegisterCode(registerCode);
            maintenanceExcel.setElevatorName(eleName);
            maintenanceExcel.setRegion(list[1]);
            maintenanceExcel.setStreet(list[2]);
            maintenanceExcel.setSupervisionDepartment(list[3]);
            maintenanceExcel.setInstallationAddress(list[4]);
            maintenanceExcel.setUseAddress(list[5]);
            maintenanceExcel.setMaintenanceUnit(list[6]);
            maintenanceExcel.setWorkOrderNumber(list[7]);
            maintenanceExcel.setMaintenanceType(list[8]);
            maintenanceExcel.setWorkOrderStatus(list[9]);
            maintenanceExcel.setWorkOrderCompletionStaff(list[10]);
            maintenanceExcel.setWorkOrderCompletionStaffPhone(list[11]);
            maintenanceExcel.setWorkOrderCompletionTime(list[12]);
            maintenanceExcel.setSignInTime(list[13]);
            maintenanceExcel.setSignInPeople(list[14]);
            maintenanceExcel.setSignInPhoneNumber(list[15]);
            maintenanceExcel.setConfirmTime(list[16]);
            maintenanceExcel.setConfirmStatus(list[17]);
            maintenanceExcel.setUseUnitAppraise(list[18]);
            maintenanceExcel.setUseUnitScore(list[19]);
            maintenanceExcel.setMainProblemsFound(list[20]);

            maintenanceExcelList.add(maintenanceExcel);
        }

        return maintenanceExcelList;
    }

    /**
     * 救援工单
     */
    private List<RescueExcel> stringsToListRescueExcel(List<String[]> lists) {

        ArrayList<RescueExcel> rescueExcelList = new ArrayList<>();

        for (String[] list : lists) {

            String registerCode = list[0];
            String eleName = getEleNameByRegisterCode(registerCode);
            RescueExcel rescueExcel = new RescueExcel();
            rescueExcel.setRegisterCode(registerCode);
            rescueExcel.setElevatorName(eleName);
            rescueExcel.setRegion(list[1]);
            rescueExcel.setStreet(list[2]);
            rescueExcel.setSupervisionDepartment(list[3]);
            rescueExcel.setInstallationAddress(list[4]);
            rescueExcel.setMaintenanceUnitNow(list[5]);
            rescueExcel.setMaintenanceUnitWorkOrder(list[6]);
            rescueExcel.setReportTime(list[7]);
            rescueExcel.setReportChannel(list[8]);
            rescueExcel.setReportPeople(list[9]);
            rescueExcel.setReportPhoneNumber(list[10]);
            rescueExcel.setEventContent(list[11]);
            rescueExcel.setWorkOrderStatus(list[12]);
            rescueExcel.setTakeOrdersTime(list[13]);
            rescueExcel.setTakeOrdersUnit(list[14]);
            rescueExcel.setTakeOrdersPeople(list[15]);
            rescueExcel.setTakeOrdersPhoneNumber(list[16]);
            rescueExcel.setSignInTime(list[17]);
            rescueExcel.setSignInUnit(list[18]);
            rescueExcel.setSignInPeople(list[19]);
            rescueExcel.setSignInPeoplePhoneNumber(list[20]);
            rescueExcel.setFalseAlarmOperationTime(list[21]);
            rescueExcel.setFalseAlarmOperationUnit(list[22]);
            rescueExcel.setFalseAlarmOperationPeople(list[23]);
            rescueExcel.setFalseAlarmOperationPeoplePhoneNumber(list[24]);
            rescueExcel.setCompletionTime(list[25]);
            rescueExcel.setCompletionUnit(list[26]);
            rescueExcel.setCompletionPeople(list[27]);
            rescueExcel.setCompletionPeoplePhoneNumber(list[28]);
            rescueExcel.setCompletionFaultType(list[29]);
            rescueExcel.setCompletionDescription(list[30]);
            rescueExcel.setConfirmTime(list[31]);
            rescueExcel.setConfirmType(list[32]);

            rescueExcelList.add(rescueExcel);
        }

        return rescueExcelList;
    }

    /**
     * 急修工单
     */
    private List<RepairExcel> stringsToListRepairExcel(List<String[]> lists) {

        ArrayList<RepairExcel> repairExcelList = new ArrayList<>();

        for (String[] list : lists) {
            String registerCode = list[0];
            String eleName = getEleNameByRegisterCode(registerCode);
            RepairExcel repairExcel = new RepairExcel();
            repairExcel.setRegisterCode(registerCode);
            repairExcel.setElevatorName(eleName);
            repairExcel.setRegion(list[1]);
            repairExcel.setStreet(list[2]);
            repairExcel.setSupervisionDepartment(list[3]);
            repairExcel.setInstallationAddress(list[4]);
            repairExcel.setMaintenanceUnitNow(list[5]);
            repairExcel.setMaintenanceUnitWorkOrder(list[6]);
            repairExcel.setReportTime(list[7]);
            repairExcel.setReportChannel(list[8]);
            repairExcel.setReportPeople(list[9]);
            repairExcel.setReportPhoneNumber(list[10]);
            repairExcel.setReportFaultType(list[11]);
            repairExcel.setEventContent(list[12]);
            repairExcel.setWorkOrderStatus(list[13]);
            repairExcel.setSignInTime(list[14]);
            repairExcel.setSignInUnit(list[15]);
            repairExcel.setSignInPeople(list[16]);
            repairExcel.setSignInPhoneNumber(list[17]);
            repairExcel.setFalseAlarmOperationTime(list[18]);
            repairExcel.setFalseAlarmOperationUnit(list[19]);
            repairExcel.setFalseAlarmOperationPeople(list[20]);
            repairExcel.setFalseAlarmOperationPeoplePhoneNumber(list[21]);
            repairExcel.setCompletionTime(list[22]);
            repairExcel.setCompletionUnit(list[23]);
            repairExcel.setCompletionPeople(list[24]);
            repairExcel.setCompletionPeoplePhoneNumber(list[25]);
            repairExcel.setCompletionFaultType(list[26]);
            repairExcel.setCompletionDescription(list[27]);
            repairExcel.setConfirmTime(list[28]);
            repairExcel.setConfirmType(list[29]);

            repairExcelList.add(repairExcel);
        }

        return repairExcelList;
    }

    /**
     * 根据注册码获取电梯name
     */
    private String getEleNameByRegisterCode(String registerCode) {
        HashMap<String, String> eleNameMap = new HashMap<>();
        String eleName = eleNameMap.get(registerCode);
        if (eleName == null) {
            eleName = tblWorkOrderService.getEleNameByRegisterCode(registerCode);
            eleNameMap.put(registerCode, eleName);
        }
        return eleName;
    }

    /**
     * 获取仪电token
     */
    private String getToken() {
        HashMap<String, String> request = new HashMap<>();
        request.put("username", "sys_shmx");
        request.put("password", "i*esa-t0013C");
        HashMap res = restTemplate.postForObject(TOKEN_URI, request, HashMap.class);
        HashMap result = (HashMap) res.get("result");
        String token = (String) result.get("token");
        return token;
    }


    /**
     * 根据网络excelUrl解析
     */
    private List<String[]> urlExcelToList(String fileUrl, String fileName) throws IOException {

        Workbook workbook = null;

        URL urlfile;
        HttpURLConnection httpUrl;
        BufferedInputStream bis;
        try {

            urlfile = new URL(fileUrl);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());

            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(bis);
            } else if (fileName.endsWith("xlsx")) {
                //2007
                workbook = new XSSFWorkbook(bis);
            }

            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if (workbook != null) {
            //遍历所有的工作表
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue; //如果第一个工作表是空的，就跳出，继续循环下一个工作表。
                }
                //获得当前sheet中有数据的第一行（防止可能前面几行没有数据）
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet中有数据的最后一行的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue; //如果当前行是空，跳出，继续遍历下一个
                    }
                    //获得当前行中有数据的第一个单元格
                    int firstCellNum = row.getFirstCellNum();
                    //根据第一行来获取表格的列数
                    int lastCellNum = sheet.getRow(firstRowNum).getPhysicalNumberOfCells();
                    //创建一个固定大小的数组
                    String[] cells = new String[lastCellNum];
                    //循环当前行中的单元格
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        //获取有数据的单元格
                        Cell cell = row.getCell(cellNum);
                        //将单元格中的数据都转换成字符串并存入数组中，每一个数组就相当于行中的所有数据
                        cells[cellNum] = POIUtils.getCellValue(cell);
                    }
                    //将每一行的数据存入集合中
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }
}