package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentRemainingTurnsDecrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> segmentEntities =
        world.entities(new Query().all(Segment.class));
    private final SegmentRemainingTurnsDecrementSystem segmentRemainingTurnsDecrementSystem =
        new SegmentRemainingTurnsDecrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(segmentRemainingTurnsDecrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new Segment(1));
        world.update(0);
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenSegmentRemoved() {
        world.addComponents(world.createEntity(), new Segment(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(segmentEntities).isEmpty();
    }

    @Test
    void givenPositiveRemainingTurns_thenRemainingTurnsDecremented() {
        world.addComponents(world.createEntity(), new Segment(2));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }
}
