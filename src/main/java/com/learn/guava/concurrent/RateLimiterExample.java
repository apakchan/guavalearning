package com.learn.guava.concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class RateLimiterExample {
    // 0.5 operation per second  eq  1 operation per 2 seconds
    private final static RateLimiter limiter = RateLimiter.create(0.5);

    private final static Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach(i -> {
            service.submit(RateLimiterExample::testSemaphore);
        });
        service.shutdown();
    }

    private static void testLimiter() {
        System.out.println(Thread.currentThread() + " waiting " + limiter.acquire());
    }

    private static void testSemaphore() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread() + " coming and working");
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread() + " release the semaphore");
        }
    }
}
