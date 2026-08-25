package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentRemainingTurnsDecrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> segmentEntities =
        registry.view(new Query().all(Segment.class));
    private final SegmentRemainingTurnsDecrementSystem segmentRemainingTurnsDecrementSystem =
        new SegmentRemainingTurnsDecrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(segmentRemainingTurnsDecrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(registry.createEntity(), new Segment(1));
        registry.update(0);
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenTurnStartedEvent_thenSegmentRemoved() {
        registry.addComponents(registry.createEntity(), new Segment(1));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(segmentEntities).isEmpty();
    }

    @Test
    void givenPositiveRemainingTurns_thenRemainingTurnsDecremented() {
        registry.addComponents(registry.createEntity(), new Segment(2));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }
}
