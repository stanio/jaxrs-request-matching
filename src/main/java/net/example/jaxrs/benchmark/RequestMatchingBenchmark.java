package net.example.jaxrs.benchmark;

import java.util.concurrent.TimeUnit;

import net.example.jaxrs.MatchInfo;
import net.example.jaxrs.MethodInfo;
import net.example.jaxrs.RequestMatching;
import net.example.jaxrs.rest.BarResource;
import net.example.jaxrs.rest.FooResource;
import net.example.jaxrs.rest.UnmatchedResource;

public abstract class RequestMatchingBenchmark {

    static Class<?>[] simpleResources = { FooResource.class,
                                          BarResource.class,
                                          //BarEnhanced.class,
                                          UnmatchedResource.class };

    volatile int count = 0;

    private final ThreadLocal<RequestMatching> requestMatchingLocal = new ThreadLocal<>();

    protected abstract RequestMatching createRequestMatcher();

    public void run(long duration, TimeUnit timeUnit) throws Exception {
        Thread throughput = new Thread(() -> {
            Thread currThread = Thread.currentThread();
            Thread.yield();
            try {
                while (!currThread.isInterrupted()) {
                    tryPath("/rest/foo");
                    count += 1;
                    tryPath("/rest/foo/bar");
                    count += 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        TimeUnit.SECONDS.toMillis(count);
        throughput.setDaemon(true);
        throughput.start();
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + timeUnit.toMillis(duration);
        do {
            throughput.join(Math.min(5_000L, endTime - currentTime));
            if (!throughput.isAlive()) {
                throw new RuntimeException("operation failed");
            }
            currentTime = System.currentTimeMillis();
        } while (endTime > currentTime);
        throughput.interrupt();
    }

    void tryPath(String path) {
        RequestMatching requestMatching = requestMatchingLocal.get();
        if (requestMatching == null) {
            requestMatching = createRequestMatcher();
            requestMatchingLocal.set(requestMatching);
        }
        MatchInfo<MethodInfo> method = requestMatching.find(path);
        if (method == null) throw new RuntimeException(path + " not found");
    }

    static long durationSeconds(String[] args) {
        long durationSeconds = 60;
        if (args.length > 0) {
            durationSeconds = Integer.parseInt(args[0]);
        }
        return durationSeconds;
    }

}
