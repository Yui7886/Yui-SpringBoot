package com.yui.common.util.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import org.apache.poi.ss.usermodel.*;

/**
 * 处理EasyPoi导出数据类型转数值问题
 *
 * @author yui
 */
public class ExcelExportStatisticStyler extends ExcelExportStylerDefaultImpl {

    private CellStyle numberCellStyle;

    public ExcelExportStatisticStyler(Workbook workbook) {
        super(workbook);
        createNumberCellStyler();
    }

    private void createNumberCellStyler() {
        numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setAlignment(HorizontalAlignment.CENTER);
        numberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 这边传的参数格式会影响你导出的数据类型
        // "0.00"是自定义类型可以与数字类型直接进行计算，并不是直接导出数字类型
        // "#,##0.00" 是货币类型 其他参数到源码查看
        numberCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0.00"));
        numberCellStyle.setWrapText(true);
    }

    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        // 自定义Dict转换
        if (entity != null && 10 == entity.getType()) {
            return numberCellStyle;
        }
        return super.getStyles(noneStyler, entity);
    }
}
