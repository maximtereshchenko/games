package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentSystemTest {

    private final World world = new World();
    private final SegmentSystem segmentSystem = new SegmentSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(segmentSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new SegmentDefinition(1, 1));
        world.addComponents(world.createEntity(), new Segment(1));
        world.update(0);
        assertThat(world.entities(new Query().all(SegmentDefinition.class)))
            .singleElement()
            .extracting(entity -> entity.component(SegmentDefinition.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentDefinition(1, 1));
        assertThat(world.entities(new Query().all(Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenNoFoodEatenEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new SegmentDefinition(1, 1));
        world.addComponents(world.createEntity(), new Segment(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SegmentDefinition.class)))
            .singleElement()
            .extracting(entity -> entity.component(SegmentDefinition.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentDefinition(1, 1));
        assertThat(world.entities(new Query().all(Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(1);
    }

    @Test
    void givenFoodEatenEvent_thenSegmentRemainingTurnsIncremented() {
        world.addComponents(world.createEntity(), new SegmentDefinition(1, 1));
        world.addComponents(world.createEntity(), new Segment(1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.addComponents(world.createEntity(), FoodEaten.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SegmentDefinition.class)))
            .singleElement()
            .extracting(entity -> entity.component(SegmentDefinition.class))
            .usingRecursiveComparison()
            .isEqualTo(new SegmentDefinition(1, 2));
        assertThat(world.entities(new Query().all(Segment.class)))
            .singleElement()
            .extracting(entity -> entity.component(Segment.class).remainingTurns)
            .isEqualTo(2);
    }
}