package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpsIncrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> statisticsEntities =
        world.entities(new Query().all(Statistics.class));
    private final WarpsIncrementSystem warpsIncrementSystem =
        new WarpsIncrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(warpsIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Warped.INSTANCE,
            new Statistics(Map.of())
        );
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

    @Test
    void givenWarped_thenWarpsIncremented() {
        world.addComponents(
            world.createEntity(),
            Warped.INSTANCE,
            new Statistics(Map.of(SessionMetric.WARPS, 1))
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
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
