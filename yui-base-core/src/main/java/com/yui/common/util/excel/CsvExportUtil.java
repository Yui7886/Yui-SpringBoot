package com.yui.common.util.excel;

import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.csv.export.CsvExportService;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import cn.afterturn.easypoi.handler.inter.IWriter;
import cn.hutool.core.collection.CollectionUtil;
import com.yui.common.util.tools.StrUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * csv 导出工具类
 *
 * @author yui
 */
public class CsvExportUtil {

    private CsvExportUtil() {
    }


    /**
     * export csv all list
     *
     * @param params
     * @param pojoClass
     * @param dataSet
     * @param outputStream
     */
    public static void exportCsv(CsvExportParams params, Class<?> pojoClass, Collection<?> dataSet, OutputStream outputStream) throws IllegalAccessException {
        IWriter<Void> writer = new CsvExportService(outputStream, params, pojoClass);
        writer.write(processValueForCsv(dataSet));
        writer.close();
    }

    /**
     * 导出csv文件
     *
     * @param params
     * @param pojoClass
     * @param dataSet
     * @param response
     */
    public static void exportCsvUtf8(CsvExportParams params, Class<?> pojoClass, Collection<?> dataSet, HttpServletResponse response) throws IOException, IllegalAccessException {
        // 中文乱码：以CSV方式导出的文件中默认不含BOM信息，通过给将要输出的内容设置BOM标识(以 EF BB BF 开头的字节流)即可解决该问题
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        exportCsv(params, pojoClass, dataSet, outputStream);
    }

    /**
     * export csv use server
     *
     * @param params
     * @param pojoClass
     * @param server
     * @param queryParams
     * @param outputStream
     */
    public static void exportCsv(CsvExportParams params, Class<?> pojoClass, IExcelExportServer server,
                                 Object queryParams, OutputStream outputStream) {
        IWriter<Void> writer = new CsvExportService(outputStream, params, pojoClass);
        int page = 1;
        List<Object> dataSet;
        while ((dataSet = server.selectListForExcelExport(queryParams, page)) != null && !dataSet.isEmpty()) {
            page++;
            writer.write(dataSet);
        }
        writer.close();
    }

    /**
     * @param params    表格标题属性
     * @param pojoClass Excel对象Class
     */
    public static IWriter<Void> exportCsv(CsvExportParams params, Class<?> pojoClass, OutputStream outputStream) {
        return new CsvExportService(outputStream, params, pojoClass);
    }

    /**
     * 根据Map创建对应的Excel
     *
     * @param params     表格标题属性
     * @param entityList Map对象列表
     */
    public static IWriter<Void> exportCsv(CsvExportParams params, List<ExcelExportEntity> entityList, OutputStream outputStream) {
        return new CsvExportService(outputStream, params, entityList);
    }

    public static <T> Collection<T> processValueForCsv(Collection<T> dataSet) throws IllegalAccessException {
        if (CollectionUtil.isEmpty(dataSet)) {
            return new ArrayList<>();
        }
        for (Object obj : dataSet) {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(obj) instanceof String) {
                    String value = field.get(obj) == null ? "" : field.get(obj).toString();
                    if (value.contains(StrUtil.DOUBLE_QUOTATION_MARKS)) {
                        value = value.replaceAll(StrUtil.DOUBLE_QUOTATION_MARKS, "\"\"");
                    }
                    field.set(obj, value);
                }
            }
        }
        return dataSet;
    }
}
