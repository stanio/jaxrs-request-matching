package net.example.jaxrs.benchmark;

import java.util.concurrent.TimeUnit;

import net.example.jaxrs.RequestMatchingEnhanced;

public class BenchmarkEnahnced extends RequestMatchingBenchmark {

    public BenchmarkEnahnced() {
        super(createRegistry());
    }

    static RequestMatchingEnhanced createRegistry() {
        return (RequestMatchingEnhanced) RequestMatchingEnhanced.of(simpleResources);
    }

    public static void main(String[] args) throws Exception {
        RequestMatchingBenchmark benchmark = new BenchmarkEnahnced();
        benchmark.run(durationSeconds(args), TimeUnit.SECONDS);
        System.out.println("[B] " + benchmark.count);
    }

}
