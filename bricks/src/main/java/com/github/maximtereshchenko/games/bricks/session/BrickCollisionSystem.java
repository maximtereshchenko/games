package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BrickCollisionSystem implements System {

    private final Iterable<Entity> ballEntities;
    private final Iterable<Entity> brickEntities;

    BrickCollisionSystem(Registry registry) {
        this.ballEntities = registry.entities(
            new Query().all(Ball.class, Collision.class)
        );
        this.brickEntities = registry.entities(
            new Query().all(Brick.class, Collision.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var ballEntity : ballEntities) {
            var ballCollision = ballEntity.component(Collision.class);
            for (var brickEntity : brickEntities) {
                var brickCollision = brickEntity.component(Collision.class);
                if (ballCollision.entityId() == brickEntity.id() && brickCollision.entityId() == ballEntity.id()) {
                    registryEdit.addComponents(brickEntity.id(), Destroyed.INSTANCE);
                }
            }
        }
    }
}
