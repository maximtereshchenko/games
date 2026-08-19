package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallSpawningSystem implements System {

    private final Iterable<Entity> spawnBallCommandEntities;
    private final Iterable<Entity> ballLimitEntities;
    private final Iterable<Entity> ballEntities;
    private final Blueprints blueprints;

    BallSpawningSystem(Registry registry, Blueprints blueprints) {
        this.spawnBallCommandEntities = registry.entities(
            new Query().all(SpawnBallCommand.class)
        );
        this.ballLimitEntities = registry.entities(
            new Query().all(BallLimit.class)
        );
        this.ballEntities = registry.entities(
            new Query().all(Ball.class)
        );
        this.blueprints = blueprints;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        var balls = balls();
        for (var ballLimitEntity : ballLimitEntities) {
            var ballLimit = ballLimitEntity.component(BallLimit.class);
            for (var spawnBallCommandEntity : spawnBallCommandEntities) {
                var spawnBallCommand = spawnBallCommandEntity.component(
                    SpawnBallCommand.class
                );
                if (balls < ballLimit.value()) {
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        blueprints.components(
                            BricksBlueprints.BALL,
                            spawnBallCommand.worldPosition(),
                            spawnBallCommand.velocity()
                        )
                    );
                    balls++;
                }
                registryEdit.deleteEntity(spawnBallCommandEntity.id());
            }
        }
    }

    private int balls() {
        var count = 0;
        for (var _ : ballEntities) {
            count++;
        }
        return count;
    }
}
