package com.learn.guava.eventbus.impl.internal;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * <pre>
 *     topic1 -> subscriber.subscribe
 *               subscriber.subscribe
 *               subscriber.subscribe
 *     topic2 -> subscriber.subscribe
 *               subscriber.subscribe
 *               subscriber.subscribe
 * </pre>
 */
class MyRegistry {

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<MySubscriber>> subscriberContainer
            = new ConcurrentHashMap<>();

    public void bind(Object subscriber) {
        List<Method> subscribeMethods = getSubscribeMethods(subscriber);
        subscribeMethods.forEach(m -> tierSubscriber(subscriber, m));
    }

    // get subscriber's legal method
    private List<Method> getSubscribeMethods(Object subscriber) {
        final List<Method> methods = new ArrayList<>();
        Class<?> subscriberClass = subscriber.getClass();
        while (subscriberClass != null) {
            Method[] declaredMethods = subscriberClass.getMethods();
            Arrays.stream(declaredMethods)
                    .filter(this::isLegalMethod)
                    .forEach(methods::add);
            subscriberClass = subscriberClass.getSuperclass();
        }
        return methods;
    }

    private boolean isLegalMethod(Method m) {
        return m.isAnnotationPresent(MySubscribe.class) && m.getParameterCount() == 1 && m.getModifiers() == Modifier.PUBLIC;
    }

    // put method into concurrent container
    private void tierSubscriber(Object subscriber, Method method) {
        MySubscribe mySubscribe = method.getDeclaredAnnotation(MySubscribe.class);
        String topic = mySubscribe.topic();
        subscriberContainer.computeIfAbsent(topic, key -> new ConcurrentLinkedDeque<>());
        subscriberContainer.get(topic).add(new MySubscriber(subscriber,method));
    }

    public void unbind(Object subscriber) {
        subscriberContainer.forEach((topic, queue) -> {
            queue.forEach(subscriberObj -> {
                if (subscriberObj.getSubscribeObject() == subscriber) {
                    subscriberObj.setDisable(true);
                }
            });
        });
    }

    public ConcurrentLinkedDeque<MySubscriber> getTopicSubscriber(String topic) {
        return subscriberContainer.get(topic);
    }

}
