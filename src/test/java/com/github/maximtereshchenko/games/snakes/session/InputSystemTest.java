package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.BeforeEach;
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
            arguments(Input.Keys.W, Direction.UP),
            arguments(Input.Keys.A, Direction.LEFT),
            arguments(Input.Keys.S, Direction.DOWN),
            arguments(Input.Keys.D, Direction.RIGHT)
        );
    }

    @BeforeEach
    void setUp() {
        Gdx.input = mock();
    }

    @ParameterizedTest
    @MethodSource("directionChangedArguments")
    void givenKeyPressed_thenDirectionChanged(
        int keyPressed,
        Direction expected
    ) {
        dominion.createEntity(new NextForwardDirection(Direction.RIGHT));
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        inputSystem.run(0);
        assertThat(dominion.findCompositionsWith(NextForwardDirection.class))
            .singleElement()
            .extracting(nextDirection -> nextDirection.value)
            .isEqualTo(expected);
    }
}