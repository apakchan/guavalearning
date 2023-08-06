package com.learn.guava.eventbus.impl.internal;

import java.util.concurrent.Executor;

public class MyEventBus implements Bus{

    private final MyRegistry registry = new MyRegistry();

    private final String busName;

    private static final String DEFAULT_NAME = "default";

    private static final String DEFAULT_TOPIC = "default_topic";

    private final MyDispatcher dispatcher;

    public MyEventBus() {
        this(DEFAULT_NAME, null, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public MyEventBus(String busName) {
        this(busName, null, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public MyEventBus(MyEventExceptionHandler eventExceptionHandler) {
        this(DEFAULT_NAME, eventExceptionHandler, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public MyEventBus(String busName, MyEventExceptionHandler eventExceptionHandler) {
        this(busName, eventExceptionHandler, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }


    public MyEventBus(String busName, MyEventExceptionHandler eventExceptionHandler, Executor executor) {
        this.busName = busName;
        this.dispatcher = MyDispatcher.newDispatcher(executor, eventExceptionHandler);
    }

    @Override
    public void register(Object subscriber) {
        // registry
        registry.bind(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        registry.unbind(subscriber);
    }

    @Override
    public void post(Object object) {
        this.post(object, DEFAULT_TOPIC);
    }

    @Override
    public void post(Object object, String topic) {
        this.dispatcher.dispatch(this, registry, object, topic);
    }

    @Override
    public void close() {
        this.dispatcher.close();
    }

    @Override
    public String getBusName() {
        return this.busName;
    }
}
