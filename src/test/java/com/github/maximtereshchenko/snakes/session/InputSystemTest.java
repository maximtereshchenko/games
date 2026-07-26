package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
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

    private final World world = new World();
    private final InputSystem inputSystem = new InputSystem(world);

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
        world.addSystems(inputSystem);
        Gdx.input = mock();
    }

    @ParameterizedTest
    @MethodSource("directionChangedArguments")
    void givenKeyPressed_thenDirectionChanged(
        int keyPressed,
        Direction expected
    ) {
        world.addComponents(
            world.createEntity(),
            new NextForwardDirection(Direction.RIGHT)
        );
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        world.update(0);
        assertThat(world.entities(new Query().all(NextForwardDirection.class)))
            .singleElement()
            .extracting(entity -> entity.component(NextForwardDirection.class).value)
            .isEqualTo(expected);
    }
}