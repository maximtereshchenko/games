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
        world.addComponents(world.createEntity(), new Segment(1));
        world.update(0);
        assertThat(world.entities(new Query().all(Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenSegmentRemoved() {
        world.addComponents(world.createEntity(), new Segment(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Segment.class))).isEmpty();
    }

    @Test
    void givenPositiveRemainingTurns_thenNoChanges() {
        world.addComponents(world.createEntity(), new Segment(2));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }
}