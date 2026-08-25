package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PaddleWideningSystem implements System {

    private final Iterable<Entity> widenPaddleEntities;
    private final Iterable<Entity> paddleEntities;

    PaddleWideningSystem(Registry registry) {
        this.widenPaddleEntities = registry.view(
            new Query().all(WidenPaddleBonus.class, Activated.class)
        );
        this.paddleEntities = registry.view(
            new Query()
                .all(
                    Paddle.class,
                    Rectangle.class,
                    MaxHalfWidth.class,
                    ResetWidthRemainingTime.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var widenPaddleEntity : widenPaddleEntities) {
            var widenPaddle = widenPaddleEntity.component(WidenPaddleBonus.class);
            for (var paddleEntity : paddleEntities) {
                var rectangle = paddleEntity.component(Rectangle.class);
                var maxHalfWidth = paddleEntity.component(MaxHalfWidth.class);
                var resetWidthRemainingTime = paddleEntity.component(
                    ResetWidthRemainingTime.class
                );
                resetWidthRemainingTime.seconds += widenPaddle.addedTimeSeconds();
                registryEdit.addComponents(
                    paddleEntity.id(),
                    new UpdateWidthCommand(
                        Math.min(
                            rectangle.halfWidth + widenPaddle.addedHalfWidth(),
                            maxHalfWidth.value()
                        )
                    )
                );
            }
        }
    }
}
