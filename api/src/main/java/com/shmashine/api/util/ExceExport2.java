package com.shmashine.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.shmashine.common.utils.FileUtil;

/**
 * @author liulifu
 * @version2019-09-21 11:21
 * @Description：Excel导出工具类，依赖于ClassUtil工具类
 */
public final class ExceExport2 {

    // 外协模版路径
    private static final String OutServiceXlsx = "doc/out_service_template.xlsx";

    public static void exportOutServiceTemplate(HttpServletResponse response) {
        //获取数据集
        XSSFWorkbook workbook;
        ClassPathResource originFile;
        File tempFile;

        try {
            originFile = new ClassPathResource(OutServiceXlsx);
            String time = DateUtil.format(DateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
            tempFile = File.createTempFile("外协团队信息导入模版" + time, ".xlsx");

            FileUtil.inputStreamToFile(originFile.getInputStream(), tempFile);

            FileInputStream fileInputStream = new FileInputStream(tempFile);

            workbook = new XSSFWorkbook(fileInputStream);
            //生成一个表格
            XSSFSheet sheet = workbook.getSheetAt(0);

            //设置表格默认列宽度为35个字节
            sheet.setDefaultColumnWidth((short) 35);
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            try {
                workbook.write(fileOutputStream);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                fileOutputStream.flush();
                workbook.close();
                fileOutputStream.close();
                fileInputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将传入的数据导出excel表并下载
     *
     * @param response       返回的HttpServletResponse
     * @param importlist     要导出的对象的集合
     * @param attributeNames 含有每个对象属性在excel表中对应的标题字符串的数组（请按对象中属性排序调整字符串在数组中的位置）
     */
    public static void export(HttpServletResponse response, List<?> importlist, String[] attributeNames) {
        //获取数据集
        List<?> datalist = importlist;

        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        //设置表格默认列宽度为35个字节
        sheet.setDefaultColumnWidth((short) 35);

        //循环字段名数组，创建标题行
        int index = 0;
        Row row = null;
        if (attributeNames != null) {
            row = sheet.createRow(index++);
            //获取字段名数组
            String[] tableAttributeName = attributeNames;
            for (int j = 0; j < tableAttributeName.length; j++) {
                //创建列
                Cell cell = row.createCell(j);
                //设置单元类型为String
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(tableAttributeName[j]));
            }
        }

        //创建普通数据行
        if (datalist != null && datalist.size() > 0) {
            //获取对象属性
            Field[] fields = ClassUtil.getClassAttribute(importlist.get(0));
            //获取对象get方法
            List<Method> methodList = ClassUtil.getMethodGet(importlist.get(0));
            //创建普通行
            for (int i = 0; i < datalist.size(); i++) {
                //因为第一行已经用于创建标题行，故从第二行开始创建
                row = sheet.createRow(index++);
                //如果是第一行就让其为标题行
                Object targetObj = datalist.get(i);
                for (int j = 0; j < fields.length; j++) {
                    //创建列
                    Cell cell = row.createCell(j);
                    cell.setCellType(CellType.STRING);
                    //
                    try {
                        Object value = methodList.get(j).invoke(targetObj, new Object[]{});
                        cell.setCellValue(transCellType(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        response.setContentType("application/octet-stream");
        //默认Excel名称
        response.setHeader("Content-Disposition", "attachment;fileName=" + "Shmashine.xls");

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取xlsx文件
     *
     * @param file
     * @param startRow
     * @return
     */
    public static List<List<String>> readXlsxData(MultipartFile file, int startRow) {
        XSSFWorkbook xssfWorkbook = null;

        try {
            InputStream inputStream = file.getInputStream();
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
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

                if (xssfCell.getCellType() == CellType.NUMERIC) {
                    if (org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate(xssfCell.getNumericCellValue())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        subList.add(sdf.format(xssfCell.getDateCellValue()));
                    }
                } else {
                    subList.add(getString(xssfCell));
                }

            }
            list.add(subList);

        }
        return list;
    }

    private static String transCellType(Object value) {
        String str = null;
        if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
        } else {
            str = String.valueOf(value);
            if (str == "null") {
                str = "";
            }
        }

        return str;
    }


    public static void exprotMaintenance(HttpServletResponse response, List<Map> dataList, String[] attributeNames) {

        if (dataList.size() <= 0 || dataList.isEmpty()) {
            return;
        }

        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        //设置表格默认列宽度为35个字节
        sheet.setDefaultColumnWidth((short) 35);

        //循环字段名数组，创建标题行
        int index = 0;
        Row row = null;
        if (attributeNames != null) {
            row = sheet.createRow(index++);
            //获取字段名数组
            String[] tableAttributeName = attributeNames;
            for (int j = 0; j < tableAttributeName.length; j++) {
                //创建列
                Cell cell = row.createCell(j);
                //设置单元类型为String
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(tableAttributeName[j]));
            }
        }

        //创建普通数据行
        if (dataList != null && dataList.size() > 0) {
            //创建普通行
            for (int i = 0; i < dataList.size(); i++) {
                //因为第一行已经用于创建标题行，故从第二行开始创建
                row = sheet.createRow(index++);
                //如果是第一行就让其为标题行
                //电梯编号
                String elevatorName = (String) dataList.get(i).get("v_elevator_name");
                Cell cell = row.createCell(0);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(elevatorName));

                //地址
                String vAddress = (String) dataList.get(i).get("v_address");
                cell = row.createCell(1);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(vAddress));

                //类型
                String type = (String) dataList.get(i).get("order_type_number");
                cell = row.createCell(2);
                cell.setCellType(CellType.STRING);
                String typeZh = transType(type);
                cell.setCellValue(transCellType(typeZh));

                //工单状态
                String iStatus = (String) dataList.get(i).get("i_status");
                cell = row.createCell(3);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(iStatus));


                //应完成日期
                Date shouldCompleteDate = (Date) dataList.get(i).get("should_complete_date");
                cell = row.createCell(4);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(shouldCompleteDate));

                //完成时间
                Date completeTime = (Date) dataList.get(i).get("complete_time");
                cell = row.createCell(5);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(transCellType(completeTime));

            }
        }

        response.setContentType("application/octet-stream");
        //默认Excel名称
        response.setHeader("Content-Disposition", "attachment;fileName=" + "ShmashineMaintenance.xls");

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * BYLX01 0 半月保
     * BYLX02 0 季度保
     * BYLX03 0 半年保
     * BYLX04 0 年度保
     *
     * @param type
     * @return
     */
    private static String transType(String type) {
        switch (type) {
            case "BYLX01":
                return "半月保";
            case "BYLX02":
                return "季度保";
            case "BYLX03":
                return "半年保";
            case "BYLX04":
                return "年度保";
            default:
                return "";
        }
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