package com.learn.guava.utilities;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class StopWatchExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopWatchExample.class);

    public static void main(String[] args) throws InterruptedException {
        process("123");
    }

    private static void process(String productId) throws InterruptedException {
        LOGGER.info("processing [{}]", productId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUnit.SECONDS.sleep(1);
        LOGGER.info("product: [{}] end and elapsed [{}]", productId, stopwatch.stop());

    }
}
