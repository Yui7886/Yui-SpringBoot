package com.yui.common.util.tools;

import cn.hutool.core.bean.BeanUtil;
import com.yui.common.util.exception.ProjectException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Bean 副本
 *
 * @author yui
 */
public class BeanCopy<S, T> {

    /**
     * 复制类
     *
     * @param dataList 数据来源
     * @param clazz    Class
     * @return {@link List}<{@link S}>
     */
    public List<S> copyList(List<T> dataList, Class<S> clazz) {
        List<S> returnList = new ArrayList<>();
        T from;
        S to;
        try {
            for (T t : dataList) {
                from = t;
                to = clazz.getDeclaredConstructor().newInstance();
                BeanUtil.copyProperties(to, from);
                returnList.add(to);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new ProjectException("复制类异常", e);
        }
        return returnList;
    }

    /**
     * List深度copy
     *
     * @param src   要拷贝的list
     * @param clazz Class
     * @return {@link List}<{@link T}>
     * @throws IOException            io异常
     * @throws ClassNotFoundException 未找到类异常
     */
    public static <T> List<T> deepCopy(List<T> src, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = getInputStream(src)) {
            return ObjectUtil.toList(in.readObject(), clazz);
        }
    }

    /**
     * Map深度copy
     *
     * @param src    要拷贝的Map
     * @param kClass Class
     * @param vClass Class
     * @return {@link Map}<{@link K}, {@link V}>
     * @throws IOException            io异常
     * @throws ClassNotFoundException 未找到类异常
     */
    public static <K, V> Map<K, V> cloneMap(Map<K, V> src, Class<K> kClass, Class<V> vClass)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = getInputStream(src)) {
            return ObjectUtil.toMap(ois.readObject(), kClass, vClass);
        }
    }

    private static ObjectInputStream getInputStream(Object src) throws IOException {
        // 写入字节流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream obs = new ObjectOutputStream(out);
        obs.writeObject(src);
        obs.close();
        // 分配内存，写入原始对象，生成新对象
        ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
        return new ObjectInputStream(ios);
    }
}
