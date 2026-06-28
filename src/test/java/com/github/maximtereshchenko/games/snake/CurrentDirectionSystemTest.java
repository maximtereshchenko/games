package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class CurrentDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final CurrentDirectionSystem currentDirectionSystem = new CurrentDirectionSystem(
        dominion
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.UP)
        );
        currentDirectionSystem.run();
        assertThat(
            dominion.findEntitiesWith(
                CurrentDirection.class,
                NextDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenTurnStartedEvent_thenCurrentDirectionSetToNext() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.UP)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        currentDirectionSystem.run();
        assertThat(
            dominion.findEntitiesWith(
                CurrentDirection.class,
                NextDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.UP);
    }
}