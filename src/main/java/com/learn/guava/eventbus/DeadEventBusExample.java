package com.learn.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.learn.guava.eventbus.listener.DeadEventListener;

public class DeadEventBusExample {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus("DeadEventBus");
        eventBus.register(new DeadEventListener());
        eventBus.post("Hello");
        eventBus.post(123);
    }
}
