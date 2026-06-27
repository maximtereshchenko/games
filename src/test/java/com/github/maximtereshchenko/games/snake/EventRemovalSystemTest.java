package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class EventRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EventRemovalSystem eventRemovalSystem = new EventRemovalSystem(dominion);

    @Test
    void givenEvent_thenEventRemoved() {
        dominion.createEntity(TurnStarted.INSTANCE);
        eventRemovalSystem.run();
        assertThat(dominion.findEntitiesWith(Event.class)).isEmpty();
    }
}