package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class WidthResettingSystem extends WidthResizingSystem {

    private final Iterable<Entity> entities;

    WidthResettingSystem(Registry registry) {
        this.entities = registry.entities(
            new Query()
                .all(
                    ResetWidthRemainingTime.class,
                    BaseWidth.class,
                    Rectangle.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var resetWidthRemainingTime = entity.component(
                ResetWidthRemainingTime.class
            );
            var baseWidth = entity.component(BaseWidth.class);
            var rectangle = entity.component(Rectangle.class);
            resetWidthRemainingTime.seconds = Math.max(
                resetWidthRemainingTime.seconds - deltaTimeSeconds,
                0
            );
            if (resetWidthRemainingTime.seconds == 0) {
                resize(
                    registryEdit,
                    entity,
                    rectangle,
                    baseWidth.value()
                );
            }
        }
    }
}
