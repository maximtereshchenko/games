package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class InputSystem implements System {

    private final Iterable<Entity> entities;
    private final Viewport viewport;
    private final Vector2 vector2;

    InputSystem(Registry registry, Viewport viewport) {
        this.entities = registry.entities(
            new Query().all(Paddle.class, Velocity.class)
        );
        this.viewport = viewport;
        this.vector2 = new Vector2();
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        vector2.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(vector2);
        for (var entity : entities) {
            var worldPosition = entity.component(WorldPosition.class);
            var velocity = entity.component(Velocity.class);
            var target = Math.clamp(
                vector2.x,
                0,
                viewport.getWorldWidth()
            );
            velocity.vector2().x =
                (target - worldPosition.vector2().x) / deltaTimeSeconds;
        }
    }
}
