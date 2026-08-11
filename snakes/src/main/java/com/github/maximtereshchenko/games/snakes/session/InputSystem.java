package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.Map;

final class InputSystem implements System {

    private static final Map<Integer, Direction> DIRECTIONS = Map.of(
        Input.Keys.W, Direction.UP,
        Input.Keys.S, Direction.DOWN,
        Input.Keys.A, Direction.LEFT,
        Input.Keys.D, Direction.RIGHT
    );

    private final Iterable<Entity> directionIntentEntities;

    InputSystem(World world) {
        this.directionIntentEntities = world.entities(
            new Query().all(DirectionIntent.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        for (var entity : directionIntentEntities) {
            for (var entry : DIRECTIONS.entrySet()) {
                if (Gdx.input.isKeyPressed(entry.getKey())) {
                    entity.component(DirectionIntent.class).value = entry.getValue();
                }
            }
        }
    }
}
