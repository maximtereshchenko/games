package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodConsumedIncrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> statisticsEntities =
        world.entities(new Query().all(Statistics.class));
    private final FoodConsumedIncrementSystem foodConsumedIncrementSystem =
        new FoodConsumedIncrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(foodConsumedIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            FoodConsumed.INSTANCE,
            new Statistics(Map.of())
        );
        world.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 0,
                    SessionMetric.FOOD_CONSUMED, 0
                )
            );
    }

    @Test
    void givenFoodConsumed_thenFoodConsumedIncremented() {
        world.addComponents(
            world.createEntity(),
            FoodConsumed.INSTANCE,
            new Statistics(Map.of())
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 0,
                    SessionMetric.FOOD_CONSUMED, 1
                )
            );
    }

    @Test
    void givenNoFoodConsumed_thenFoodConsumedNotIncremented() {
        world.addComponents(world.createEntity(), new Statistics(Map.of()));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 0,
                    SessionMetric.FOOD_CONSUMED, 0
                )
            );
    }
}
