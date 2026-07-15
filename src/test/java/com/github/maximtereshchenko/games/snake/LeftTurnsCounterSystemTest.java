package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

final class LeftTurnsCounterSystemTest {

    private final Dominion dominion = Dominion.create();
    private final LeftTurnsCounterSystem leftTurnsSystem = new LeftTurnsCounterSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.UP)
        );
        dominion.createEntity(new LeftTurnsCounter(1));
        var before = dominion.findAllEntities().stream().toList();
        leftTurnsSystem.run(0);
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenLeftTurn_thenLeftTurnsCounterIncremented(Direction direction) {
        dominion.createEntity(
            new CurrentDirection(direction),
            new NextDirection(direction.left())
        );
        dominion.createEntity(new LeftTurnsCounter(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        leftTurnsSystem.run(0);
        assertThat(dominion.findCompositionsWith(LeftTurnsCounter.class))
            .singleElement()
            .extracting(result -> result.value)
            .isEqualTo(2);
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenRightTurn_thenLeftTurnsCounterNotIncremented(Direction direction) {
        dominion.createEntity(
            new CurrentDirection(direction),
            new NextDirection(direction.opposite().left())
        );
        dominion.createEntity(new LeftTurnsCounter(1));
        dominion.createEntity(TurnStarted.INSTANCE);
        leftTurnsSystem.run(0);
        assertThat(dominion.findCompositionsWith(LeftTurnsCounter.class))
            .singleElement()
            .extracting(result -> result.value)
            .isEqualTo(1);
    }
}