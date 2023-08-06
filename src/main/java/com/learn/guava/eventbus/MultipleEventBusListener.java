package com.learn.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.learn.guava.eventbus.listener.MultipleEventListeners;

public class MultipleEventBusListener {
    public static void main(String[] args) {
        final EventBus eventBus = new EventBus();
        eventBus.register(new MultipleEventListeners());
        eventBus.post("i am a string");
        eventBus.post(123);
    }
}
