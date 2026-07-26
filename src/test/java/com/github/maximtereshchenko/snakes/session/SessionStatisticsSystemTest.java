package com.github.maximtereshchenko.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class SessionStatisticsSystemTest {

    private final Dominion dominion = Dominion.create();
    private final SessionStatisticsSystem sessionStatisticsSystem = new SessionStatisticsSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
        );
        dominion.createEntity(new SessionStatisticsAccumulator());
        var before = dominion.findAllEntities().stream().toList();
        sessionStatisticsSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenLeftTurn_thenLeftTurnsCounterIncremented(Direction direction) {
        dominion.createEntity(
            new CurrentForwardDirection(direction),
            new NextForwardDirection(direction.left())
        );
        dominion.createEntity(new SessionStatisticsAccumulator());
        dominion.createEntity(TurnStarted.INSTANCE);
        sessionStatisticsSystem.run(0);
        assertThat(dominion.findCompositionsWith(SessionStatisticsAccumulator.class))
            .singleElement()
            .extracting(result -> result.value)
            .isEqualTo(Map.of(SessionStatistics.LEFT_TURNS, 1));
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenRightTurn_thenLeftTurnsCounterNotIncremented(Direction direction) {
        dominion.createEntity(
            new CurrentForwardDirection(direction),
            new NextForwardDirection(direction.opposite().left())
        );
        dominion.createEntity(new SessionStatisticsAccumulator());
        dominion.createEntity(TurnStarted.INSTANCE);
        sessionStatisticsSystem.run(0);
        assertThat(dominion.findCompositionsWith(SessionStatisticsAccumulator.class))
            .singleElement()
            .extracting(result -> result.value)
            .isEqualTo(Map.of(SessionStatistics.LEFT_TURNS, 0));
    }
}