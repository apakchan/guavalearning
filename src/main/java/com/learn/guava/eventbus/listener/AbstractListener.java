package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractListener.class);

    @Subscribe
    public void abstractTask(String event) {
        LOGGER.info("received event [{}] and will take action by [abstractTask]", event);
    }
}
