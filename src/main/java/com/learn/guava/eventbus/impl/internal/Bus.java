package com.learn.guava.eventbus.impl.internal;

public interface Bus {
    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Object object);

    void post(Object object, String topic);

    void close();

    String getBusName();
}
