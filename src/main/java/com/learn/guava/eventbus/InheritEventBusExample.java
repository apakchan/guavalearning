package com.learn.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.learn.guava.eventbus.listener.ConcreteListener;

public class InheritEventBusExample {
    public static void main(String[] args) {
        final EventBus eventBus = new EventBus();
        eventBus.register(new ConcreteListener());
        eventBus.post("event coming");
    }
}
