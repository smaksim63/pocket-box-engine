package io.pocketbox.engine.event;

import java.lang.reflect.Method;

class SubscriberMethod {
    Object subscriber;
    Method method;

    SubscriberMethod(Object subscriber, Method method) {
        this.subscriber = subscriber;
        this.method = method;
    }
}