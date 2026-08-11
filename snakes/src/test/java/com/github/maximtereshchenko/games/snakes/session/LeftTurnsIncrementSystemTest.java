package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class LeftTurnsIncrementSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> statisticsEntities =
        registry.entities(new Query().all(Statistics.class));
    private final LeftTurnsIncrementSystem leftTurnsIncrementSystem =
        new LeftTurnsIncrementSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(leftTurnsIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(), Direction.UP),
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

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenLeftTurn_thenLeftTurnsCounterIncremented(Direction direction) {
        registry.addComponents(
            registry.createEntity(),
            direction,
            new DirectionIntent(Set.of(), direction.left()),
            new Statistics(Map.of())
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 1,
                    SessionMetric.FOOD_CONSUMED, 0,
                    SessionMetric.WARPS, 0
                )
            );
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenRightTurn_thenLeftTurnsCounterNotIncremented(Direction direction) {
        registry.addComponents(
            registry.createEntity(),
            direction,
            new DirectionIntent(Set.of(), direction.opposite().left()),
            new Statistics(Map.of())
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
                    SessionMetric.WARPS, 0
                )
            );
    }
}
