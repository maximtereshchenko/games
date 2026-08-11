package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BrickRemovalSystem implements System {

    private final Iterable<Entity> entities;
    private final World physicsWorld;

    BrickRemovalSystem(Registry registry, World physicsWorld) {
        this.entities = registry.entities(
            new Query().all(Brick.class, Destroyed.class, Physics.class)
        );
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var physicsComponent = entity.component(Physics.class);
            physicsWorld.destroyBody(physicsComponent.fixture().getBody());
            registryEdit.deleteEntity(entity.id());
        }
    }
}
