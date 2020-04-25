package com.rains.graphql.common.utils;

import java.util.List;

public class BeanUtils {
    /**
     * @param orig 源对象
     * @param dest 目标对象
     */
    public static void copyProperties(final Object orig, final Object dest) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param origs           源list对象
     * @param dests           目标list对象
     * @param origsElementTpe 源list元素类型对象
     * @param destElementTpe  目标list元素类型对象
     * @param <T1>            源list元素类型
     * @param <T2>            目标list元素类型
     * @Description：拷贝list元素对象，将origs中的元素信息，拷贝覆盖至dests中
     */
    public static <T1, T2> void copyProperties(final List<T1> origs, final List<T2> dests, Class<T1> origsElementTpe, Class<T2> destElementTpe) {
        if (origs == null || dests == null) {
            return;
        }
        if (dests.size() != 0) {
            //防止目标对象被覆盖，要求必须长度为零
            throw new RuntimeException("目标对象存在值");
        }
        try {
            for (T1 orig : origs) {
                T2 t = destElementTpe.newInstance();
                dests.add(t);
                copyProperties(orig, t);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
