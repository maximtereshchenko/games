package com.github.maximtereshchenko.games.common.event;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class EventBus<T> {

    private final Set<Subscriber<T>> subscribers = new CopyOnWriteArraySet<>();

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(T event) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }
}
