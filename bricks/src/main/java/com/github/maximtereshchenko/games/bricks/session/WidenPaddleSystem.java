package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class WidenPaddleSystem implements System {

    private final Iterable<Entity> widenPaddleEntities;
    private final Iterable<Entity> paddleEntities;

    WidenPaddleSystem(Registry registry) {
        this.widenPaddleEntities = registry.entities(
            new Query().all(WidenPaddle.class, Removed.class)
        );
        this.paddleEntities = registry.entities(
            new Query().all(Paddle.class, Rectangle.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var widenPaddleEntity : widenPaddleEntities) {
            var widenPaddle = widenPaddleEntity.component(WidenPaddle.class);
            for (var paddleEntity : paddleEntities) {
                var rectangle = paddleEntity.component(Rectangle.class);
                rectangle.width += widenPaddle.extraWidth();
                registryEdit.addComponents(paddleEntity.id(), Resized.INSTANCE);
            }
        }
    }
}
