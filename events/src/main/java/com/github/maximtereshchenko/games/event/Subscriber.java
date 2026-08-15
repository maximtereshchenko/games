package com.github.maximtereshchenko.games.event;

public interface Subscriber<T> {

    void onEvent(T event);
}
