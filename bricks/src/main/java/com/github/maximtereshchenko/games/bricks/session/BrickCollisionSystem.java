package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class BrickCollisionSystem extends CollisionSystem {

    BrickCollisionSystem(Registry registry) {
        super(
            registry,
            new Query().all(Ball.class),
            new Query().all(Brick.class)
        );
    }

    @Override
    void onCollision(
        RegistryEdit registryEdit,
        Entity colliderEntity,
        Entity impactedEntity
    ) {
        registryEdit.addComponents(impactedEntity.id(), Removed.INSTANCE);
    }
}
