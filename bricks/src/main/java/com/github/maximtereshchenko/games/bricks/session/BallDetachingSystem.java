package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallDetachingSystem implements System {

    private final Iterable<Entity> entities;

    BallDetachingSystem(Registry registry) {
        this.entities = registry.entities(
            new Query()
                .all(Ball.class, Attached.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            for (var entity : entities) {
                registryEdit.removeComponents(entity.id(), Attached.class);
            }
        }
    }
}
