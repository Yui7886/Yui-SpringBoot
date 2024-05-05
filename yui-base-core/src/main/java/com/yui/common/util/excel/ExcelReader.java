package com.yui.common.util.excel;

import cn.hutool.core.date.DateUtil;
import com.yui.common.util.tools.StrUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel 阅读器
 *
 * @author lipx
 */
@Component
public class ExcelReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);
    private static ExcelReader excelReader;

    private Workbook workBook;
    private Map<Sheet, String[]> sheetHeaders;

    public ExcelReader() {
        super();
    }

    public ExcelReader(String filePath) {
        this(new File(filePath));
    }

    public ExcelReader(File file) {
        // 解决版本问题，HSSFWorkbook是97-03版本的xls版本，XSSFWorkbook是07版本的xlsx
        try {
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } catch (Exception e) {
            try {
                workBook = new HSSFWorkbook(new FileInputStream(file));
            } catch (Exception e1) {
                LOGGER.error("Excel格式不正确", e1);
                throw new RuntimeException(e1);
            }
        }
    }

    public ExcelReader(InputStream inputStream) {
        // 解决版本问题，HSSFWorkbook是97-03版本的xls版本，XSSFWorkbook是07版本的xlsx
        try {
            workBook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            try {
                workBook = new HSSFWorkbook(inputStream);
            } catch (Exception e1) {
                LOGGER.error("Excel格式不正确", e1);
                throw new RuntimeException(e1);
            }
        }
    }

    @PostConstruct
    private void init() {
        excelReader = this;
        excelReader.projectBasicService = this.projectBasicService;
        excelReader.personBasicService = this.personBasicService;
    }

    /**
     * 初始化sheet和表头信息，默认以每个sheet的第一行作为表头
     */
    private void initDefaultSheetHeaders() {
        sheetHeaders = new LinkedHashMap<>();

        if (workBook == null) {
            return;
        }

        int numberOfSheets = workBook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workBook.getSheetAt(i);
            String sheetName = workBook.getSheetName(i);
            LOGGER.debug("sheetName[{}]: {}", i, sheetName);

            // 默认以第一行作为表头
            Row row = sheet.getRow(0);
            if (row == null) {
                sheetHeaders.put(sheet, new String[]{});
                continue;
            }

            String[] headers = new String[]{};
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                String cellValue = getCellValue(row.getCell(j));
                headers = ArrayUtils.add(headers, cellValue);
            }
            sheetHeaders.put(sheet, headers);
        }
    }


    /**
     * 读取cell的值
     *
     * @param cell         需要读取的cell
     * @param defaultValue 默认值
     * @return
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return StrUtil.emptyToDefault(cell.getStringCellValue(), "");
    }

    /**
     * POI3.15之后的读取方法(建议用这个)
     *
     * @param cell
     * @return
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        String cellValue = null;
        switch (cell.getCellType()) {
            case NUMERIC -> {
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    cellValue = DateUtil.format(cell.getDateCellValue(), DateUtil);
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    cellValue = String.valueOf(nf.format(cell.getNumericCellValue())).replace(",", "");
                }
            }
            case STRING -> cellValue = cell.getStringCellValue();
            case BOOLEAN -> cellValue = String.valueOf(cell.getBooleanCellValue());
            case ERROR -> cellValue = "错误类型";
            default -> cellValue = "";
        }
        return cellValue.trim();
    }

}
