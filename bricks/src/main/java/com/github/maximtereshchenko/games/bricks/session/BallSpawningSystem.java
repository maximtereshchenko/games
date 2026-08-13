package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallSpawningSystem implements System {

    private final Iterable<Entity> spawnBallsEntities;
    private final Iterable<Entity> paddleEntities;

    BallSpawningSystem(Registry registry) {
        this.spawnBallsEntities = registry.entities(
            new Query().all(SpawnBalls.class, Removed.class)
        );
        this.paddleEntities = registry.entities(
            new Query()
                .all(
                    Paddle.class,
                    WorldPosition.class,
                    Rectangle.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var spawnBallsEntity : spawnBallsEntities) {
            var spawnBalls = spawnBallsEntity.component(SpawnBalls.class);
            for (var paddleEntity : paddleEntities) {
                var worldPosition = paddleEntity.component(
                    WorldPosition.class
                );
                var rectangle = paddleEntity.component(Rectangle.class);
                var circle = new Circle(0.1f);
                for (var i = 1; i <= spawnBalls.amount(); i++) {
                    var vector2 = new Vector2(worldPosition.vector2());
                    vector2.y += (rectangle.halfHeight + circle.radius()) * 1.1f;
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        Ball.INSTANCE,
                        BodyDef.BodyType.DynamicBody,
                        new CollisionGroupIndex(-1),
                        circle,
                        new WorldPosition(vector2),
                        new Velocity(
                            new Vector2(0, 5)
                                .rotateDeg(90)
                                .rotateDeg(
                                    -180f * i /
                                    (spawnBalls.amount() + 1)
                                )
                        ),
                        new Speed(5),
                        new Visible(Color.valueOf("#feffff"))
                    );
                }
            }
        }
    }
}
