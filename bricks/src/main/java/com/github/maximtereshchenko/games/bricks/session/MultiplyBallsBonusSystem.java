package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class MultiplyBallsBonusSystem implements System {

    private final Iterable<Entity> multiplyBallsEntities;
    private final Iterable<Entity> ballsEntities;

    MultiplyBallsBonusSystem(Registry registry) {
        this.multiplyBallsEntities = registry.entities(
            new Query().all(MultiplyBallsBonus.class, Activated.class)
        );
        this.ballsEntities = registry.entities(
            new Query()
                .all(
                    Ball.class,
                    WorldPosition.class,
                    Velocity.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var multiplyBallsEntity : multiplyBallsEntities) {
            var multiplyBalls = multiplyBallsEntity.component(
                MultiplyBallsBonus.class
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
                        new SpawnBallCommand(
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
