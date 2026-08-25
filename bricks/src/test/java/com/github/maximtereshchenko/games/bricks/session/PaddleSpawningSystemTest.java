package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class PaddleSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> paddleEntities =
        registry.view(new Query().all(Paddle.class));
    private final Blueprints blueprints =
        new Blueprints.Builder(Map.of(BricksBlueprints.PADDLE, List.of(Paddle.INSTANCE)))
            .build();
    private final PaddleSpawningSystem paddleSpawningSystem =
        new PaddleSpawningSystem(registry, blueprints);

    @BeforeEach
    void setUp() {
        registry.addSystems(paddleSpawningSystem);
    }

    @Test
    void givenNoPaddle_thenPaddleSpawned() {
        registry.update(0);
        assertThat(paddleEntities).hasSize(1);
    }

    @Test
    void givenPaddle_thenPaddleNotSpawnedAgain() {
        registry.addComponents(registry.createEntity(), Paddle.INSTANCE);
        registry.update(0);
        assertThat(paddleEntities).hasSize(1);
    }
}
