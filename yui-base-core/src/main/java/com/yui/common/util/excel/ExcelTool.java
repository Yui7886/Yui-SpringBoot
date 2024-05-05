package com.yui.common.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 解析Excel文件单元格内容
 *
 * @author yui
 */
public class ExcelTool {
    public static final String EMPTY = "";
    private static final String POINT = ".";

    /**
     * 获得path的后缀名
     *
     * @param path 文件路径
     * @return {@link String} 路径的后缀名
     */
    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1);
        }
        return EMPTY;
    }

    /**
     * 解析xls和xlsx不兼容问题
     *
     * @param filePath 文件路径
     * @return {@link Workbook}
     * @throws IOException io异常
     */
    public static Workbook getWorkBook(String filePath) throws IOException {
        Workbook workbook;
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        File file = new File(filePath);
        if (suffix.equals(ExcelUtils.XLS)) {
            POIFSFileSystem pfs = new POIFSFileSystem(file);
            workbook = new HSSFWorkbook(pfs);
            return workbook;
        } else if (suffix.equals(ExcelUtils.XLSX)) {
            try {
                workbook = new XSSFWorkbook(file);
                return workbook;
            } catch (IOException | InvalidFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 解析xls和xlsx不兼容问题
     *
     * @param file
     * @return
     */
    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        if (filename.endsWith("xls")) {
            POIFSFileSystem pfs = new POIFSFileSystem(file.getInputStream());
            workbook = new HSSFWorkbook(pfs);
            return workbook;
        } else if (filename.endsWith("xlsx")) {
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
                return workbook;
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
