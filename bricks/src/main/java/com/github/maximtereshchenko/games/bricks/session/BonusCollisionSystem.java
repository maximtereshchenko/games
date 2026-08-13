package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class BonusCollisionSystem extends CollisionSystem {

    BonusCollisionSystem(Registry registry) {
        super(
            registry,
            new Query().all(Paddle.class),
            new Query()
                .one(
                    WidenPaddle.class,
                    SpawnBarrier.class,
                    MultiplyBalls.class,
                    SpawnBalls.class
                )
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
