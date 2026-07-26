package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;

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
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
        );
        world.addComponents(world.createEntity(), new SessionStatisticsAccumulator());
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(CurrentForwardDirection.class, NextForwardDirection.class)
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(Direction.RIGHT, Direction.UP);
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
            new CurrentForwardDirection(direction),
            new NextForwardDirection(direction.left())
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
            new CurrentForwardDirection(direction),
            new NextForwardDirection(direction.opposite().left())
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