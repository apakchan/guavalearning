package com.learn.guava.eventbus.impl.internal;

import java.lang.reflect.Method;

public class MySubscriber {
    private final Object subscribeObject;

    private final Method subscribeMethod;

    private boolean disable = false;

    public MySubscriber(Object subscribeObject, Method subscribeMethod) {
        this.subscribeObject = subscribeObject;
        this.subscribeMethod = subscribeMethod;
    }

    public Object getSubscribeObject() {
        return subscribeObject;
    }

    public Method getSubscribeMethod() {
        return subscribeMethod;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public boolean isDisable() {
        return disable;
    }
}
