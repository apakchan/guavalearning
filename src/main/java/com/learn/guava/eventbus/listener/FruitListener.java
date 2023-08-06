package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.Subscribe;
import com.learn.guava.eventbus.event.Fruit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FruitListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FruitListener.class);

    @Subscribe
    public void eat(Fruit event) {

    }
}
