package com.yui.common.util.tools;

import com.mybatisflex.core.paginate.Page;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 汇总数据工具类
 *
 * @author yui
 */
public class SummaryDataUtil {

    public static <T> SummaryDataRecord<T> getSummaryDataRecord(Page<T> page) {
        List<T> records = page.getRecords();
        Map<String, BigDecimal> summaryDataMap = null;
        try {
            summaryDataMap = getSummaryDataMap(records);
        } catch (IllegalAccessException ignored) {

        }
        return new SummaryDataRecord<>(page.getTotalRow(), summaryDataMap, records);
    }

    private static <T> Map<String, BigDecimal> getSummaryDataMap(List<T> list) throws IllegalAccessException {
        Map<String, BigDecimal> summaryData = new HashMap<>(list.get(0).getClass().getDeclaredFields().length);
        for (T item : list) {
            Field[] fields = item.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(item) instanceof BigDecimal) {
                    BigDecimal a = Optional.ofNullable(summaryData.get(field.getName())).orElse(BigDecimal.ZERO);
                    BigDecimal b = Optional.ofNullable((BigDecimal) field.get(item)).orElse(BigDecimal.ZERO);
                    a = a.add(b);
                    summaryData.put(field.getName(), a);
                }
                field.setAccessible(false);
            }
        }
        return summaryData;
    }

    /**
     * 汇总数据记录
     *
     * @author yui
     */
    public record SummaryDataRecord<T>(Long total, Map<String, BigDecimal> summaryData, List<T> data) {}

}
