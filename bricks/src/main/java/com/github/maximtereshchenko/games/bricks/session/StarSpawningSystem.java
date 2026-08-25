package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.Random;

final class StarSpawningSystem implements System {

    private final Iterable<Entity> starsSpawnedEntities;
    private final View brickEntities;
    private final Iterable<Entity> removedBrickEntities;
    private final Configuration configuration;
    private final Blueprints blueprints;
    private final Random random;

    StarSpawningSystem(
        Registry registry,
        Configuration configuration,
        Blueprints blueprints,
        Random random
    ) {
        this.starsSpawnedEntities = registry.view(
            new Query().all(SpawnedStars.class)
        );
        this.brickEntities = registry.view(
            new Query().all(Brick.class)
        );
        this.removedBrickEntities = registry.view(
            new Query()
                .all(
                    Brick.class,
                    Removed.class,
                    WorldPosition.class
                )
        );
        this.configuration = configuration;
        this.blueprints = blueprints;
        this.random = random;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        var bricksCount = brickEntities.size();
        for (var starsSpawnedEntity : starsSpawnedEntities) {
            var starsSpawned = starsSpawnedEntity.component(
                SpawnedStars.class
            );
            for (var removedBrickEntity : removedBrickEntities) {
                var worldPosition = removedBrickEntity.component(
                    WorldPosition.class
                );
                if (starsSpawned.accumulated == configuration.maxStars()) {
                    continue;
                }
                if (shouldSpawn(starsSpawned, bricksCount)) {
                    spawnStar(
                        registryEdit,
                        worldPosition.vector2()
                    );
                    starsSpawned.accumulated++;
                }
                bricksCount--;
            }
        }
    }

    private boolean shouldSpawn(
        SpawnedStars starsSpawned,
        int bricksCount
    ) {
        return random.nextFloat() < chance(starsSpawned, bricksCount);
    }

    private float chance(SpawnedStars starsSpawned, int bricksCount) {
        return (float) (configuration.maxStars() - starsSpawned.accumulated) /
               bricksCount;
    }

    private void spawnStar(
        RegistryEdit registryEdit,
        Vector2 vector2
    ) {
        registryEdit.addComponents(
            registryEdit.createEntity(),
            blueprints.components(
                BricksBlueprints.INCREMENT_STARS_BONUS,
                new WorldPosition(new Vector2(vector2))
            )
        );
    }
}
