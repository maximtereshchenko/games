package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentTimersIncrementSystemTest {

    private final World world = new World();
    private final SegmentTimersIncrementSystem segmentTimersIncrementSystem =
        new SegmentTimersIncrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(segmentTimersIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new SegmentTimerDefinition(1, 1));
        world.addComponents(world.createEntity(), new Timer(1, 1), Segment.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SegmentTimerDefinition.class)))
            .singleElement()
            .extracting(entity -> entity.component(SegmentTimerDefinition.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentTimerDefinition(1, 1));
        assertThat(world.entities(new Query().all(Timer.class, Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class))
            .usingRecursiveComparison()
            .isEqualTo(new Timer(1, 1));
    }

    @Test
    void givenNoFoodEatenEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new SegmentTimerDefinition(1, 1));
        world.addComponents(world.createEntity(), new Timer(1, 1), Segment.INSTANCE);
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SegmentTimerDefinition.class)))
            .singleElement()
            .extracting(entity -> entity.component(SegmentTimerDefinition.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentTimerDefinition(1, 1));
        assertThat(world.entities(new Query().all(Timer.class, Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class))
            .usingRecursiveComparison()
            .isEqualTo(new Timer(1, 1));
    }

    @Test
    void givenFoodEatenEvent_thenTimerIncremented() {
        world.addComponents(world.createEntity(), new SegmentTimerDefinition(1, 1));
        world.addComponents(world.createEntity(), new Timer(1, 1), Segment.INSTANCE);
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.addComponents(world.createEntity(), FoodEaten.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SegmentTimerDefinition.class)))
            .singleElement()
            .extracting(entity -> entity.component(SegmentTimerDefinition.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentTimerDefinition(1, 2));
        assertThat(world.entities(new Query().all(Timer.class, Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Timer.class))
            .usingRecursiveComparison()
            .isEqualTo(new Timer(1, 2));
    }
}