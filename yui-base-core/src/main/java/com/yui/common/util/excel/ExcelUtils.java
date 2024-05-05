package com.yui.common.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.yui.common.util.tools.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Excel 实用程序
 *
 * @author yui
 */
public class ExcelUtils {

    public static final String XLS = "xls";

    public static final String XLSX = "xlsx";

    /**
     * 得到Workbook对象
     *
     * @param file 文件
     * @return {@link Workbook}
     * @throws IOException io异常
     */
    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        // 这样写  excel 能兼容03和07
        InputStream is = file.getInputStream();
        Workbook hssfWorkbook;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (Exception ex) {
            is = file.getInputStream();
            hssfWorkbook = new XSSFWorkbook(is);
        }
        return hssfWorkbook;
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
                                   boolean isCreateHeader, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
                                   HttpServletResponse response) {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    public static void exportExcel(List<?> list, ExportParams exportParams, Class<?> pojoClass, String fileName,
                                   HttpServletResponse response) {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * 导出map
     */
    public static void exportExcelForMap(List<?> list, String title, String sheetName, String fileName,
                                         List<ExcelExportEntity> beanList, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), beanList, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static void exportExcelSelect(List<?> list, String title, String sheetName, Class<?> pojoClass,
                                         String fileName, HttpServletResponse response,
                                         List<Map<String, Object>> selectList) {
        selectExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName), selectList);
    }

    private static void selectExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response,
                                     ExportParams exportParams, List<Map<String, Object>> selectList) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        for (Map<String, Object> map : selectList) {
            ExcelSelectListUtil.selectList(workbook, Integer.parseInt(map.get("x").toString()),
                    Integer.parseInt(map.get("y").toString()), (String[]) map.get("select"));
        }
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName,
                                      HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel;charset=GBK");
            String contentDisposition = "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", contentDisposition);
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // throw new NormalException(e.getMessage());
        }
    }

    public static void downLoadExcelXlsx(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", ExcelUtil.XLSX_CONTENT_TYPE);
            String contentDisposition = "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", contentDisposition);
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // throw new NormalException(e.getMessage());
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            // throw new NormalException("模板不能为空");
        } catch (Exception e) {
            // throw new NormalException(e.getMessage());
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (Exception ignored) {
        }
        return list;
    }

    public static void writeCellValue(Sheet sheet, int rowIndex, int colIndex, int valueType, CellStyle cellStyle, Object value) {
        if (sheet == null) {
            return;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
            DataFormat format = sheet.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(format.getFormat("@"));
            cell.setCellStyle(cellStyle);
            cell.setCellValue("");
        }
        writeCellValue(cell, valueType, cellStyle, value);
    }


    public static void writeCellValue(Cell cell, int valueType, CellStyle cellStyle, Object value) {
        if (cell == null || value == null) {
            return;
        }
        switch (valueType) {
            case 3, 5, 1 -> cell.setCellValue(value.toString());
            case 4 -> cell.setCellValue(Boolean.parseBoolean(value.toString()));
            case 0 -> cell.setCellValue(Double.parseDouble(value.toString()));
            case 2 -> cell.setCellFormula(value.toString());
            default -> {
            }
        }
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
    }

    /***
     * 获取当前SHEET 所有Cell集合
     *
     * @param sheet 表
     * @return {@link List}<{@link Cell}>
     */
    public static List<Cell> getAllCell(Sheet sheet) {
        List<Cell> list = new ArrayList<>();
        int maxRow = sheet.getLastRowNum();
        int maxRol;
        for (int row = 0; row <= maxRow; row++) {
            Row rowObj = sheet.getRow(row);
            if (null == rowObj) {
                continue;
            }
            maxRol = rowObj.getLastCellNum();
            for (int rol = 0; rol < maxRol; rol++) {
                Cell cell = rowObj.getCell(rol);
                if (null == cell || StrUtil.isNotBlank(cell.toString())) {
                    continue;
                }
                list.add(cell);
            }
        }
        return list;
    }

    public static void exportExcelForMutiSheet(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = new HSSFWorkbook();
        for (Map<String, Object> map : list) {
            new ExcelExportService().createSheetForMap(workbook, (ExportParams) map.get("title"),
                    ObjectUtil.toList(map.get("entity"), ExcelExportEntity.class),
                    (Collection<?>) map.get("data"));
        }
        downLoadExcel(fileName, response, workbook);
    }
}