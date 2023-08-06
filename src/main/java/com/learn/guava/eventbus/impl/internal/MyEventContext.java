package com.learn.guava.eventbus.impl.internal;

import java.lang.reflect.Method;

public interface MyEventContext {
    String getSource();

    Object getSubscriber();

    Method getSubscriberMethod();

    Object getEvent();
}
