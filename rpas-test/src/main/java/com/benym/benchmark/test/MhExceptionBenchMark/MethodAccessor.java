package com.benym.benchmark.test.MhExceptionBenchMark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * MethodHandle在各类开源框架中大量使用，如Mybatis等
 * 为什么使用MethodHandle
 * MethodHandle性能压测如何
 *
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
 * Benchmark                                      Mode  Cnt     Score     Error  Units
 * MhExceptionBenchMark.MhExceptioTest.directNew  avgt   10   154.341 ±  16.426  ns/op
 * MhExceptionBenchMark.MhExceptioTest.mhLamda    avgt   10   274.854 ±  13.552  ns/op
 * MhExceptionBenchMark.MhExceptioTest.mhNoLamda  avgt   10  3157.479 ± 173.273  ns/op
 * @date: 2022/12/1 14:30
 */
public final class MethodAccessor {

    private static final Logger logger = LoggerFactory.getLogger(MethodAccessor.class);

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static final MethodType methodType = MethodType.methodType(void.class, String.class);

    private static final ConcurrentHashMap<String, AbstractException> cacheException = new ConcurrentHashMap<>();

    /**
     * 方法句柄是一个有类型的，可以直接执行的指向底层方法、构造器、field等的引用
     * 可以简单理解为函数指针，它是一种更加底层的查找、调整和调用方法的机制
     * <p>
     * 提供比反射更高效的映射方法
     * 效率上与原生调用仅有纳秒级差距
     *
     * @param cls     动态推断的class
     * @param message 需要抛出给前端的信息
     * @param <T>     class类型
     * @return AbstractException或其子类
     */
    public static <T extends AbstractException> AbstractException getException(Class<T> cls, String message) {
        try {
            AbstractException abstractException = cacheException.get(cls.toString());
            if (abstractException != null) {
                return abstractException;
            }
            abstractException = MethodAccessor.createAndApply(cls, message);
            cacheException.putIfAbsent(cls.toString(), abstractException);
            return abstractException;
        } catch (Throwable throwable) {
            throw new RuntimeException("获取cache exception异常", throwable);
        }
    }

    /**
     * 参考@link{<a href="https://stackoverflow.com/questions/53675777/how-to-instantiate-an-object-using-lambdametafactory">...</a>}
     * 根据异常Class，动态通过LambdaMetafactory寻找构造函数
     *
     * @param cls 异常Class
     * @param <T> 异常Class类型
     * @return Function<String, AbstractException>
     */
    @SuppressWarnings("unchecked")
    public static <T> Function<String, AbstractException> createConstruct(Class<T> cls) {
        try {
            MethodHandle methodHandle = lookup.findConstructor(cls, methodType);
            CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    methodHandle.type().generic(),
                    methodHandle,
                    methodHandle.type());
            return (Function<String, AbstractException>) site.getTarget().invokeExact();
        } catch (Throwable throwable) {
            logger.warn("LambdaMetafactory创建构造函数异常:", throwable);
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 根据异常Class类型，和异常消息，动态进行构造函数构建，并赋值
     *
     * @param cls 异常Class
     * @param message 异常消息
     * @param <T> 异常Class类型
     * @return AbstractException
     */
    public static <T> AbstractException createAndApply(Class<T> cls, String message) {
        try {
            Function<String, AbstractException> function = createConstruct(cls);
            return function.apply(message);
        } catch (Throwable throwable) {
            logger.warn("LambdaMetafactory apply message异常:", throwable);
            throw new RuntimeException(throwable);
        }
    }
}
