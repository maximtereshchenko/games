package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PaddleCollisionSystem implements System {

    private final Iterable<Entity> paddleEntities;
    private final Iterable<Entity> ballEntities;

    PaddleCollisionSystem(Registry registry) {
        this.paddleEntities = registry.entities(
            new Query()
                .all(
                    Paddle.class,
                    Collision.class,
                    WorldPosition.class,
                    Rectangle.class
                )
        );
        this.ballEntities = registry.entities(
            new Query()
                .all(
                    Ball.class,
                    Collision.class,
                    WorldPosition.class,
                    Velocity.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var paddleEntity : paddleEntities) {
            var paddleCollision = paddleEntity.component(Collision.class);
            var paddleWorldPosition = paddleEntity.component(WorldPosition.class);
            var paddleRectangle = paddleEntity.component(Rectangle.class);
            for (var ballEntity : ballEntities) {
                var ballCollision = ballEntity.component(Collision.class);
                var ballWorldPosition = ballEntity.component(WorldPosition.class);
                var ballVelocity = ballEntity.component(Velocity.class);
                if (paddleCollision.entityId() == ballEntity.id() && ballCollision.entityId() == paddleEntity.id()) {
                    var vector2 = ballVelocity.vector2();
                    var speed = 5;
                    vector2.set(
                        (ballWorldPosition.vector2().x - paddleWorldPosition.vector2().x) /
                        (paddleRectangle.width / 2f) * speed,
                        speed
                    );
                    vector2.nor().scl(speed);
                }
            }
        }
    }
}
