package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
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
    void givenFoodConsumed_thenFoodConsumedIncremented() {
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(2),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 1))
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 0,
                    SessionMetric.FOOD_CONSUMED, 3,
                    SessionMetric.WARPS, 0
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
                    SessionMetric.FOOD_CONSUMED, 0,
                    SessionMetric.WARPS, 0
                )
            );
    }
}
