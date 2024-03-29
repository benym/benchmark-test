package com.benym.benchmark.test.MhExceptionBenchMark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 对于非私有变量访问的场景，MethodHandle可以和LambdaMetafactory.metafactory任意lambda函数结合，做到更通用化，此处为和Function结合
 * 采用纳秒为单位，数值越小越快，从上至下，直接new set、lambda mh、无lambda的mh、反射
 * Benchmark                                      Mode  Cnt     Score     Error  Units
 * MhExceptionBenchMark.MhExceptioTest.directNew  avgt   10  2421.192 ± 165.195  ns/op
 * MhExceptionBenchMark.MhExceptioTest.mhLamda    avgt   10  2589.443 ± 204.428  ns/op
 * MhExceptionBenchMark.MhExceptioTest.mhNoLamda  avgt   10  2664.148 ± 217.869  ns/op
 * MhExceptionBenchMark.MhExceptioTest.reflet     avgt   10  2710.181 ± 304.747  ns/op
 *
 * @date: 2022/12/1 14:30
 */
public final class MethodAccessor {

    private static final Logger logger = LoggerFactory.getLogger(MethodAccessor.class);

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static final MethodType methodType = MethodType.methodType(void.class, String.class);

    private static final ConcurrentHashMap<String, Function<String, AbstractException>> cacheFunction = new ConcurrentHashMap<>();

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
            Function<String, AbstractException> function = cacheFunction.get(cls.toString());
            if (function != null) {
                return applyMessage(function, message);
            }
            function = MethodAccessor.createConstruct(cls);
            cacheFunction.putIfAbsent(cls.toString(), function);
            return applyMessage(function, message);
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
            logger.warn("LambdaMetafactory create construct异常:", throwable);
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 根据Function函数和异常message，调用对应构造函数方法
     *
     * @param function function函数
     * @param message  异常消息
     * @return AbstractException
     */
    public static AbstractException applyMessage(Function<String, AbstractException> function, String message) {
        try {
            return function.apply(message);
        } catch (Throwable throwable) {
            logger.warn("LambdaMetafactory function apply异常:", throwable);
            throw new RuntimeException(throwable);
        }
    }
}
