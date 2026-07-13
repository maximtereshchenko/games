package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class CurrentDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final BiPredicate<Direction, Direction> predicate = mock();
    private final CurrentDirectionSystem currentDirectionSystem = new CurrentDirectionSystem(
        dominion,
        predicate
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
    void givenPredicateFalse_thenNoChanges() {
        when(predicate.test(Direction.RIGHT, Direction.UP)).thenReturn(false);
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
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenPredicateTrue_thenCurrentDirectionSetToNext() {
        when(predicate.test(Direction.RIGHT, Direction.UP)).thenReturn(true);
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