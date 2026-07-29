package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentRemainingTurnsIncrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> segmentDefinitionEntities =
        world.entities(new Query().all(SegmentPolicy.class));
    private final Iterable<Entity> segmentEntities =
        world.entities(new Query().all(Segment.class));
    private final SegmentRemainingTurnsIncrementSystem segmentRemainingTurnsIncrementSystem =
        new SegmentRemainingTurnsIncrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(segmentRemainingTurnsIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new SegmentPolicy(1, 1));
        world.addComponents(world.createEntity(), new Segment(1));
        world.update(0);
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
        world.addComponents(world.createEntity(), new SegmentPolicy(1, 1));
        world.addComponents(world.createEntity(), new Segment(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
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
        world.addComponents(world.createEntity(), new SegmentPolicy(2, 1));
        world.addComponents(world.createEntity(), new Segment(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.addComponents(world.createEntity(), new FoodConsumed(2));
        world.update(0);
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
