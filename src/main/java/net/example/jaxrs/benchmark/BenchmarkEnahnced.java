package net.example.jaxrs.benchmark;

import java.util.concurrent.TimeUnit;

import net.example.jaxrs.RequestMatching;
import net.example.jaxrs.RequestMatchingEnhanced;

public class BenchmarkEnahnced extends RequestMatchingBenchmark {

    @Override
    protected RequestMatching createRequestMatcher() {
        return RequestMatchingEnhanced.of(simpleResources);
    }

    public static void main(String[] args) throws Exception {
        RequestMatchingBenchmark benchmark = new BenchmarkEnahnced();
        benchmark.run(durationSeconds(args), TimeUnit.SECONDS);
        System.out.println("[B] " + benchmark.count);
    }

}
