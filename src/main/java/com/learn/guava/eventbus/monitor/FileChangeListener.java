package com.learn.guava.eventbus.monitor;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChangeListener.class);

    @Subscribe
    public void onChange(FileChangeEvent fileChangeEvent) {
        LOGGER.info("{}-{}", fileChangeEvent.getKind(), fileChangeEvent.getPath());
    }

}
