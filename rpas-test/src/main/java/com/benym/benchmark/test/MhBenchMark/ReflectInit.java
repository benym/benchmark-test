package com.benym.benchmark.test.MhBenchMark;

import java.lang.reflect.Field;

/**
 * @date: 2022/12/3 13:52
 */
public class ReflectInit {

    public static Field init(Class<?> clazz, String fieldName) {
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }
}
