package com.learn.guava.eventbus.impl.test;

import com.learn.guava.eventbus.impl.internal.MyEventBus;

public class MyEventBusExample {
    public static void main(String[] args) {
        MyEventBus myEventBus = new MyEventBus();
        myEventBus.register(new MySimpleListener());
        myEventBus.post("123", "my_topic");
    }
}
