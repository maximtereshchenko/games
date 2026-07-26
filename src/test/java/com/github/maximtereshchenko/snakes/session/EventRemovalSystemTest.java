package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class EventRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EventRemovalSystem eventRemovalSystem = new EventRemovalSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Event.INSTANCE);
        var before = dominion.findAllEntities().stream().toList();
        eventRemovalSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenEvent_thenEventRemoved() {
        dominion.createEntity(TurnStarted.INSTANCE, Event.INSTANCE);
        eventRemovalSystem.run(0);
        assertThat(dominion.findEntitiesWith(Event.class)).isEmpty();
    }
}