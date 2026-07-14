package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class NextDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final NextDirectionSystem nextDirectionSystem = new NextDirectionSystem(
        dominion,
        Mode.CLASSIC
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.UP)
        );
        nextDirectionSystem.run(0);
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
    void givenNonLegalDirection_thenNextDirectionRevertedToCurrent() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.LEFT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentDirection.class,
                NextDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.RIGHT, Direction.RIGHT);
    }

    @Test
    void givenLegalDirection_thenNextDirectionUnchanged() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.UP)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        nextDirectionSystem.run(0);
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
}
