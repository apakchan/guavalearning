package com.learn.guava.eventbus.monitor;

import lombok.Data;
import lombok.ToString;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

@Data
@ToString
public class FileChangeEvent {
    private final Path path;
    private final WatchEvent.Kind<?> kind;

    public FileChangeEvent(Path path, WatchEvent.Kind<?> kind) {
        this.path = path;
        this.kind = kind;
    }
}
