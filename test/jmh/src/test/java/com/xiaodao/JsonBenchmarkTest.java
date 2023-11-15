package com.xiaodao;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Threads(2)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Thread)
public class JsonBenchmarkTest {

    // 指定参数有三个值
    @Param(value = 
                {"{\"startDate\":\"2020-04-01 16:00:00\",\"endDate\":\"2020-05-20 13:00:00\",\"flag\":true,\"threads\":5,\"shardingIndex\":0}",
                "{\"startDate\":\"2020-04-01 16:00:00\",\"endDate\":\"2020-05-20 14:00:00\"}",
                "{\"flag\":true,\"threads\":5,\"shardingIndex\":0}"})
    private String jsonStr;

    @Benchmark
    // @Test
    public void testGson() {
        System.out.println(jsonStr);
    }

    @Benchmark
    // @Test
    public void testJackson() {
    }

}
