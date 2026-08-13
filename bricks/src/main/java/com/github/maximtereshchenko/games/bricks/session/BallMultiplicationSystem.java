package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallMultiplicationSystem implements System {

    private final Iterable<Entity> multiplyBallsEntities;
    private final Iterable<Entity> ballsEntities;

    BallMultiplicationSystem(Registry registry) {
        this.multiplyBallsEntities = registry.entities(
            new Query().all(MultiplyBalls.class, Removed.class)
        );
        this.ballsEntities = registry.entities(
            new Query()
                .all(
                    Ball.class,
                    Circle.class,
                    WorldPosition.class,
                    Velocity.class,
                    Visible.class,
                    Fixture.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var multiplyBallsEntity : multiplyBallsEntities) {
            var multiplyBalls = multiplyBallsEntity.component(
                MultiplyBalls.class
            );
            for (var ballsEntity : ballsEntities) {
                var circle = ballsEntity.component(Circle.class);
                var worldPosition = ballsEntity.component(
                    WorldPosition.class
                );
                var velocity = ballsEntity.component(Velocity.class);
                var visible = ballsEntity.component(Visible.class);
                var fixture = ballsEntity.component(Fixture.class);
                var angleStart = velocity.vector2().angleDeg() - 90;
                var rotation = 180 / multiplyBalls.factor();
                for (var i = 1; i < multiplyBalls.factor(); i++) {
                    var vector2 = new Vector2(velocity.vector2());
                    vector2.setAngleDeg(angleStart + rotation * i);
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        Ball.INSTANCE,
                        fixture.getBody().getType(),
                        new CollisionGroupIndex(
                            fixture.getFilterData().groupIndex
                        ),
                        circle,
                        new WorldPosition(new Vector2(worldPosition.vector2())),
                        new Velocity(vector2),
                        visible
                    );
                }
            }
        }
    }
}
