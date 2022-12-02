package com.benym.benchmark.test.utils;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Time : 2022/7/7 21:56
 */
public class RpasBeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(RpasBeanUtils.class);

    /**
     * 享元模式
     */
    private static final Map<String, BeanCopier> bencopierMap = new ConcurrentReferenceHashMap<>();

    public RpasBeanUtils() {
    }

    /**
     * 无converter模式下获取BeanCopier实例
     *
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @return BeanCopier
     */
    public static <S,T> BeanCopier getBeanCopierWithNoConverter(Class<S> sourceClass, Class<T> targetClass) {
        StringBuffer beanKey = new StringBuffer();
        beanKey.append(sourceClass);
        beanKey.append(targetClass);
        BeanCopier beanCopier;
        String beanKeyStr = beanKey.toString();
        if (!bencopierMap.containsKey(beanKeyStr)) {
            synchronized (RpasBeanUtils.class) {
                if (!bencopierMap.containsKey(beanKeyStr)) {
                    beanCopier = BeanCopier.create(sourceClass,targetClass,false);
                    bencopierMap.put(beanKeyStr,beanCopier);
                } else {
                    beanCopier = bencopierMap.get(beanKeyStr);
                }
            }
        } else {
            beanCopier = bencopierMap.get(beanKeyStr);
        }
        return beanCopier;
    }

    /**
     * converter模式下获取BeanCopier实例
     *
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @return BeanCopier
     */
    private static <S,T> BeanCopier getBeanCopierWithConverter(Class<S> sourceClass, Class<T> targetClass, Converter converter) {
        StringBuffer beanKey = new StringBuffer();
        beanKey.append(sourceClass);
        beanKey.append(targetClass);
        beanKey.append(converter.toString());
        BeanCopier beanCopier;
        String beanKeyStr = beanKey.toString();
        if (!bencopierMap.containsKey(beanKeyStr)) {
            synchronized (RpasBeanUtils.class) {
                if (!bencopierMap.containsKey(beanKeyStr)) {
                    beanCopier = BeanCopier.create(sourceClass,targetClass, true);
                    bencopierMap.put(beanKeyStr,beanCopier);
                } else {
                    beanCopier = bencopierMap.get(beanKeyStr);
                }
            }
        } else {
            beanCopier = bencopierMap.get(beanKeyStr);
        }
        return beanCopier;
    }

    /**
     * 拷贝source对象属性到target中，不使用converter，名字和类型不严格匹配时将不拷贝
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copy(Object source, Object target) {
        BeanCopier beanCopier = getBeanCopierWithNoConverter(source.getClass(), target.getClass());
        beanCopier.copy(source, target, null);
    }

    /**
     * 拷贝source对象属性到target class中，不使用converter，名字和类型不严格匹配时将不拷贝
     *
     * @param source 源对象
     * @param clazz 目标类
     * @return copy后的对象
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T target;
        try {
            target = clazz.newInstance();
            copy(source, target);
        } catch (Exception e) {
            logger.warn("RpasBeanUtils copy exception:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 拷贝source对象属性到target中，使用自定义converter，拷贝规则严格符合converter规则
     *
     * @param source 源对象
     * @param target 目标对象
     * @param converter 自定义converter
     */
    public static void copyWithConverter(Object source, Object target, Converter converter) {
        BeanCopier beanCopier = getBeanCopierWithConverter(source.getClass(), target.getClass(), converter);
        beanCopier.copy(source, target, converter);
    }

    /**
     * 拷贝source对象属性到target class中，使用自定义converter，拷贝规则严格符合converter规则
     *
     * @param source 源对象
     * @param clazz 目标类
     * @param converter 自定义converter
     * @return copy后的对象
     */
    public static <T> T copyWithConverter(Object source, Class<T> clazz, Converter converter) {
        if (source == null) {
            return null;
        }
        T target;
        try {
            target = clazz.newInstance();
            copyWithConverter(source, target, converter);
        } catch (Exception e) {
            logger.warn("RpasBeanUtils copy with converter exception:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 拷贝源list对象到新class list
     *
     * @param sources 源对象List
     * @param clazz 目标类
     * @return 拷贝后的新class list
     */
    public static <T> List<T> copyToList(List<?> sources, Class<T> clazz) {
        if (sources == null) {
            return null;
        }
        List<T> targets = new ArrayList<>(sources.size());
        if (!sources.isEmpty()) {
            sources.forEach(source -> {
                T clone = copy(source, clazz);
                targets.add(clone);
            });
        }
        return targets;
    }

    /**
     * 拷贝源list对象到新class list，使用自定义converter
     *
     * @param sources 源对象List
     * @param clazz 目标类
     * @param converter 自定义converter
     * @return 拷贝后的新class list
     */
    public static <T> List<T> copyToList(List<?> sources, Class<T> clazz, Converter converter) {
        if (sources == null) {
            return null;
        }
        List<T> targets = new ArrayList<>(sources.size());
        if (!sources.isEmpty()) {
            sources.forEach(source -> {
                T clone = copyWithConverter(source, clazz, converter);
                targets.add(clone);
            });
        }
        return targets;
    }
}
