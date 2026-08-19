package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class SpawnBallsBonusSystem implements System {

    private final Iterable<Entity> spawnBallsEntities;
    private final Iterable<Entity> paddleEntities;

    SpawnBallsBonusSystem(Registry registry) {
        this.spawnBallsEntities = registry.entities(
            new Query().all(SpawnBallsBonus.class, Activated.class)
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
            var spawnBalls = spawnBallsEntity.component(SpawnBallsBonus.class);
            for (var paddleEntity : paddleEntities) {
                var worldPosition = paddleEntity.component(
                    WorldPosition.class
                );
                var ballOffset = paddleEntity.component(BallOffset.class);
                for (var i = 1; i <= spawnBalls.amount(); i++) {
                    var vector2 = new Vector2(worldPosition.vector2());
                    vector2.y += ballOffset.value();
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        new SpawnBallCommand(
                            new WorldPosition(vector2),
                            new Velocity(
                                new Vector2(0, 5)
                                    .rotateDeg(90)
                                    .rotateDeg(
                                        -180f * i /
                                        (spawnBalls.amount() + 1)
                                    )
                            )
                        )
                    );
                }
            }
        }
    }
}
