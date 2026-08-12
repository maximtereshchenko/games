package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class BonusCollisionSystem extends CollisionSystem {

    BonusCollisionSystem(Registry registry) {
        super(
            registry,
            new Class[]{Paddle.class},
            new Class[]{WidenPaddle.class}
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
