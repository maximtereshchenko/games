package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class BrickCollisionSystem extends CollisionSystem {

    BrickCollisionSystem(Registry registry) {
        super(
            registry,
            new Class[]{Ball.class},
            new Class[]{Brick.class}
        );
    }

    @Override
    void onCollision(
        RegistryEdit registryEdit,
        Entity colliderEntity,
        Entity impactedEntity
    ) {
        registryEdit.addComponents(impactedEntity.id(), Destroyed.INSTANCE);
    }
}
