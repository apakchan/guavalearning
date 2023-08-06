package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleListener.class);

    @Subscribe
    public void doAction(final String event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("received event [{}] and will take action", event);
        }

    }
}
