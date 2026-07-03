package com.github.maximtereshchenko.games.snake;

import java.util.HashSet;
import java.util.Set;

final class ApplicationEvents {

    private final Set<Subscriber> subscribers = new HashSet<>();

    void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    void publish(ApplicationEvent event) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }
}
