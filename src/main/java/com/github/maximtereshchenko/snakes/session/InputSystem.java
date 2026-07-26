package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

import java.util.Map;

final class InputSystem implements System {

    private final Iterable<Entity> nextForwardDirectionEntities;
    private final Map<Integer, Direction> directions;

    InputSystem(World world) {
        this.nextForwardDirectionEntities = world.entities(
            new Query().all(NextForwardDirection.class)
        );
        this.directions = Map.of(
            Input.Keys.W, Direction.UP,
            Input.Keys.S, Direction.DOWN,
            Input.Keys.A, Direction.LEFT,
            Input.Keys.D, Direction.RIGHT
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var entry : directions.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                for (var entity : nextForwardDirectionEntities) {
                    entity.component(NextForwardDirection.class).value = entry.getValue();
                }
            }
        }
    }
}
