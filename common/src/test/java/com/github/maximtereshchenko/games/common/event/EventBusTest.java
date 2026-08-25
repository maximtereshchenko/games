package com.github.maximtereshchenko.games.common.event;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class EventBusTest {

    private final Subscriber<String> first = mock();
    private final Subscriber<String> second = mock();
    private final EventBus<String> eventBus = new EventBus<>();

    @Test
    void whenPublish_thenSubscribersNotified() {
        eventBus.subscribe(first);
        eventBus.publish("event");
        verify(first).onEvent("event");
    }

    @Test
    void whenMultipleSubscribers_thenAllNotified() {
        eventBus.subscribe(first);
        eventBus.subscribe(second);
        eventBus.publish("event");
        verify(first).onEvent("event");
        verify(second).onEvent("event");
    }

    @Test
    void whenSameSubscriberSubscribedTwice_thenNotifiedOnce() {
        eventBus.subscribe(first);
        eventBus.subscribe(first);
        eventBus.publish("event");
        verify(first).onEvent("event");
    }

    @Test
    void whenPublish_thenSubscribersNotifiedInSubscriptionOrder() {
        eventBus.subscribe(first);
        eventBus.subscribe(second);
        eventBus.publish("event");
        var order = inOrder(first, second);
        order.verify(first).onEvent("event");
        order.verify(second).onEvent("event");
    }

    @Test
    void givenNoSubscribers_whenPublish_thenNothingHappens() {
        eventBus.publish("event");
        verifyNoInteractions(first);
        verifyNoInteractions(second);
    }
}
