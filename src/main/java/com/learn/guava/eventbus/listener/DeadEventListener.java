package com.learn.guava.eventbus.listener;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

public class DeadEventListener {
    @Subscribe
    public void handle(DeadEvent deadEvent) {
        System.out.println(deadEvent.getSource());
        System.out.println(deadEvent.getEvent());
    }
}
