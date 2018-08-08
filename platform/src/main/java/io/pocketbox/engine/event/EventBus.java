package io.pocketbox.engine.event;

import com.esotericsoftware.minlog.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EventBus {

    private static EventBus bus;
    private LinkedList<Object> eventQueue;
    private LinkedList<Object> localEventQueue;
    private Map<Class<?>, ArrayList<SubscriberMethod>> subscriberMethodMap;
    private Map<Object, Class<?>> typesBySubscriber;

    private static final String MESSAGE_METHOD_NAME = "onMessage";
    private static final String DEFAULT_ON_MESSAGE_METHOD_TYPE = "java.lang.Object";

    public EventBus() {
        eventQueue = new LinkedList<>();
        localEventQueue = new LinkedList<>();
        subscriberMethodMap = new HashMap<>();
        typesBySubscriber = new HashMap<>();
    }

    public void register(Object object) {
        Class<?> subscriberClass = object.getClass();
        Method[] methods = subscriberClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(MESSAGE_METHOD_NAME) &&
                    !method.getParameterTypes()[0].getName().equals(DEFAULT_ON_MESSAGE_METHOD_TYPE)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<?> eventType = parameterTypes[0];
                ArrayList<SubscriberMethod> methodArrayList = subscriberMethodMap.get(eventType);
                if (methodArrayList == null) {
                    methodArrayList = new ArrayList<>();
                }
                SubscriberMethod subscriberMethod = new SubscriberMethod(object, method);
                methodArrayList.add(subscriberMethod);
                subscriberMethodMap.put(eventType, methodArrayList);
                typesBySubscriber.put(object, eventType);
                Log.debug("Subscribe object=" + object + " for event=" + eventType);
                break;
            }
        }
    }

    public void post(Object event) {
        eventQueue.push(event);
    }

    public void process() {
        while (eventQueue.size() != 0) {
            Object event = eventQueue.pop();
            ArrayList<SubscriberMethod> methodArrayList = subscriberMethodMap.get(event.getClass());
            if (methodArrayList != null) {
                for (SubscriberMethod subscriberMethod : methodArrayList) {
                    try {
                        subscriberMethod.method.invoke(subscriberMethod.subscriber, event);
                    } catch (IllegalAccessException e) {
                        Log.debug("Can't deliver the event subscriberMethod=" + subscriberMethod);
                    } catch (InvocationTargetException e) {
                        Log.debug("Can't deliver the event subscriberMethod=" + subscriberMethod);
                    }
                }
            } else {
                localEventQueue.push(event);
            }
        }
        eventQueue.addAll(localEventQueue);
        localEventQueue.clear();
    }

    public static EventBus instance() {
        if (bus == null) {
            synchronized (EventBus.class) {
                if (bus == null) {
                    bus = new EventBus();
                }
            }
        }
        return bus;
    }

    public void unregister(Object subscriber) {
        Class<?> subscribedType = typesBySubscriber.get(subscriber);
        if (subscribedType != null) {
            unsubscribeByEventType(subscriber, subscribedType);
            typesBySubscriber.remove(subscriber);
            Log.debug("Object " + subscriber + " has successful unregistered");
        } else {
            Log.debug("Object " + subscriber + " already unregistered");
        }
    }

    private void unsubscribeByEventType(Object subscriber, Class<?> eventType) {
        ArrayList<SubscriberMethod> subscriptions = subscriberMethodMap.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                SubscriberMethod subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }
}
