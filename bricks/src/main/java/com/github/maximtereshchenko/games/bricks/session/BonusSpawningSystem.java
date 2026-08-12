package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

final class BonusSpawningSystem implements System {

    private final Iterable<Entity> bonusSpawnPolicyEntities;
    private final Iterable<Entity> removedBrickEntities;
    private final Random random;

    BonusSpawningSystem(Registry registry, Random random) {
        this.bonusSpawnPolicyEntities = registry.entities(
            new Query().all(BonusSpawnPolicy.class)
        );
        this.removedBrickEntities = registry.entities(
            new Query().all(Brick.class, Removed.class, WorldPosition.class)
        );
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
                        worldPosition.vector2(),
                        bonusSpawnPolicy.components()
                    );
                }

            }
        }
    }

    private void spawnBonus(
        RegistryEdit registryEdit,
        Vector2 vector2,
        List<Object[]> components
    ) {
        var all = new ArrayList<>();
        all.add(BodyDef.BodyType.DynamicBody);
        all.add(Sensor.INSTANCE);
        all.add(new Circle(0.2f));
        all.add(new WorldPosition(new Vector2(vector2)));
        all.add(new Velocity(new Vector2(0, -1)));
        all.addAll(
            Arrays.asList(
                components.get(random.nextInt(components.size()))
            )
        );
        registryEdit.addComponents(
            registryEdit.createEntity(),
            all.toArray()
        );
    }
}
