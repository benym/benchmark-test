package com.benym.benchmark.test.MhExceptionBenchMark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @date: 2022/12/5 14:00
 */
@Fork(1) // Fork 1个线程进行测试
@BenchmarkMode(Mode.AverageTime) // 平均时间
@Warmup(iterations = 3) // JIT预热
@Measurement(iterations = 10, time = 5) // 迭代10次,每次5s
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 结果所使用的时间单位
@Threads(10) // 线程10个
public class MhExceptioTest {

    @State(Scope.Benchmark)
    public static class MhNoLambda{
        MethodHandle methodHandle;

        @Setup(Level.Trial)
        public void prepare() throws NoSuchMethodException, IllegalAccessException {
            MethodType methodType = MethodType.methodType(void.class, String.class);
            methodHandle = MethodHandles.publicLookup().findConstructor(ValidException.class, methodType);
        }
    }

    @State(Scope.Benchmark)
    public static class MhLambda{
        Function<String,AbstractException> function;

        @Setup(Level.Trial)
        public void prepare(){
            function = MethodAccessor.createConstruct(ValidException.class);
        }
    }

    @State(Scope.Benchmark)
    public static class Constr{
        Constructor<ValidException> constructor;

        @Setup(Level.Trial)
        public void prepare() throws NoSuchMethodException {
            constructor = ValidException.class.getConstructor(String.class);
        }
    }


    @Benchmark
    public <T extends AbstractException> AbstractException directNew(){
        ValidException validException = new ValidException("test");
        return validException;
    }

    @Benchmark
    public <T extends AbstractException> AbstractException mhNoLamda(MhNoLambda mhNoLambda) throws Throwable {
        return (AbstractException) mhNoLambda.methodHandle.invoke("test");
    }

    @Benchmark
    public <T extends AbstractException> AbstractException reflet(Constr constr) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ValidException validException = constr.constructor.newInstance("test");
        return validException;
    }

    @Benchmark
    public <T extends AbstractException> AbstractException mhLamda(MhLambda mhLambda){
        return mhLambda.function.apply("test");
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MhExceptioTest.class.getSimpleName())
                .result("./result-mh-ex.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
