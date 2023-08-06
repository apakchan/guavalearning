package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcreteListener extends BaseListener{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcreteListener.class);

    @Subscribe
    public void concreteTask(String event) {
        LOGGER.info("received event [{}] and will take action by [concreteTask]", event);
    }
}
