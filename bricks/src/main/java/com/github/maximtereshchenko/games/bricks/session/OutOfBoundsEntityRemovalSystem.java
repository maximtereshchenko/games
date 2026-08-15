package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class OutOfBoundsEntityRemovalSystem implements System {

    private final Iterable<Entity> entities;
    private final Viewport viewport;

    OutOfBoundsEntityRemovalSystem(Registry registry, Viewport viewport) {
        this.entities = registry.entities(
            new Query().all(WorldPosition.class)
        );
        this.viewport = viewport;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var worldPosition = entity.component(WorldPosition.class);
            var vector2 = worldPosition.vector2();
            if (
                isOutOfBounds(vector2.x, viewport.getWorldWidth()) ||
                isOutOfBounds(vector2.y, viewport.getWorldHeight())
            ) {
                registryEdit.addComponents(entity.id(), Removed.INSTANCE);
            }
        }
    }

    private boolean isOutOfBounds(float coordinate, float length) {
        var offset = length * 0.05f;
        return coordinate < -offset || coordinate > length + offset;
    }
}
