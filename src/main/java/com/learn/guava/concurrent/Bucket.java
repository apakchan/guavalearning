package com.learn.guava.concurrent;

import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Bucket {

    private final ConcurrentLinkedQueue<Integer> container = new ConcurrentLinkedQueue<>();

    private final static int BUCKET_LIMIT = 500;

    // 10 operation per second
    private final RateLimiter rateLimiter = RateLimiter.create(10);

    private final Monitor offerMonitor = new Monitor();

    private final Monitor pollMonitor = new Monitor();

    public void submit(Integer data) {
        if (offerMonitor.enterIf(offerMonitor.newGuard(() -> container.size() < BUCKET_LIMIT))) {
            try {
                container.offer(data);
                System.out.println(Thread.currentThread() + " submit data: " + data + ", current size: " + container.size());
            } finally {
                offerMonitor.leave();
            }
        } else {
            throw new IllegalStateException("The Bucket is full.");
        }
    }

    public void takeThenConsume(Consumer<Integer> consumer) {
        if (pollMonitor.enterIf(pollMonitor.newGuard(() -> !container.isEmpty()))) {
            try {
                Integer data = container.poll();
                System.out.println(Thread.currentThread() + " consume data: " + data + ", current size: " + container.size());
                consumer.accept(data);
            } finally {
                pollMonitor.leave();
            }
        } else {
            throw new IllegalStateException("The Bucket is empty.");
        }
    }

    public static void main(String[] args) {
        final Bucket bucket = new Bucket();
        final AtomicInteger DATA_CREATER = new AtomicInteger(0);
        IntStream.range(0, 5).forEach(i -> {
            // this loop creates 25 data per second
            new Thread(() -> {
                while (true) {
                    try {
                        int data = DATA_CREATER.getAndIncrement();
                        bucket.submit(data);
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (Exception e) {
                        if (e instanceof IllegalStateException) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }).start();
        });

        IntStream.range(0, 5).forEach(i -> {
            // this loop creates 10 data per second
            new Thread(() -> {
                while (true) {
                    try {
                        bucket.takeThenConsume(data -> {
                        });
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (Exception e) {
                        if (e instanceof IllegalStateException) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }).start();
        });
    }

}
