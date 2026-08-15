package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.Random;

final class StarSpawningSystem implements System {

    private final Iterable<Entity> starsSpawnedEntities;
    private final Iterable<Entity> brickEntities;
    private final Iterable<Entity> removedBrickEntities;
    private final Random random;

    StarSpawningSystem(Registry registry, Random random) {
        this.starsSpawnedEntities = registry.entities(
            new Query().all(SpawnedStars.class)
        );
        this.brickEntities = registry.entities(
            new Query().all(Brick.class)
        );
        this.removedBrickEntities = registry.entities(
            new Query()
                .all(
                    Brick.class,
                    Removed.class,
                    WorldPosition.class
                )
        );
        this.random = random;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        var bricksCount = bricksCount();
        for (var starsSpawnedEntity : starsSpawnedEntities) {
            var starsSpawned = starsSpawnedEntity.component(
                SpawnedStars.class
            );
            for (var removedBrickEntity : removedBrickEntities) {
                var worldPosition = removedBrickEntity.component(
                    WorldPosition.class
                );
                if (starsSpawned.accumulated == starsSpawned.max) {
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
        return (float) (starsSpawned.max - starsSpawned.accumulated) /
               bricksCount;
    }

    private int bricksCount() {
        var bricksCount = 0;
        for (var _ : brickEntities) {
            bricksCount++;
        }
        return bricksCount;
    }

    private void spawnStar(
        RegistryEdit registryEdit,
        Vector2 vector2
    ) {
        registryEdit.addComponents(
            registryEdit.createEntity(),
            Bonus.INSTANCE,
            IncrementStars.INSTANCE,
            BodyDef.BodyType.DynamicBody,
            Sensor.INSTANCE,
            new Star(0.2f),
            new WorldPosition(new Vector2(vector2)),
            new Velocity(new Vector2(0, -3)),
            new Visible(Color.GOLD)
        );
    }
}
