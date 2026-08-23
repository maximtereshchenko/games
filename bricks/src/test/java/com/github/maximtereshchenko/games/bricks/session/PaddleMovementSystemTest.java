package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class PaddleMovementSystemTest {

    private final Registry registry = new Registry();
    private final Configuration configuration = mock();
    private final Viewport viewport = mock();
    private final Input input = mock();
    private final PaddleMovementSystem paddleMovementSystem =
        new PaddleMovementSystem(registry, configuration, viewport);

    @BeforeEach
    void setUp() {
        Gdx.input = input;
        when(configuration.worldDimensions()).thenReturn(new Configuration.Dimensions(20, 10));
        registry.addSystems(paddleMovementSystem);
    }

    @Test
    void givenPointer_thenPaddleVelocityUpdated() {
        when(input.getX()).thenReturn(100);
        when(input.getY()).thenReturn(50);
        doAnswer(invocation -> {
            invocation.getArgument(0, Vector2.class).set(8, 1);
            return invocation.getArgument(0);
        }).when(viewport).unproject(any(Vector2.class));
        var velocity = new Velocity(new Vector2());
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(4, 1)),
            velocity
        );
        registry.update(2);
        assertThat(velocity.vector2().x).isEqualTo(2);
    }

    @Test
    void givenPointerOutsideWorld_thenTargetClamped() {
        when(input.getX()).thenReturn(0);
        when(input.getY()).thenReturn(0);
        doAnswer(invocation -> {
            invocation.getArgument(0, Vector2.class).set(50, 1);
            return invocation.getArgument(0);
        }).when(viewport).unproject(any(Vector2.class));
        var velocity = new Velocity(new Vector2());
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(10, 1)),
            velocity
        );
        registry.update(1);
        assertThat(velocity.vector2().x).isEqualTo(10);
    }
}
