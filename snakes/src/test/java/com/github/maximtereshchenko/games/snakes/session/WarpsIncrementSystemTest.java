package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpsIncrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> statisticsEntities =
        registry.view(new Query().all(Statistics.class));
    private final WarpsIncrementSystem warpsIncrementSystem =
        new WarpsIncrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(warpsIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Warped.INSTANCE,
            new Statistics(Map.of())
        );
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

    @Test
    void givenWarped_thenWarpsIncremented() {
        registry.addComponents(
            registry.createEntity(),
            Warped.INSTANCE,
            new Statistics(Map.of(SessionMetric.WARPS, 1))
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 0,
                    SessionMetric.FOOD_CONSUMED, 0,
                    SessionMetric.WARPS, 2
                )
            );
    }

    @Test
    void givenNoWarped_thenWarpsNotIncremented() {
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
