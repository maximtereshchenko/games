package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentRemainingTurnsIncrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> segmentDefinitionEntities =
        registry.view(new Query().all(SegmentPolicy.class));
    private final Iterable<Entity> segmentEntities =
        registry.view(new Query().all(Segment.class));
    private final SegmentRemainingTurnsIncrementSystem segmentRemainingTurnsIncrementSystem =
        new SegmentRemainingTurnsIncrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(segmentRemainingTurnsIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(registry.createEntity(), new SegmentPolicy(1, 1));
        registry.addComponents(registry.createEntity(), new Segment(1));
        registry.update(0);
        assertThat(segmentDefinitionEntities)
            .singleElement()
            .extracting(entity -> entity.component(SegmentPolicy.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentPolicy(1, 1));
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenNoFoodConsumed_thenNoChanges() {
        registry.addComponents(registry.createEntity(), new SegmentPolicy(1, 1));
        registry.addComponents(registry.createEntity(), new Segment(1));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(segmentDefinitionEntities)
            .singleElement()
            .extracting(entity -> entity.component(SegmentPolicy.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentPolicy(1, 1));
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenFoodConsumed_thenSegmentRemainingTurnsIncremented() {
        registry.addComponents(registry.createEntity(), new SegmentPolicy(2, 1));
        registry.addComponents(registry.createEntity(), new Segment(1));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.addComponents(registry.createEntity(), new FoodConsumed(2));
        registry.update(0);
        assertThat(segmentDefinitionEntities)
            .singleElement()
            .extracting(entity -> entity.component(SegmentPolicy.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentPolicy(2, 5));
        assertThat(segmentEntities)
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(5);
    }
}
