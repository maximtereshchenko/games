package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class FoodConsumedIncrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> statisticsEntities =
        registry.entities(new Query().all(Statistics.class));
    private final FoodConsumedIncrementSystem foodConsumedIncrementSystem =
        new FoodConsumedIncrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(foodConsumedIncrementSystem);
    }

    @Test
    void givenFoodConsumed_thenFoodConsumedIncremented() {
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(2),
            new Statistics(Map.of(SessionMetric.FOOD_CONSUMED, 1))
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
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
        registry.addComponents(registry.createEntity(), new Statistics(Map.of()));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
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
