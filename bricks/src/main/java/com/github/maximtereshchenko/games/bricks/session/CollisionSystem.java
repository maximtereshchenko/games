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
            var colliderCollisions = colliderEntity.component(Collisions.class);
            for (var impactedEntity : impactedEntities) {
                var impactedCollisions = impactedEntity.component(Collisions.class);
                if (
                    contains(colliderCollisions, impactedEntity) &&
                    contains(impactedCollisions, colliderEntity)
                ) {
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

    private boolean contains(Collisions collisions, Entity entity) {
        return collisions.entityIds().contains(entity.id());
    }

    private Class<?>[] withCollision(Class<?>[] components) {
        var copy = Arrays.copyOf(components, components.length + 1);
        copy[copy.length - 1] = Collisions.class;
        return copy;
    }
}
