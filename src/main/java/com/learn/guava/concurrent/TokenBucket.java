package com.learn.guava.concurrent;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;
import com.learn.guava.utilities.StopWatchExample;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucket {
    private AtomicInteger phoneNumber = new AtomicInteger(0);

    private final static int LIMIT = 100;

    private RateLimiter rateLimiter = RateLimiter.create(10);

    private final int limit;

    public TokenBucket() {
        this(LIMIT);
    }

    public TokenBucket(int limit) {
        this.limit = limit;
    }

    public int buy() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        boolean success = rateLimiter.tryAcquire(10, TimeUnit.SECONDS);
        if (success) {
            if (phoneNumber.get() >= limit) {
                throw new IllegalStateException("please wait to next time");
            }
            int phoneNum = this.phoneNumber.getAndIncrement();
            handlerOrder(phoneNum);
            System.out.println(Thread.currentThread() + " user get the phone: " + phoneNum + " ELT: " + stopwatch.stop());
            return phoneNum;
        } else {
            stopwatch.stop();
            throw new RuntimeException("Sorry, occur exception when buying.");
        }
    }

    private void handlerOrder(int phoneNum) {
        if (phoneNum >= limit) {
            throw new IllegalStateException("please wait to next time");
        }
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
