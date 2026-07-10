package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class ApplicationEventsTest {

    private final ApplicationEvents applicationEvents = new ApplicationEvents();

    @Test
    void whenPublish_thenSubscribersNotified() {
        var subscriber = mock(Subscriber.class);
        var applicationEvent = new SnakeSessionEnded();
        applicationEvents.subscribe(subscriber);
        applicationEvents.publish(applicationEvent);
        verify(subscriber).onEvent(applicationEvent);
    }
}