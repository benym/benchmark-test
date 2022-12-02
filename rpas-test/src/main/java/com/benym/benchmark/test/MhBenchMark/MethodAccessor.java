package com.benym.benchmark.test.MhBenchMark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MethodHandle在各类开源框架中大量使用，如Mybatis等
 * 为什么使用MethodHandle
 * MethodHandle性能压测如何
 * @link {<a href="https://www.optaplanner.org/blog/2018/01/09/JavaReflectionButMuchFaster.html">...</a>}
 * <p/>
 * 由于在类外进行私有变量的访问
 * 在JDK8环境下
 * MethodHandle虽然单独有效，但和LambdaMetafactory.metafactory BiConsumer很难结合在一起
 * @link {<a href="https://stackoverflow.com/questions/28184065/java-8-access-private-member-with-lambda">...</a>}
 * 一种更hack的结合方法是采用MethodHandle的内部方法，但由于安全性和运行原理未知，暂不使用
 * @link {<a href="https://stackoverflow.com/questions/69068124/lambdametafactory-and-private-methods">...</a>}
 * <p/>
 * 在JDK9环境下MethodHandle提供privateLookupIn则才能和LambdaMetafactory.metafactory结合
 * <p/>
 * tip: 即使不采用LambdaMetafactory方式做到更为通用的MethodHandle, 如果使用时不采用static，则MethodHandle甚至慢于反射
 * 简单的静态化MethodHandle仍然是通过底层native执行方法，压测性能接近原生，远快于反射
 * <p/> 采用纳秒为单位，数值越小越快
 * Benchmark                                              Mode  Cnt    Score    Error  Units
 * MethodHandleTest.MHBenchmark.testNoStaticMethodHandle  avgt   10  732.150 ± 40.476  ns/op
 * MethodHandleTest.MHBenchmark.testNoStaticReflection    avgt   10  439.412 ±  8.547  ns/op
 * MethodHandleTest.MHBenchmark.testStaticMethodHandle    avgt   10    1.561 ±  0.014  ns/op
 * MethodHandleTest.MHBenchmark.testStaticReflection      avgt   10   25.693 ±  0.543  ns/op
 *
 * @date: 2022/12/2 14:30
 */
public final class MethodAccessor {
    private static final Logger logger = LoggerFactory.getLogger(MethodAccessor.class);

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static final ConcurrentHashMap<String, MethodHandle> methodHandleCache = new ConcurrentHashMap<>();

    static {
        initMethodHandles("testField");
    }

    private static void initMethodHandles(String fieldName) {
        try {
            String key = EntityWithNoSet.class + fieldName;
            MethodHandle cacheHandle = methodHandleCache.get(key);
            if (cacheHandle != null) {
                return;
            }
            Field field = EntityWithNoSet.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            MethodHandle methodHandle = lookup.unreflectSetter(field);
            methodHandleCache.putIfAbsent(key, methodHandle);
        } catch (Exception e) {
            logger.warn("MethodHandle初始化异常", e);
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getCache(String key) {
        return methodHandleCache.get(key);
    }
}
