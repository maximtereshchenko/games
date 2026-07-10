package com.github.maximtereshchenko.games.snake;

import java.util.LinkedHashSet;
import java.util.Set;

final class ApplicationEvents {

    private final Set<Subscriber> subscribers = new LinkedHashSet<>();

    void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    void publish(ApplicationEvent event) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }
}
