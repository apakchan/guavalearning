package com.learn.guava.eventbus.impl.internal;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class MyDispatcher {
    private final Executor executorService;

    private final MyEventExceptionHandler eventExceptionHandler;

    public static final SeqExecutorService SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;

    public static final PreThreadExecutorService PRE_THREAD_EXECUTOR_SERVICE = PreThreadExecutorService.INSTANCE;

    public MyDispatcher(Executor executorService, MyEventExceptionHandler eventExceptionHandler) {
        this.executorService = executorService;
        this.eventExceptionHandler = eventExceptionHandler;
    }

    public void dispatch(Bus bus, MyRegistry registry, Object event, String topic) {
        ConcurrentLinkedDeque<MySubscriber> subscribers = registry.getTopicSubscriber(topic);
        if (subscribers == null) {
            if (eventExceptionHandler != null) {
                // TODO impl context
                eventExceptionHandler.handle(new IllegalArgumentException("The topic " + topic + " not bind yet"), new BaseContext(bus.getBusName(), null, event));
            }
            return;
        }
        subscribers.stream()
                .filter(mySubscriber -> !mySubscriber.isDisable())
                .filter(mySubscriber -> {
                    Method method = mySubscriber.getSubscribeMethod();
                    Class<?> parameterType = method.getParameterTypes()[0];
                    return parameterType.isAssignableFrom(event.getClass());
                }).forEach(mySubscriber -> doInvokeSubscribe(mySubscriber, event, bus));
    }

    private void doInvokeSubscribe(MySubscriber subscriber, Object event, Bus bus) {
        Method targetMethod = subscriber.getSubscribeMethod();
        Object targetObj = subscriber.getSubscribeObject();
        executorService.execute(() -> {
            try {
                targetMethod.invoke(targetObj, event);
            } catch (Exception e) {
                if (eventExceptionHandler != null) {
                    eventExceptionHandler.handle(e, new BaseContext(bus.getBusName(), subscriber, event));
                }
            }
        });
    }

    public void close() {
        if (executorService instanceof ExecutorService) {
            ((ExecutorService) executorService).shutdown();
        }
    }

    static MyDispatcher newDispatcher(Executor executor, MyEventExceptionHandler eventExceptionHandler) {
        return new MyDispatcher(executor, eventExceptionHandler);
    }

    static MyDispatcher seqDispatcher(MyEventExceptionHandler eventExceptionHandler) {
        return newDispatcher(SEQ_EXECUTOR_SERVICE, eventExceptionHandler);
    }

    static MyDispatcher preThreadDispatcher(MyEventExceptionHandler eventExceptionHandler) {
        return newDispatcher(PRE_THREAD_EXECUTOR_SERVICE, eventExceptionHandler);
    }

    private static class SeqExecutorService implements Executor {

        private final static SeqExecutorService INSTANCE = new SeqExecutorService();

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    private static class PreThreadExecutorService implements Executor {

        private final static PreThreadExecutorService INSTANCE = new PreThreadExecutorService();

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

    private static class BaseContext implements MyEventContext {
        private final String eventBusName;

        private final MySubscriber subscriber;

        private final Object event;

        public BaseContext(String eventBusName, MySubscriber subscriber, Object event) {
            this.eventBusName = eventBusName;
            this.subscriber = subscriber;
            this.event = event;
        }

        @Override
        public String getSource() {
            return this.eventBusName;
        }

        @Override
        public Object getSubscriber() {
            return subscriber != null ? subscriber.getSubscribeObject() : null;
        }

        @Override
        public Method getSubscriberMethod() {
            return subscriber != null ? subscriber.getSubscribeMethod() : null;
        }

        @Override
        public Object getEvent() {
            return this.event;
        }
    }
}
