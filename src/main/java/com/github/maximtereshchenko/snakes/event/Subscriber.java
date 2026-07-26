package com.github.maximtereshchenko.snakes.event;

public interface Subscriber {

    void onEvent(ApplicationEvent applicationEvent);
}
