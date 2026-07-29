package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class LeftTurnsIncrementSystemTest {

    private final World world = new World();
    private final Iterable<Entity> statisticsEntities =
        world.entities(new Query().all(Statistics.class));
    private final LeftTurnsIncrementSystem leftTurnsIncrementSystem =
        new LeftTurnsIncrementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(leftTurnsIncrementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(), Direction.UP),
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

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenLeftTurn_thenLeftTurnsCounterIncremented(Direction direction) {
        world.addComponents(
            world.createEntity(),
            direction,
            new DirectionIntent(Set.of(), direction.left()),
            new Statistics(Map.of())
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(statisticsEntities)
            .singleElement()
            .extracting(entity -> entity.component(Statistics.class).value)
            .isEqualTo(
                Map.of(
                    SessionMetric.LEFT_TURNS, 1,
                    SessionMetric.FOOD_CONSUMED, 0
                )
            );
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenRightTurn_thenLeftTurnsCounterNotIncremented(Direction direction) {
        world.addComponents(
            world.createEntity(),
            direction,
            new DirectionIntent(Set.of(), direction.opposite().left()),
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
                    SessionMetric.FOOD_CONSUMED, 0
                )
            );
    }
}
