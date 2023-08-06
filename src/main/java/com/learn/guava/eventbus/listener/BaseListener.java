package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseListener extends AbstractListener{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseListener.class);

    @Subscribe
    public void baseTask(String event) {
        LOGGER.info("received event [{}] and will take action by [baseTask]", event);
    }
}
