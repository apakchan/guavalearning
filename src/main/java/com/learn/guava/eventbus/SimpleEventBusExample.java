package com.learn.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.learn.guava.eventbus.listener.SimpleListener;

public class SimpleEventBusExample {
    public static void main(String[] args) {
        final EventBus eventBus = new EventBus();
        eventBus.register(new SimpleListener());
        eventBus.post("123");
    }
}
