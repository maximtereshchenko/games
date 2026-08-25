package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PaddleShorteningSystem implements System {

    private final Iterable<Entity> shortenPaddleEntities;
    private final Iterable<Entity> paddleEntities;

    PaddleShorteningSystem(Registry registry) {
        this.shortenPaddleEntities = registry.view(
            new Query().all(ShortenPaddleBonus.class, Activated.class)
        );
        this.paddleEntities = registry.view(
            new Query()
                .all(
                    Paddle.class,
                    Rectangle.class,
                    MinHalfWidth.class,
                    ResetWidthRemainingTime.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var shortenPaddleEntity : shortenPaddleEntities) {
            var shortenPaddle = shortenPaddleEntity.component(ShortenPaddleBonus.class);
            for (var paddleEntity : paddleEntities) {
                var rectangle = paddleEntity.component(Rectangle.class);
                var minHalfWidth = paddleEntity.component(MinHalfWidth.class);
                var resetWidthRemainingTime = paddleEntity.component(
                    ResetWidthRemainingTime.class
                );
                resetWidthRemainingTime.seconds += shortenPaddle.addedTimeSeconds();
                registryEdit.addComponents(
                    paddleEntity.id(),
                    new UpdateWidthCommand(
                        Math.max(
                            rectangle.halfWidth - shortenPaddle.subtractedHalfWidth(),
                            minHalfWidth.value()
                        )
                    )
                );
            }
        }
    }
}
