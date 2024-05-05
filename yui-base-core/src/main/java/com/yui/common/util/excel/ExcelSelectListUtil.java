package com.yui.common.util.excel;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * Excel 选择列表实用程序
 *
 * @author lkc
 */
public class ExcelSelectListUtil {
    public static void selectList(Workbook workbook, int firstCol, int lastCol, String[] strings) {
        Sheet sheet = workbook.getSheetAt(0);
        // 生成下拉列表
        // 只对(x，x)单元格有效
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(3, 65535, firstCol, lastCol);
        // 生成下拉框内容
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(strings);
        HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
        // 对sheet页生效
        sheet.addValidationData(dataValidation);
    }
}
