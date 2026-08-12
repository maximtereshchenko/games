package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class PaddleCollisionSystem extends CollisionSystem {

    PaddleCollisionSystem(Registry registry) {
        super(
            registry,
            new Class[]{
                Paddle.class,
                WorldPosition.class,
                Rectangle.class
            },
            new Class[]{
                Ball.class,
                Velocity.class,
                WorldPosition.class
            }
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
        var vector2 = ballVelocity.vector2();
        var speed = vector2.len(); //TODO constant
        vector2.set(
            (ballWorldPosition.vector2().x - paddleWorldPosition.vector2().x) /
            (paddleRectangle.width / 2f) * speed,
            speed
        );
        vector2.nor().scl(speed);
    }
}
