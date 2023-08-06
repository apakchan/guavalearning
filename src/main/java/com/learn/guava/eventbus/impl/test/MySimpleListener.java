package com.learn.guava.eventbus.impl.test;

import com.learn.guava.eventbus.impl.internal.MySubscribe;

public class MySimpleListener {

    @MySubscribe
    public void test1(String x) {
        System.out.println("simpleListener: param - " + x);
    }
}
