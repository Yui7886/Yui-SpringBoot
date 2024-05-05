package com.yui.common.util.tools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Object工具类
 *
 * @author yui
 */
public class ObjectUtil {

    /**
     * 将object转为list
     *
     * @param obj   object
     * @param clazz Class
     * @return {@link List}<{@link T}>
     */
    public static <T> List<T> toList(Object obj, Class<T> clazz) {
        List<T> result;
        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).stream().map(clazz::cast).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 将object转为map
     *
     * @param obj    object
     * @param kClass Class
     * @param vClass Class
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> toMap(Object obj, Class<K> kClass, Class<V> vClass) {
        Map<K, V> result = new HashMap<>(5);
        if (obj instanceof Map<?, ?>) {
            Set<? extends Map.Entry<?, ?>> entries = ((Map<?, ?>) obj).entrySet();
            for (Map.Entry<?, ?> entry : entries) {
                result.put(kClass.cast(entry.getKey()), vClass.cast(entry.getValue()));
            }
            return result;
        }
        return null;
    }

}
