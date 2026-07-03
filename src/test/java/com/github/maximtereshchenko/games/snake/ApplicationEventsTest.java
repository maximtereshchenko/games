package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class ApplicationEventsTest {

    private final ApplicationEvents applicationEvents = new ApplicationEvents();

    @Test
    void whenPublish_thenSubscribersNotified() {
        var subscriber = mock(Subscriber.class);
        applicationEvents.subscribe(subscriber);
        applicationEvents.publish(ApplicationEvent.SNAKE_SESSION_ENDED);
        verify(subscriber).onEvent(ApplicationEvent.SNAKE_SESSION_ENDED);
    }
}