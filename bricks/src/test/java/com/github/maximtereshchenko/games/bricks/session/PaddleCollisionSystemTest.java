package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class PaddleCollisionSystemTest {

    private final Registry registry = new Registry();
    private final PaddleCollisionSystem paddleCollisionSystem =
        new PaddleCollisionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(paddleCollisionSystem);
    }

    @Test
    void givenBallAbovePaddle_thenVelocityAngleChanged() {
        var paddleId = registry.createEntity();
        var ballId = registry.createEntity();
        var velocity = new Velocity(new Vector2(0, -1));
        registry.addComponents(
            paddleId,
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(10, 5)),
            new Rectangle(5, 0.5f),
            new Collisions(Set.of(ballId))
        );
        registry.addComponents(
            ballId,
            Ball.INSTANCE,
            velocity,
            new WorldPosition(new Vector2(15, 6)),
            new Collisions(Set.of(paddleId))
        );
        registry.update(0);
        assertThat(velocity.vector2().angleDeg()).isEqualTo(45);
    }

    @Test
    void givenBallBelowPaddle_thenVelocityUnchanged() {
        var paddleId = registry.createEntity();
        var ballId = registry.createEntity();
        var velocity = new Velocity(new Vector2(0, -1));
        registry.addComponents(
            paddleId,
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(10, 5)),
            new Rectangle(5, 0.5f),
            new Collisions(Set.of(ballId))
        );
        registry.addComponents(
            ballId,
            Ball.INSTANCE,
            velocity,
            new WorldPosition(new Vector2(10, 4)),
            new Collisions(Set.of(paddleId))
        );
        registry.update(0);
        assertThat(velocity.vector2()).usingRecursiveComparison().isEqualTo(new Vector2(0, -1));
    }
}
