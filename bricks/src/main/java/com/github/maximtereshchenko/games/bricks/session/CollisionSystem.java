package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.Arrays;

abstract class CollisionSystem implements System {

    private final Iterable<Entity> colliderEntities;
    private final Iterable<Entity> impactedEntities;

    CollisionSystem(
        Registry registry,
        Class<?>[] colliderComponents,
        Class<?>[] impactedComponents
    ) {
        this.colliderEntities = registry.entities(
            new Query().all(withCollision(colliderComponents))
        );
        this.impactedEntities = registry.entities(
            new Query().all(withCollision(impactedComponents))
        );
    }

    @Override
    public final void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var colliderEntity : colliderEntities) {
            var colliderCollision = colliderEntity.component(Collision.class);
            for (var impactedEntity : impactedEntities) {
                var impactedCollision = impactedEntity.component(Collision.class);
                if (colliderCollision.entityId() == impactedEntity.id() &&
                    impactedCollision.entityId() == colliderEntity.id()) {
                    onCollision(registryEdit, colliderEntity, impactedEntity);
                }
            }
        }
    }

    abstract void onCollision(
        RegistryEdit registryEdit,
        Entity colliderEntity,
        Entity impactedEntity
    );

    private Class<?>[] withCollision(Class<?>[] components) {
        var copy = Arrays.copyOf(components, components.length + 1);
        copy[copy.length - 1] = Collision.class;
        return copy;
    }
}
