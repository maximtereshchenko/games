package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class OutOfBoundsEntityRemovalSystem implements System {

    private final Iterable<Entity> entities;
    private final Configuration configuration;

    OutOfBoundsEntityRemovalSystem(
        Registry registry,
        Configuration configuration
    ) {
        this.entities = registry.entities(
            new Query().all(WorldPosition.class)
        );
        this.configuration = configuration;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var worldPosition = entity.component(WorldPosition.class);
            var vector2 = worldPosition.vector2();
            var dimensions = configuration.worldDimensions();
            if (
                isOutOfBounds(vector2.x, dimensions.width()) ||
                isOutOfBounds(vector2.y, dimensions.height())
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
