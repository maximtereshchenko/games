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
            arguments(Head.Direction.RIGHT, Input.Keys.W, Head.Direction.UP),
            arguments(Head.Direction.UP, Input.Keys.A, Head.Direction.LEFT),
            arguments(Head.Direction.LEFT, Input.Keys.S, Head.Direction.DOWN),
            arguments(Head.Direction.DOWN, Input.Keys.D, Head.Direction.RIGHT)
        );
    }

    private static Stream<Arguments> directionNotChangedArguments() {
        return Stream.of(
            arguments(Head.Direction.RIGHT, Input.Keys.A),
            arguments(Head.Direction.UP, Input.Keys.S),
            arguments(Head.Direction.LEFT, Input.Keys.D),
            arguments(Head.Direction.DOWN, Input.Keys.W)
        );
    }

    @BeforeEach
    void setUp() {
        Gdx.input = mock(Input.class);
    }

    @ParameterizedTest
    @MethodSource("directionChangedArguments")
    void givenKeyPressed_thenDirectionChanged(
        Head.Direction current,
        int keyPressed,
        Head.Direction next
    ) {
        dominion.createEntity(new Head(current));
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        inputSystem.run();
        assertThat(dominion.findCompositionsWith(Head.class))
            .singleElement()
            .extracting(head -> head.current, head -> head.next)
            .containsExactly(current, next);
    }

    @Test
    void givenNextDirectionOpposite_thenDirectionChanged() {
        dominion.createEntity(new Head(Head.Direction.UP, Head.Direction.RIGHT));
        when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
        inputSystem.run();
        assertThat(dominion.findCompositionsWith(Head.class))
            .singleElement()
            .extracting(head -> head.current, head -> head.next)
            .containsExactly(Head.Direction.UP, Head.Direction.LEFT);
    }

    @ParameterizedTest
    @MethodSource("directionNotChangedArguments")
    void givenOppositeDirection_thenDirectionNotChanged(
        Head.Direction direction,
        int keyPressed
    ) {
        dominion.createEntity(new Head(direction));
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        inputSystem.run();
        assertThat(dominion.findCompositionsWith(Head.class))
            .singleElement()
            .extracting(head -> head.current, head -> head.next)
            .containsExactly(direction, direction);
    }
}