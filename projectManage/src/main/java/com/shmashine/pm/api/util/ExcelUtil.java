package com.shmashine.pm.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.OSSUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 业务工具类，不是通用工具类
 *
 * @author chenx
 */
@Slf4j
public final class ExcelUtil {

    private static final String InvestigateTaskFile = "doc/elevator_green_code.xlsx";
    private static final String DistributionFile = "doc/distribution_temp.xlsx";

    public static String createExcel(List<?> importlist, String type, String bizId) {
        //获取数据集
        XSSFWorkbook workbook;
        ClassPathResource oriFile;
        File tempFile;
        int index = 1;

        try {
            if ("investigateTask".equals(type)) {
                oriFile = new ClassPathResource(InvestigateTaskFile);
                tempFile = File.createTempFile("investigateTask" + bizId, ".xlsx");
            } else {
                oriFile = new ClassPathResource(DistributionFile);
                tempFile = File.createTempFile("distribution" + bizId, ".xlsx");
                index = 2;
            }

//            FileUtils.copyFile(oriFile.getFile(), tempFile);
            FileUtil.inputStreamToFile(oriFile.getInputStream(), tempFile);

            FileInputStream fileInputStream = new FileInputStream(tempFile);
            workbook = new XSSFWorkbook(fileInputStream);
            //生成一个表格
            XSSFSheet sheet = workbook.getSheetAt(0);
            //设置表格默认列宽度为35个字节
            sheet.setDefaultColumnWidth((short) 35);

            //循环字段名数组，创建标题行
            XSSFRow row = null;

            //创建普通数据行
            if (importlist != null && importlist.size() > 0) {
                //创建普通行
                for (Object o : importlist) {
                    //因为第一行已经用于创建标题行，故从第二行开始创建
                    row = sheet.createRow(index++);
                    //如果是第一行就让其为标题行
                    List<String> list = (List<String>) o;

                    for (int j = 0; j < list.size(); j++) {
                        //创建列
                        XSSFCell cell = row.createCell(j);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(transCellType(list.get(j)));
                    }
                }
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                fileInputStream.close();
            } catch (Exception ex) {
                log.error("ex ==", ex);
                ex.printStackTrace();
            }

            return OSSUtil.savePmTaskFile(tempFile);
        } catch (Exception e) {
            log.error("e ==", e);
            e.printStackTrace();
        }

        return "";
    }

    public static List<List<String>> readXlsxData(MultipartFile file, int startRow) {
        XSSFWorkbook xssfWorkbook = null;

        try {
            InputStream inputStream = file.getInputStream();
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            log.error("Excel data file cannot be found!");
        }

        assert xssfWorkbook != null;
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

        List<List<String>> list = new ArrayList<List<String>>();

        XSSFRow titleRow = xssfSheet.getRow(0);
        //循环取每行的数据
        out:
        for (int rowIndex = startRow; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }

            List<String> subList = new ArrayList<String>();
//            int cells = xssfRow.getPhysicalNumberOfCells();
//            int cells = 66;
            int cells = titleRow.getPhysicalNumberOfCells();

            //循环取每个单元格(cell)的数据
            inner:
            for (int cellIndex = 0; cellIndex < cells; cellIndex++) {
                XSSFCell xssfCell = xssfRow.getCell(cellIndex);

                if (cellIndex == 0 && Objects.equals(getString(xssfCell), "")) {
                    break out;
                }
                subList.add(getString(xssfCell));
            }
            list.add(subList);

        }
        return list;
    }

    private static String transCellType(Object value) {
        String str = null;
        if (value instanceof Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
        } else {
            str = String.valueOf(value);
            if (Objects.equals(str, "null")) {
                str = "";
            }
        }

        return str;
    }

    private static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else {
            return xssfCell.getStringCellValue();
        }
    }
}