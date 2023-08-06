package com.learn.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.learn.guava.eventbus.listener.ExceptionListener;

public class ExceptionEventBusExample {
    public static void main(String[] args) {
        final EventBus eventBus = new EventBus(new MyExceptionHandler());
        eventBus.register(new ExceptionListener());
        eventBus.post("event");
    }

    static class MyExceptionHandler implements SubscriberExceptionHandler {
        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            System.out.println(context.getEvent());
            System.out.println(context.getEventBus());
            System.out.println(context.getSubscriber());
            System.out.println(context.getSubscriberMethod());
        }
    }
}
