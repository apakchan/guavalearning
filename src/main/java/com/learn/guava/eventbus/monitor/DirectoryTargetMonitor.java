package com.learn.guava.eventbus.monitor;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class DirectoryTargetMonitor implements TargetMonitor{
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryTargetMonitor.class);

    private final EventBus eventBus;

    private final Path targetPath;

    private WatchService watchService;

    private volatile boolean start = false;

    public DirectoryTargetMonitor(final EventBus eventBus, final String targetPath) {
        this(eventBus, targetPath, "");
    }

    public DirectoryTargetMonitor(final EventBus eventBus, final String targetPath, final String... morePath) {
        this.eventBus = eventBus;
        this.targetPath = Paths.get(targetPath, morePath);
    }

    @Override
    public void startMonitor() throws Exception {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.targetPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_CREATE);
        LOGGER.info("directory [{}] has been monitored", targetPath);
        this.start = true;
        while (start) {
            WatchKey watchKey = null;
            try {
                watchKey = watchService.take();
                watchKey.pollEvents().forEach(event -> {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path path = (Path) event.context();
                    Path child = DirectoryTargetMonitor.this.targetPath.resolve(path);
                    this.eventBus.post(new FileChangeEvent(child, kind));
                });
            } catch (Exception e) {
                this.start = false;
            } finally {
                if (watchKey != null) {
                    watchKey.reset();
                }
            }
        }
    }

    @Override
    public void stopMonitor() throws Exception {
        LOGGER.info("directory [{}] monitor will be stopped", targetPath);
        Thread.currentThread().interrupt();
        this.start = false;
        this.watchService.close();
        LOGGER.info("done");
    }
}
