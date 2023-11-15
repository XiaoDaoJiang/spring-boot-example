package com.example.demo;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class JMHExample {

    /**
     * 这里基准函数
     */
    @Benchmark
    public void jmhMethod() {

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                        .include(JMHExample.class.getSimpleName())  // 基准类
                        .forks(1)
                        .build();
        new Runner(opt).run();
    }
}