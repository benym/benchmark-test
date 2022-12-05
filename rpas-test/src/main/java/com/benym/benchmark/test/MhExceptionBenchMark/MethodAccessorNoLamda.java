package com.benym.benchmark.test.MhExceptionBenchMark;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: yuanming3
 * @date: 2022/12/5 14:45
 */
public class MethodAccessorNoLamda {

    private static final MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

    private static final MethodType methodType = MethodType.methodType(void.class, String.class);

    private static final ConcurrentHashMap<String, MethodHandle> cacheException = new ConcurrentHashMap<>();

    /**
     * 方法句柄是一个有类型的，可以直接执行的指向底层方法、构造器、field等的引用
     * 可以简单理解为函数指针，它是一种更加底层的查找、调整和调用方法的机制
     *
     * 提供比反射更高效的映射方法
     * 效率上与原生调用仅有纳秒级差距
     *
     * @param cls 动态推断的class
     * @param message 需要抛出给前端的信息
     * @param <T> class类型
     * @return AbstractException或其子类
     */
    public static <T extends AbstractException> AbstractException getException(Class<T> cls, String message) {
        try {
            MethodHandle methodHandle = cacheException.get(cls.toString());
            if (methodHandle != null) {
                return (AbstractException) methodHandle.invoke(message);
            }
            methodHandle = publicLookup.findConstructor(cls, methodType);
            cacheException.putIfAbsent(cls.toString(), methodHandle);
            Object invoke = methodHandle.invoke(message);
            return (AbstractException) invoke;
        } catch (Throwable throwable) {
            throw new RuntimeException("获取cache exception异常", throwable);
        }
    }
}
