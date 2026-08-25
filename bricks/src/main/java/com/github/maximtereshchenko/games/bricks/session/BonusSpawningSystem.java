package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

final class BonusSpawningSystem implements System {

    private final Iterable<Entity> bonusSpawnPolicyEntities;
    private final Iterable<Entity> removedBrickEntities;
    private final Blueprints blueprints;
    private final Random random;

    BonusSpawningSystem(
        Registry registry,
        Blueprints blueprints,
        Random random
    ) {
        this.bonusSpawnPolicyEntities = registry.view(
            new Query().all(BonusSpawnPolicy.class)
        );
        this.removedBrickEntities = registry.view(
            new Query()
                .all(
                    Brick.class,
                    Removed.class,
                    WorldPosition.class
                )
        );
        this.blueprints = blueprints;
        this.random = random;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var bonusSpawnPolicyEntity : bonusSpawnPolicyEntities) {
            var bonusSpawnPolicy = bonusSpawnPolicyEntity.component(
                BonusSpawnPolicy.class
            );
            for (var removedBrickEntity : removedBrickEntities) {
                var worldPosition = removedBrickEntity.component(
                    WorldPosition.class
                );
                if (random.nextFloat() <= bonusSpawnPolicy.chance()) {
                    spawnBonus(
                        registryEdit,
                        bonusSpawnPolicy,
                        worldPosition
                    );
                }
            }
        }
    }

    private void spawnBonus(
        RegistryEdit registryEdit,
        BonusSpawnPolicy bonusSpawnPolicy,
        WorldPosition worldPosition
    ) {
        blueprint(bonusSpawnPolicy.bonusChances())
            .ifPresent(
                blueprint -> registryEdit.addComponents(
                    registryEdit.createEntity(),
                    blueprints.components(
                        blueprint,
                        new WorldPosition(
                            new Vector2(
                                worldPosition.vector2()
                            )
                        )
                    )
                )
            );
    }

    private Optional<String> blueprint(Map<String, Float> componentChances) {
        var chance = random.nextFloat();
        for (var entry : componentChances.entrySet()) {
            chance -= entry.getValue();
            if (chance < 0) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
