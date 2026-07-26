package com.github.maximtereshchenko.snakes.event;

import java.util.LinkedHashSet;
import java.util.Set;

public final class ApplicationEvents {

    private final Set<Subscriber> subscribers = new LinkedHashSet<>();

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(ApplicationEvent event) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }
}
