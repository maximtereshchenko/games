package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class EventRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = new EntityFactory(dominion);
    private final EventRemovalSystem eventRemovalSystem = new EventRemovalSystem(dominion);

    @Test
    void givenEvent_thenEventRemoved() {
        entityFactory.createTurnStartedEvent();
        eventRemovalSystem.run();
        assertThat(dominion.findCompositionsWith(Event.class)).isEmpty();
    }
}