package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallMultiplicationSystem implements System {

    private final Iterable<Entity> multiplyBallsEntities;
    private final Iterable<Entity> ballsEntities;
    private final Blueprints blueprints;

    BallMultiplicationSystem(
        Registry registry,
        Blueprints blueprints
    ) {
        this.multiplyBallsEntities = registry.entities(
            new Query().all(MultiplyBalls.class, Activated.class)
        );
        this.ballsEntities = registry.entities(
            new Query()
                .all(
                    Ball.class,
                    WorldPosition.class,
                    Velocity.class
                )
        );
        this.blueprints = blueprints;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var multiplyBallsEntity : multiplyBallsEntities) {
            var multiplyBalls = multiplyBallsEntity.component(
                MultiplyBalls.class
            );
            for (var ballsEntity : ballsEntities) {
                var worldPosition = ballsEntity.component(
                    WorldPosition.class
                );
                var velocity = ballsEntity.component(Velocity.class);
                for (var i = 1; i < multiplyBalls.factor(); i++) {
                    var vector2 = new Vector2(velocity.vector2());
                    vector2.setAngleDeg(
                        vector2.angleDeg() + 90 -
                        180f * i / multiplyBalls.factor());
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        blueprints.components(
                            BricksBlueprints.BALL,
                            new WorldPosition(
                                new Vector2(worldPosition.vector2())
                            ),
                            new Velocity(vector2)
                        )
                    );
                }
            }
        }
    }
}
