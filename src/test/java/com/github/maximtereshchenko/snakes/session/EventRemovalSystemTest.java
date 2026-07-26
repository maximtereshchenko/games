package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class EventRemovalSystemTest {

    private final World world = new World();
    private final EventRemovalSystem eventRemovalSystem = new EventRemovalSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(eventRemovalSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), Event.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Event.class))).hasSize(1);
    }

    @Test
    void givenEvent_thenEventRemoved() {
        world.addComponents(
            world.createEntity(),
            TurnStarted.INSTANCE,
            Event.INSTANCE
        );
        world.update(0);
        assertThat(world.entities(new Query().all(Event.class))).isEmpty();
    }
}