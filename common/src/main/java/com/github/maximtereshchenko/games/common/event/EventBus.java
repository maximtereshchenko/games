package com.github.maximtereshchenko.games.common.event;

import java.util.LinkedHashSet;
import java.util.Set;

public final class EventBus<T> {

    private final Set<Subscriber<T>> subscribers = new LinkedHashSet<>();

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(T event) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }
}
