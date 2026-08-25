package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class InputSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> directionIntentEntities =
        registry.view(new Query().all(DirectionIntent.class));
    private final InputSystem inputSystem = new InputSystem(registry);

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
        registry.addSystems(inputSystem);
        Gdx.input = mock();
    }

    @ParameterizedTest
    @MethodSource("directionChangedArguments")
    void givenKeyPressed_thenDirectionChanged(
        int keyPressed,
        Direction expected
    ) {
        registry.addComponents(
            registry.createEntity(),
            new DirectionIntent(Set.of(), Direction.RIGHT)
        );
        when(Gdx.input.isKeyPressed(keyPressed)).thenReturn(true);
        registry.update(0);
        assertThat(directionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(DirectionIntent.class))
            .usingRecursiveComparison()
            .isEqualTo(new DirectionIntent(Set.of(), expected));
    }
}
