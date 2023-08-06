package com.learn.guava.eventbus.monitor;

import com.google.common.eventbus.EventBus;

public class MonitorClient {
    public static void main(String[] args) throws Exception {
        final EventBus eventBus = new EventBus();
        eventBus.register(new FileChangeListener());
        DirectoryTargetMonitor directoryTargetMonitor = new DirectoryTargetMonitor(eventBus, "E:\\IdeaProjectsBook\\guavaProgramming\\monitor");
        directoryTargetMonitor.startMonitor();

    }
}
