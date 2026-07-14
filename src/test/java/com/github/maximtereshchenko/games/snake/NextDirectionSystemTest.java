package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class NextDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final BiPredicate<Direction, Direction> predicate = mock();
    private final NextDirectionSystem nextDirectionSystem = new NextDirectionSystem(
        dominion,
        predicate
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
    void givenPredicateFalse_thenNextDirectionRevertedToCurrent() {
        when(predicate.test(Direction.RIGHT, Direction.UP)).thenReturn(false);
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
            .containsExactly(Direction.RIGHT, Direction.RIGHT);
    }

    @Test
    void givenPredicateTrue_thenNextDirectionUnchanged() {
        when(predicate.test(Direction.RIGHT, Direction.UP)).thenReturn(true);
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
