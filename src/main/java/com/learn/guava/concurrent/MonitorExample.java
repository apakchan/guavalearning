package com.learn.guava.concurrent;

import com.google.common.util.concurrent.Monitor;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;

public class MonitorExample {
    static class Synchronized {
        private final LinkedList<Integer> queue = new LinkedList<>();
        private final int MAX = 10;

        public void offer(int value) {
            synchronized (queue) {
                while (queue.size() >= MAX) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(value);
                queue.notifyAll();
            }
        }

        public int take() {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer res = queue.pollFirst();
                queue.notifyAll();
                return res;
            }
        }
    }

    static class LockCondition {
        private final LinkedList<Integer> queue = new LinkedList<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition FULL_CONDITION = lock.newCondition();
        private final Condition EMPTY_CONDITION = lock.newCondition();
        private final int MAX = 10;

        public void offer(int value) {
            try {
                lock.lock();
                while (queue.size() >= MAX) {
                    FULL_CONDITION.await();
                }
                queue.addLast(value);
                EMPTY_CONDITION.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public Integer take() {
            Integer res = null;
            try {
                lock.lock();
                while (queue.isEmpty()) {
                    EMPTY_CONDITION.await();
                }
                res = queue.pollFirst();
                FULL_CONDITION.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return res;
        }
    }

    static class MonitorGuard {
        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        private final Monitor monitor = new Monitor();

        private final Monitor.Guard CAN_OFFER = monitor.newGuard(() -> queue.size() < MAX);

        private final Monitor.Guard CAN_TAKE = monitor.newGuard(() -> !queue.isEmpty());

        public void offer(int value) {
            try {
                monitor.enterWhen(CAN_OFFER);
                queue.addLast(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                monitor.leave();
            }
        }

        public Integer take() {
            Integer res = null;
            try {
                monitor.enterWhen(CAN_TAKE);
                res = queue.pollFirst();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                monitor.leave();
            }
            return res;
        }
    }

    public static void main(String[] args) {
        final MonitorGuard synchronizer = new MonitorGuard();
        final AtomicInteger COUNTER = new AtomicInteger(0);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    int data = COUNTER.getAndIncrement();
                    System.out.println(currentThread().getName() + " offer: " + data);
                    synchronizer.offer(data);
                    try {
                        TimeUnit.MILLISECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "produceThread-" + i).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    int data = synchronizer.take();
                    System.out.println(currentThread().getName() + " take: " + data);
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "consumeThread-" + i).start();
        }
    }
}
