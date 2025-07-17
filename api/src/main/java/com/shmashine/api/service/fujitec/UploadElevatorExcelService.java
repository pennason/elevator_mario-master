package com.shmashine.api.service.fujitec;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.common.entity.TblElevatorFujitec;

/**
 * 电梯导入服务模块
 */

@Service
public class UploadElevatorExcelService {
    @Resource
    private TblElevatorFujitecService elevatorFujitecService;

    public ResponseResult importExcel(Workbook workbook) throws Exception {

        int sheetNum = workbook.getNumberOfSheets();

        for (int i = 0; i < sheetNum; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            Row row = sheet.getRow(0);

            int number = row.getPhysicalNumberOfCells();

            if (number != 28) {
                return ResponseResult.error();
            }

            int rowNum = sheet.getPhysicalNumberOfRows();
            for (int j = 1; j < rowNum; j++) {
                int mark = 1;
                Row curRow = sheet.getRow(j);

                TblElevatorFujitec elevatorFujitec = new TblElevatorFujitec();

                //设备出厂编号
                Cell productIdCell = curRow.getCell(mark++);
                String productId = dealWithCell(productIdCell);
                elevatorFujitec.setProductId(productId);

                //电梯识别码
                Cell identificationNumberCell = curRow.getCell(mark++);
                String identificationNumber = dealWithCell(identificationNumberCell);
                elevatorFujitec.setIdentificationNumber(identificationNumber);

                //设备注册代码
                Cell idNrCell = curRow.getCell(mark++);
                String idNr = dealWithCell(idNrCell);
                elevatorFujitec.setIdNr(idNr);

                //设备品种
                Cell productVarietyCell = curRow.getCell(mark++);
                String productVariety = dealWithCell(productVarietyCell);
                elevatorFujitec.setProductVariety(productVariety);

                //设备品种
                Cell productNameCell = curRow.getCell(mark++);
                String productName = dealWithCell(productNameCell);
                elevatorFujitec.setProductName(productName);

                //设备安装地址
                Cell instAddrCell = curRow.getCell(mark++);
                String instAddr = dealWithCell(instAddrCell);
                elevatorFujitec.setInstAddr(instAddr);

                //设备内部编号
                Cell unitIdCell = curRow.getCell(mark++);
                String unitId = dealWithCell(unitIdCell);
                elevatorFujitec.setUnitId(unitId);

                //设备制造商
                Cell vendorCell = curRow.getCell(mark++);
                String vendor = dealWithCell(vendorCell);
                elevatorFujitec.setVendor(vendor);

                //进口设备代理商
                Cell importDealerCell = curRow.getCell(mark++);
                String importDealer = dealWithCell(importDealerCell);
                elevatorFujitec.setImportDealer(importDealer);

                //设备出厂日期
                Cell productionDateCell = curRow.getCell(mark++);
                String productionDate = dealWithCell(productionDateCell);
                elevatorFujitec.setProductionDate(productionDate);

                //设备改造单位
                Cell modCompanyCell = curRow.getCell(mark++);
                String modCompany = dealWithCell(modCompanyCell);
                elevatorFujitec.setModCompany(modCompany);

                //设备改造日期
                Cell modDateCell = curRow.getCell(mark++);
                String modDate = dealWithCell(modDateCell);
                elevatorFujitec.setModDate(modDate);

                //设备安装单位
                Cell instCompanyCell = curRow.getCell(mark++);
                String instCompany = dealWithCell(instCompanyCell);
                elevatorFujitec.setInstCompany(instCompany);

                //设备安装日期
                Cell instDateCell = curRow.getCell(mark++);
                String instDate = dealWithCell(instDateCell);
                elevatorFujitec.setInstDate(instDate);

                //维护保养单位名称
                Cell maintCompanyCell = curRow.getCell(mark++);
                String maintCompany = dealWithCell(maintCompanyCell);
                elevatorFujitec.setMaintCompany(maintCompany);

                //应急救援电话
                Cell emergencyTelCell = curRow.getCell(mark++);
                String emergencyTel = dealWithCell(emergencyTelCell);
                elevatorFujitec.setEmergencyTel(emergencyTel);

                //使用单位名称
                Cell userCell = curRow.getCell(mark++);
                String user = dealWithCell(userCell);
                elevatorFujitec.setUser(user);

                //层站数
                Cell liftFloorNRCell = curRow.getCell(mark++);
                String liftFloorNR = dealWithCell(liftFloorNRCell);
                elevatorFujitec.setLiftFloorNr(liftFloorNR);

                //额定速度
                Cell liftRatedSpeedCell = curRow.getCell(mark++);
                String liftRatedSpeed = dealWithCell(liftRatedSpeedCell);
                elevatorFujitec.setLiftRatedSpeed(liftRatedSpeed);

                //额定载重量
                Cell liftRatedLoadCell = curRow.getCell(mark++);
                String liftRatedLoad = dealWithCell(liftRatedLoadCell);
                elevatorFujitec.setLiftRatedLoad(liftRatedLoad);

                //电梯所属地区号
                Cell areaCityCell = curRow.getCell(mark++);
                String areaCode = dealWithCell(areaCityCell);
                elevatorFujitec.setAreaCode(areaCode);

                //制造许可证编号
                Cell manufactureLicenseCell = curRow.getCell(mark++);
                String manufactureLicense = dealWithCell(manufactureLicenseCell);
                elevatorFujitec.setManufactureLicense(manufactureLicense);

                //终端制造企业编号（由监管平台统一分配）
                Cell iotManufactorCodeCell = curRow.getCell(mark++);
                String iotManufactorCode = dealWithCell(iotManufactorCodeCell);
                elevatorFujitec.setIotManufactorCode(iotManufactorCode);

                //监测终端类型
                Cell iotEquipmentTypeCell = curRow.getCell(mark++);
                String iotEquipmentType = dealWithCell(iotEquipmentTypeCell);
                elevatorFujitec.setIotEquipmentType(iotEquipmentType);

                //监测终端编号
                Cell iotEquipmentNumberCell = curRow.getCell(mark++);
                String iotEquipmentNumber = dealWithCell(iotEquipmentNumberCell);
                elevatorFujitec.setIotEquipmentNumber(iotEquipmentNumber);

                //制造单位统一社会信用代码
                Cell manufacturerCodeCell = curRow.getCell(mark++);
                String manufacturerCode = dealWithCell(manufacturerCodeCell);
                elevatorFujitec.setManufacturerCode(manufacturerCode);

                //城市名称
                Cell cityCell = curRow.getCell(mark++);
                String city = dealWithCell(cityCell);
                elevatorFujitec.setCity(city);


                //如果电梯编号存在，则更新信息，否则新增信息
                TblElevatorFujitec fujitecElevator = elevatorFujitecService.getElevatorByProductId(productId);
                if (null != fujitecElevator) {
                    elevatorFujitecService.updateElevatorFujitecInfo(elevatorFujitec);
                } else {
                    elevatorFujitecService.insertElevatorFujitecInfo(elevatorFujitec);
                }
            }
        }
        return ResponseResult.success();
    }

    private String dealWithCell(Cell cell) {
        if (null == cell) {
            return "";
        }
        switch (cell.getCellType()) {
            //判断读取的数据中是否有String类型的
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                /**
                 * 判断是否读取到了日期数据：
                 * 如果是那就进行格式转换，否则会读取的科学计数值
                 * 不是就输出number 数字
                 */
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    //格式转换
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdf.format(date);
                    return dateStr;
                } else {
                    int index = String.valueOf(cell.getNumericCellValue()).indexOf(".");
                    return String.valueOf(cell.getNumericCellValue()).substring(0, index);
                }
            case BLANK:
                return "";
            default:
                return String.valueOf(cell.getNumericCellValue());
        }
    }
}
