package com.github.maximtereshchenko.games.common.event;

public interface Subscriber<T> {

    void onEvent(T event);
}
