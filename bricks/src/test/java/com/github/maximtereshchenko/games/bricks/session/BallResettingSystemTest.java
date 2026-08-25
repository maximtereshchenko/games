package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class BallResettingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> ballEntities =
        registry.view(new Query().all(Ball.class, Attached.class, WorldPosition.class));
    private final Blueprints blueprints =
        new Blueprints.Builder(Map.of(BricksBlueprints.BALL, List.of(Ball.INSTANCE))).build();
    private final BallResettingSystem ballResettingSystem =
        new BallResettingSystem(registry, blueprints);

    @BeforeEach
    void setUp() {
        registry.addSystems(ballResettingSystem);
    }

    @Test
    void givenNoBalls_thenBallSpawnedAtPaddle() {
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(10, 5)),
            new BallOffset(2)
        );
        registry.update(0);
        assertThat(ballEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class).vector2())
            .usingRecursiveComparison()
            .isEqualTo(new Vector2(10, 7));
    }

    @Test
    void givenBall_thenBallNotSpawned() {
        registry.addComponents(registry.createEntity(), Ball.INSTANCE);
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(10, 5)),
            new BallOffset(2)
        );
        registry.update(0);
        assertThat(registry.view(new Query().all(Attached.class))).isEmpty();
    }
}
