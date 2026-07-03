package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class InputSystemTest {

    private final Dominion dominion = Dominion.create();
    private final InputSystem inputSystem = new InputSystem(dominion);

    private static Stream<Arguments> directionChangedArguments() {
        return Stream.of(
            arguments(Direction.RIGHT, Input.Keys.W, Direction.UP),
            arguments(Direction.UP, Input.Keys.A, Direction.LEFT),
            arguments(Direction.LEFT, Input.Keys.S, Direction.DOWN),
            arguments(Direction.DOWN, Input.Keys.D, Direction.RIGHT)
        );
    }

    private static Stream<Arguments> directionNotChangedArguments() {
        return Stream.of(
            arguments(Direction.RIGHT, Input.Keys.A),
            arguments(Direction.UP, Input.Keys.S),
            arguments(Direction.LEFT, Input.Keys.D),
            arguments(Direction.DOWN, Input.Keys.W)
        );
    }

    @BeforeEach
    void setUp() {
        Gdx.input = mock();
    }

    @ParameterizedTest
    @MethodSource("directionChangedArguments")
    void givenKeyPressed_thenDirectionChanged(
        Direction current,
        int keyPressed,
        Direction next
    ) {
        dominion.createEntity(new CurrentDirection(current), new NextDirection(current));
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        inputSystem.run();
        assertThat(dominion.findCompositionsWith(CurrentDirection.class, NextDirection.class))
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(current, next);
    }

    @Test
    void givenNextDirectionOpposite_thenDirectionChanged() {
        dominion.createEntity(
            new CurrentDirection(Direction.UP),
            new NextDirection(Direction.RIGHT)
        );
        when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
        inputSystem.run();
        assertThat(dominion.findCompositionsWith(CurrentDirection.class, NextDirection.class))
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.LEFT);
    }

    @ParameterizedTest
    @MethodSource("directionNotChangedArguments")
    void givenOppositeDirection_thenDirectionNotChanged(
        Direction direction,
        int keyPressed
    ) {
        dominion.createEntity(new CurrentDirection(direction), new NextDirection(direction));
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        inputSystem.run();
        assertThat(dominion.findCompositionsWith(CurrentDirection.class, NextDirection.class))
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(direction, direction);
    }
}