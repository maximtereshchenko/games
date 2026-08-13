package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class PaddleCollisionSystem extends CollisionSystem {

    PaddleCollisionSystem(Registry registry) {
        super(
            registry,
            new Query()
                .all(
                    Paddle.class,
                    WorldPosition.class,
                    Rectangle.class
                ),
            new Query()
                .all(
                    Ball.class,
                    Velocity.class,
                    WorldPosition.class
                )
        );
    }

    @Override
    void onCollision(
        RegistryEdit registryEdit,
        Entity colliderEntity,
        Entity impactedEntity
    ) {
        var paddleWorldPosition = colliderEntity.component(WorldPosition.class);
        var paddleRectangle = colliderEntity.component(Rectangle.class);
        var ballVelocity = impactedEntity.component(Velocity.class);
        var ballWorldPosition = impactedEntity.component(WorldPosition.class);
        ballVelocity.vector2()
            .setAngleDeg(
                90 - 45 *
                     (ballWorldPosition.vector2().x - paddleWorldPosition.vector2().x) /
                     paddleRectangle.halfWidth
            );
    }
}
