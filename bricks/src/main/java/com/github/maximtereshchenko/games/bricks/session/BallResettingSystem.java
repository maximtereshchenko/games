package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallResettingSystem implements System {

    private final Iterable<Entity> ballEntities;
    private final Iterable<Entity> paddleEntities;
    private final Blueprints blueprints;

    BallResettingSystem(
        Registry registry,
        Blueprints blueprints
    ) {
        this.ballEntities = registry.view(
            new Query().all(Ball.class)
        );
        this.paddleEntities = registry.view(
            new Query()
                .all(
                    Paddle.class,
                    WorldPosition.class,
                    BallOffset.class
                )
        );
        this.blueprints = blueprints;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (ballEntities.iterator().hasNext()) {
            return;
        }
        for (var paddleEntity : paddleEntities) {
            var worldPosition = paddleEntity.component(WorldPosition.class);
            var ballOffset = paddleEntity.component(BallOffset.class);
            var vector2 = new Vector2(worldPosition.vector2());
            vector2.y += ballOffset.value();
            registryEdit.addComponents(
                registryEdit.createEntity(),
                blueprints.components(
                    BricksBlueprints.BALL,
                    Attached.INSTANCE,
                    new WorldPosition(vector2)
                )
            );
        }
    }
}
