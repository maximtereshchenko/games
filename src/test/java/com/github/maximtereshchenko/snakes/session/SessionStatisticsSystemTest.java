package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class SessionStatisticsSystemTest {

    private final World world = new World();
    private final SessionStatisticsSystem sessionStatisticsSystem = new SessionStatisticsSystem(
        world
    );

    @BeforeEach
    void setUp() {
        world.addSystems(sessionStatisticsSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new ForwardMovement(1, 1, Direction.RIGHT),
            new PlannedMovement(Set.of(), Direction.UP)
        );
        world.addComponents(world.createEntity(), new SessionStatisticsAccumulator());
        world.update(0);
        assertThat(world.entities(new Query().all(SessionStatisticsAccumulator.class)))
            .singleElement()
            .extracting(
                entity -> entity.component(SessionStatisticsAccumulator.class).value
            )
            .isEqualTo(Map.of(SessionStatistics.LEFT_TURNS, 0));
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenLeftTurn_thenLeftTurnsCounterIncremented(Direction direction) {
        world.addComponents(
            world.createEntity(),
            new ForwardMovement(1, 1, direction),
            new PlannedMovement(Set.of(), direction.left())
        );
        world.addComponents(world.createEntity(), new SessionStatisticsAccumulator());
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SessionStatisticsAccumulator.class)))
            .singleElement()
            .extracting(
                entity -> entity.component(SessionStatisticsAccumulator.class).value
            )
            .isEqualTo(Map.of(SessionStatistics.LEFT_TURNS, 1));
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenRightTurn_thenLeftTurnsCounterNotIncremented(Direction direction) {
        world.addComponents(
            world.createEntity(),
            new ForwardMovement(1, 1, direction),
            new PlannedMovement(Set.of(), direction.opposite().left())
        );
        world.addComponents(world.createEntity(), new SessionStatisticsAccumulator());
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(SessionStatisticsAccumulator.class)))
            .singleElement()
            .extracting(
                entity -> entity.component(SessionStatisticsAccumulator.class).value
            )
            .isEqualTo(Map.of(SessionStatistics.LEFT_TURNS, 0));
    }
}