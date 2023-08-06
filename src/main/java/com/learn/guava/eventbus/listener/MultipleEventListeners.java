package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleEventListeners {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleEventListeners.class);

    @Subscribe
    public void stringTask1(String event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("received event [{}] and will take action by [stringTask1]", event);
        }
    }

    @Subscribe
    public void stringTask2(String event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("received event [{}] and will take action by [stringTask2]", event);
        }
    }

    @Subscribe
    public void integerTask(Integer event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("received event [{}] and will take action by [integerTask]", event);
        }
    }
}
