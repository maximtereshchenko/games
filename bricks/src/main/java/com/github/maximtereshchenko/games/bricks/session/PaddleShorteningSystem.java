package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class PaddleShorteningSystem extends WidthResizingSystem {

    private final Iterable<Entity> shortenPaddleEntities;
    private final Iterable<Entity> paddleEntities;

    PaddleShorteningSystem(Registry registry) {
        this.shortenPaddleEntities = registry.entities(
            new Query().all(ShortenPaddle.class, Activated.class)
        );
        this.paddleEntities = registry.entities(
            new Query()
                .all(
                    Paddle.class,
                    Rectangle.class,
                    MinWidth.class,
                    ResetWidthRemainingTime.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var shortenPaddleEntity : shortenPaddleEntities) {
            var shortenPaddle = shortenPaddleEntity.component(ShortenPaddle.class);
            for (var paddleEntity : paddleEntities) {
                var rectangle = paddleEntity.component(Rectangle.class);
                var minWidth = paddleEntity.component(MinWidth.class);
                var resetWidthRemainingTime = paddleEntity.component(
                    ResetWidthRemainingTime.class
                );
                resetWidthRemainingTime.seconds += shortenPaddle.extraTimeSeconds();
                resize(
                    registryEdit,
                    paddleEntity,
                    rectangle,
                    Math.max(
                        rectangle.halfWidth * 2 - shortenPaddle.subtractedWidth(),
                        minWidth.value()
                    )
                );
            }
        }
    }
}
