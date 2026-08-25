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

final class BallSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> ballEntities =
        registry.view(new Query().all(Ball.class));
    private final Iterable<Entity> commandEntities =
        registry.view(new Query().all(SpawnBallCommand.class));
    private final Blueprints blueprints =
        new Blueprints.Builder(Map.of(BricksBlueprints.BALL, List.of(Ball.INSTANCE))).build();
    private final BallSpawningSystem ballSpawningSystem =
        new BallSpawningSystem(registry, blueprints);

    @BeforeEach
    void setUp() {
        registry.addSystems(ballSpawningSystem);
    }

    @Test
    void givenSpawnBallCommandBelowLimit_thenBallSpawnedAndCommandDeleted() {
        registry.addComponents(registry.createEntity(), new BallLimit(2));
        registry.addComponents(
            registry.createEntity(),
            new SpawnBallCommand(
                new WorldPosition(new Vector2(1, 2)),
                new Velocity(new Vector2(0, 1))
            )
        );
        registry.update(0);
        assertThat(ballEntities).hasSize(1);
        assertThat(commandEntities).isEmpty();
    }

    @Test
    void givenBallLimitReached_thenCommandDeletedWithoutSpawning() {
        registry.addComponents(registry.createEntity(), new BallLimit(1));
        registry.addComponents(registry.createEntity(), Ball.INSTANCE);
        registry.addComponents(
            registry.createEntity(),
            new SpawnBallCommand(
                new WorldPosition(new Vector2(1, 2)),
                new Velocity(new Vector2(0, 1))
            )
        );
        registry.update(0);
        assertThat(ballEntities).hasSize(1);
        assertThat(commandEntities).isEmpty();
    }
}
