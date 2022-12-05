package com.benym.benchmark.test.MhExceptionBenchMark;

import com.benym.benchmark.test.MhBenchMark.EntityWithNoSet;
import com.benym.benchmark.test.MhBenchMark.MHBenchmark;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

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
    public static class Ex {
        ValidException validException;

        // 初始化
        @Setup(Level.Trial)
        public void prepare() {
            validException = new ValidException();
        }
    }

    @Benchmark
    public <T extends AbstractException> AbstractException directNew(Ex ex){
        ex.validException.setErrMessage("测试一下消息");
        return ex.validException;
    }

    @Benchmark
    public <T extends AbstractException> AbstractException mhNoLamda(){
        return MethodAccessorNoLamda.getException(ValidException.class, "测试一下消息");
    }

    @Benchmark
    public <T extends AbstractException> AbstractException mhLamda(){
        return MethodAccessor.getException(ValidException.class, "测试一下消息");
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
