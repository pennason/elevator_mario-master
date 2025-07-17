package com.shmashine.api.service.fujitec;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevatorFujitec;

@Service
public class ExportElevatorInfoExcelService {

    public void exportElevatorInfoExcel(List<TblElevatorFujitec> elevatorFujitecs, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] title = {
                "序号",
                "设备出厂编号",
                "电梯识别码",
                "设备注册代码",
                "设备品种",
                "设备型号",
                "设备安装地址",
                "设备内部编号",
                "设备制造商",
                "进口设备代理商",
                "设备出厂日期",
                "设备改造单位",
                "设备改造日期",
                "设备安装单位",
                "设备安装日期",
                "维护保养单位名称",
                "应急救援电话",
                "使用单位名称",
                "层站数",
                "额定速度",
                "额定载重量",
                "已安装电梯所在城市的区号",
                "制造许可证编号",
                "终端制造企业编号（由监管平台统一分配）",
                "监测终端类型",
                "监测终端编号",
                "制造单位统一社会信用代码",
                "城市名称"};
        int num = 1;
        int rowIndex = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(rowIndex++);
        for (int i = 0; i < title.length; i++) {
            row.createCell(i).setCellValue(title[i]);
        }

        for (TblElevatorFujitec elevatorFujitec : elevatorFujitecs) {

            int column = 0;
            row = sheet.createRow(rowIndex++);

            //序号
            row.createCell(column++).setCellValue(num++);

            /**
             * 设备出厂编号
             */
            String productId = elevatorFujitec.getProductId();
            if (!"".equals(productId) && null != productId) {
                row.createCell(column++).setCellValue(productId);
            } else {
                row.createCell(column++).setCellValue("");
            }

            /**
             * 电梯识别码
             */
            String identificationNumber = elevatorFujitec.getIdentificationNumber();
            if (!"".equals(identificationNumber) && null != identificationNumber) {
                row.createCell(column++).setCellValue(identificationNumber);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备注册代码
             */
            String idNr = elevatorFujitec.getIdNr();
            if (!"".equals(idNr) && null != idNr) {
                row.createCell(column++).setCellValue(idNr);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备品种
             */
            String productVariety = elevatorFujitec.getProductVariety();
            if (!"".equals(productVariety) && null != productVariety) {
                row.createCell(column++).setCellValue(productVariety);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备型号
             */
            String productName = elevatorFujitec.getProductName();
            if (!"".equals(productName) && null != productName) {
                row.createCell(column++).setCellValue(productName);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备安装地址
             */
            String instAddr = elevatorFujitec.getInstAddr();
            if (!"".equals(instAddr) && null != instAddr) {
                row.createCell(column++).setCellValue(instAddr);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备内部编号
             */
            String unitId = elevatorFujitec.getUnitId();
            if (!"".equals(unitId) && null != unitId) {
                row.createCell(column++).setCellValue(unitId);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备制造商
             */
            String vendor = elevatorFujitec.getVendor();
            if (!"".equals(vendor) && null != vendor) {
                row.createCell(column++).setCellValue(vendor);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 进口设备代理商
             */
            String importDealer = elevatorFujitec.getImportDealer();
            if (!"".equals(importDealer) && null != importDealer) {
                row.createCell(column++).setCellValue(importDealer);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备出厂日期
             */
            String productionDate = elevatorFujitec.getProductionDate();
            if (!"".equals(productionDate) && null != productionDate) {
                row.createCell(column++).setCellValue(productionDate);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备改造单位
             */
            String modCompany = elevatorFujitec.getModCompany();
            if (!"".equals(modCompany) && null != modCompany) {
                row.createCell(column++).setCellValue(modCompany);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备改造日期
             */
            String modDate = elevatorFujitec.getModDate();
            if (!"".equals(modDate) && null != modDate) {
                row.createCell(column++).setCellValue(modDate);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备安装单位
             */
            String instCompany = elevatorFujitec.getInstCompany();
            if (!"".equals(instCompany) && null != instCompany) {
                row.createCell(column++).setCellValue(instCompany);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 设备安装日期
             */
            String instDate = elevatorFujitec.getInstDate();
            if (!"".equals(instDate) && null != instDate) {
                row.createCell(column++).setCellValue(instDate);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 维护保养单位名称
             */
            String maintCompany = elevatorFujitec.getMaintCompany();
            if (!"".equals(maintCompany) && null != maintCompany) {
                row.createCell(column++).setCellValue(maintCompany);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 应急救援电话
             */
            String emergencyTel = elevatorFujitec.getEmergencyTel();
            if (!"".equals(emergencyTel) && null != emergencyTel) {
                row.createCell(column++).setCellValue(emergencyTel);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 使用单位名称
             */
            String user = elevatorFujitec.getUser();
            if (!"".equals(user) && null != user) {
                row.createCell(column++).setCellValue(user);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 层站数
             */
            String liftFloorNr = elevatorFujitec.getLiftFloorNr();
            if (!"".equals(liftFloorNr) && null != liftFloorNr) {
                row.createCell(column++).setCellValue(liftFloorNr);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 额定速度
             */
            String liftRatedSpeed = elevatorFujitec.getLiftRatedSpeed();
            if (!"".equals(liftRatedSpeed) && null != liftRatedSpeed) {
                row.createCell(column++).setCellValue(liftRatedSpeed);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 额定载重量
             */
            String liftRatedLoad = elevatorFujitec.getLiftRatedLoad();
            if (!"".equals(liftRatedLoad) && null != liftRatedLoad) {
                row.createCell(column++).setCellValue(liftRatedLoad);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             * 城市区号
             */
            String areaCode = elevatorFujitec.getAreaCode();
            if (!"".equals(areaCode) && null != areaCode) {
                row.createCell(column++).setCellValue(areaCode);
            } else {
                row.createCell(column++).setCellValue("");
            }

            /**
             * 制造许可证编号
             */
            String manufactureLicense = elevatorFujitec.getManufactureLicense();
            if (!"".equals(manufactureLicense) && null != manufactureLicense) {
                row.createCell(column++).setCellValue(manufactureLicense);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             *终端制造企业编号（由监管平台统一分配）
             */
            String iotManufactorCode = elevatorFujitec.getIotManufactorCode();
            if (!"".equals(iotManufactorCode) && null != iotManufactorCode) {
                row.createCell(column++).setCellValue(iotManufactorCode);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             *监测终端类型
             */
            String iotEquipmentType = elevatorFujitec.getIotEquipmentType();
            if (!"".equals(iotEquipmentType) && null != iotEquipmentType) {
                row.createCell(column++).setCellValue(iotEquipmentType);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             *监测终端编号
             */
            String iotEquipmentNumber = elevatorFujitec.getIotEquipmentNumber();
            if (!"".equals(iotEquipmentNumber) && null != iotEquipmentNumber) {
                row.createCell(column++).setCellValue(iotEquipmentNumber);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             *制造单位统一社会信用代码
             */
            String manufacturerCode = elevatorFujitec.getManufacturerCode();
            if (!"".equals(manufacturerCode) && null != manufacturerCode) {
                row.createCell(column++).setCellValue(manufacturerCode);
            } else {
                row.createCell(column++).setCellValue("");
            }


            /**
             *城市名称
             */
            String city = elevatorFujitec.getCity();
            if (!"".equals(city) && null != city) {
                row.createCell(column++).setCellValue(city);
            } else {
                row.createCell(column++).setCellValue("");
            }

        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String fileName = date + "电梯数据" + ".xlsx";
        //准备将Excel的输出流通过response输出到页面下载
        String userAgent = request.getHeader("User-Agent");
        //response.setContentType("application/force-download");// 设置强制下载不打开
        response.setContentType("application/octet-stream");
        if (userAgent.contains("MSIE")) {
            response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));// 设置文件名*/
        } else {
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));// 设置文件名*/
        }

        //刷新缓冲
        response.flushBuffer();
        OutputStream ouputStream = response.getOutputStream();
        //workbook将Excel写入到response的输出流中，供页面下载该Excel文件
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
        workbook.close();
    }
}
