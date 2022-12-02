package com.benym.benchmark.test.MhBenchMark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                              Mode  Cnt    Score    Error  Units
 * MethodHandleTest.MHBenchmark.testNoStaticMethodHandle  avgt   10  732.150 ± 40.476  ns/op
 * MethodHandleTest.MHBenchmark.testNoStaticReflection    avgt   10  439.412 ±  8.547  ns/op
 * MethodHandleTest.MHBenchmark.testStaticMethodHandle    avgt   10    1.561 ±  0.014  ns/op
 * MethodHandleTest.MHBenchmark.testStaticReflection      avgt   10   25.693 ±  0.543  ns/op
 * 纳秒为单位，数值越小越快
 *
 * @date: 2022/12/2 18:30
 */
@Fork(1) // Fork 1个线程进行测试
@BenchmarkMode(Mode.AverageTime) // 平均时间
@Warmup(iterations = 3) // JIT预热
@Measurement(iterations = 10, time = 5) // 迭代10次,每次5s
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 结果所使用的时间单位
@Threads(10) // 线程10个
public class MHBenchmark {

    private static final MethodHandle staticMethod = MethodAccessor.getCache(EntityWithNoSet.class + "testField");

    private static final Field field = ReflectInit.init(EntityWithNoSet.class, "testField");

    /**
     * 作用域为本次JMH测试，线程共享
     * <p>
     * 初始化source数据集
     */
    @State(Scope.Benchmark)
    public static class GenerateModel {
        EntityWithNoSet source;

        // 初始化
        @Setup(Level.Trial)
        public void prepare() {
            source = new EntityWithNoSet(123);
        }
    }

    @Benchmark
    public void testNoStaticReflection(GenerateModel generateModel) throws NoSuchFieldException, IllegalAccessException {
        EntityWithNoSet pageResponse = new EntityWithNoSet();
        Class<? extends EntityWithNoSet> EntityWithNoSetClass = pageResponse.getClass();
        Field testField = EntityWithNoSetClass.getDeclaredField("testField");
        testField.setAccessible(true);
        testField.set(pageResponse, generateModel.source.getTestField());
    }

    @Benchmark
    public void testStaticReflection(GenerateModel generateModel) throws NoSuchFieldException, IllegalAccessException {
        EntityWithNoSet pageResponse = new EntityWithNoSet();
        field.set(pageResponse, generateModel.source.getTestField());
    }

    @Benchmark
    public void testStaticMethodHandle(GenerateModel generateModel) throws Throwable {
        EntityWithNoSet pageResponse = new EntityWithNoSet();
        staticMethod.invoke(pageResponse, generateModel.source.getTestField());
    }

    @Benchmark
    public void testNoStaticMethodHandle(GenerateModel generateModel) throws Throwable {
        EntityWithNoSet pageResponse = new EntityWithNoSet();
        MethodAccessor.getCache(EntityWithNoSet.class + "testField").invoke(pageResponse, generateModel.source.getTestField());
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MHBenchmark.class.getSimpleName())
                .result("./result-mh.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
