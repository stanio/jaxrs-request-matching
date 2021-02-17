package net.example.jaxrs.benchmark;

import java.util.concurrent.TimeUnit;

import net.example.jaxrs.RequestMatchingSpec;

public class BenchmarkSpec extends RequestMatchingBenchmark {

    public BenchmarkSpec() {
        super(RequestMatchingSpec.of(simpleResources));
    }

    public static void main(String[] args) throws Exception {
        RequestMatchingBenchmark benchmark = new BenchmarkSpec();
        benchmark.run(durationSeconds(args), TimeUnit.SECONDS);
        System.out.println("[A] " + benchmark.count);
    }

}
