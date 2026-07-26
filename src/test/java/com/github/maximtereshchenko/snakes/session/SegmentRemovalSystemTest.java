package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentRemovalSystemTest {

    private final World world = new World();
    private final SegmentRemovalSystem timerRemovalSystem =
        new SegmentRemovalSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(timerRemovalSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new Timer(0, 0), Segment.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Timer.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class).turnsRemaining)
            .isEqualTo(0);
    }

    @Test
    void givenTurnStartedEvent_thenTimerRemoved() {
        world.addComponents(world.createEntity(), new Timer(0, 0), Segment.INSTANCE);
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Timer.class))).isEmpty();
    }

    @Test
    void givenTimerPositive_thenNoChanges() {
        world.addComponents(world.createEntity(), new Timer(1, 1), Segment.INSTANCE);
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Timer.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class).turnsRemaining)
            .isEqualTo(1);
    }
}