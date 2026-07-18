package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class CurrentForwardDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final CurrentForwardDirectionSystem currentDirectionSystem =
        new CurrentForwardDirectionSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
        );
        currentDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenTurnStartedEvent_thenCurrentDirectionSetToNext() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        currentDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.UP);
    }
}