package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class PaddleWideningSystem extends WidthResizingSystem {

    private final Iterable<Entity> widenPaddleEntities;
    private final Iterable<Entity> paddleEntities;

    PaddleWideningSystem(Registry registry) {
        this.widenPaddleEntities = registry.entities(
            new Query().all(WidenPaddle.class, Activated.class)
        );
        this.paddleEntities = registry.entities(
            new Query()
                .all(
                    Paddle.class,
                    Rectangle.class,
                    MaxWidth.class,
                    ResetWidthRemainingTime.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var widenPaddleEntity : widenPaddleEntities) {
            var widenPaddle = widenPaddleEntity.component(WidenPaddle.class);
            for (var paddleEntity : paddleEntities) {
                var rectangle = paddleEntity.component(Rectangle.class);
                var maxWidth = paddleEntity.component(MaxWidth.class);
                var resetWidthRemainingTime = paddleEntity.component(
                    ResetWidthRemainingTime.class
                );
                resetWidthRemainingTime.seconds += widenPaddle.extraTimeSeconds();
                resize(
                    registryEdit,
                    paddleEntity,
                    rectangle,
                    Math.min(
                        rectangle.halfWidth * 2 + widenPaddle.extraWidth(),
                        maxWidth.value()
                    )
                );
            }
        }
    }
}
