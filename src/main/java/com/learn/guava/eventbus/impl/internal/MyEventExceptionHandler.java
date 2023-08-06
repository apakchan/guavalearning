package com.learn.guava.eventbus.impl.internal;

public interface MyEventExceptionHandler {

    void handle(Throwable cause, MyEventContext context);
}
