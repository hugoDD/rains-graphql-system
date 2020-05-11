package com.rains.graphql.common.utils;

import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.rains.graphql.common.converter.DateTimeConverter;
import com.rains.graphql.common.exception.BeanTransMapException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.cglib.beans.BeanMap;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class BeanMapUtils {
    private BeanMapUtils() {
    }

    /**
     * 将对象装换为 map,对象转成 map，key肯定是字符串
     *
     * @param bean 转换对象
     * @return 返回转换后的 map 对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object bean) {
        return null == bean ? null : BeanMap.create(bean);
    }

    /**
     * map 装换为 java bean 对象
     *
     * @param map   转换 MAP
     * @param clazz 对象 Class
     * @return 返回 bean 对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        T bean = ClassUtils.newInstance(clazz);
        try {
            DateTimeConverter dtConverter = new DateTimeConverter();
            ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
            convertUtilsBean.deregister(Date.class);
            convertUtilsBean.register(dtConverter, Date.class);
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean,
                    new PropertyUtilsBean());
            beanUtilsBean.populate(bean, map);
        } catch (Exception e) {
            throw new BeanTransMapException(e);
        }
        return bean;
    }

    /**
     * List&lt;T&gt; 转换为 List&lt;Map&lt;String, Object&gt;&gt;
     *
     * @param beans 转换对象集合
     * @return 返回转换后的 bean 列表
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> beans) {
        if (CollectionUtils.isEmpty(beans)) {
            return Collections.emptyList();
        }
        return beans.stream().map(BeanUtils::beanToMap).collect(toList());
    }

    /**
     * List&lt;Map&lt;String, Object&gt;&gt; 转换为 List&lt;T&gt;
     *
     * @param maps  转换 MAP 集合
     * @param clazz 对象 Class
     * @return 返回转换后的 bean 集合
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) {
        if (CollectionUtils.isEmpty(maps)) {
            return Collections.emptyList();
        }
        return maps.stream().map(e -> mapToBean(e, clazz)).collect(toList());
    }

    public static <T> List<T> objToBeans(Object[] maps, Class<T> clazz) {
        if (maps == null || maps.length == 0) {
            return Collections.emptyList();
        }
        Stream.of(maps).filter(e -> e.getClass().isAssignableFrom(clazz)).collect(toList());
        return Stream.of(maps).filter(e -> e instanceof Map).map(e -> mapToBean((Map<String, Object>) e, clazz)).collect(toList());
    }


}
