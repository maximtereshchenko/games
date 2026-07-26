package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.dominion.ecs.api.Dominion;

import java.util.Map;

final class InputSystem implements System {

    private final Dominion dominion;
    private final Map<Integer, Direction> directions;

    InputSystem(Dominion dominion) {
        this.dominion = dominion;
        this.directions = Map.of(
            Input.Keys.W, Direction.UP,
            Input.Keys.S, Direction.DOWN,
            Input.Keys.A, Direction.LEFT,
            Input.Keys.D, Direction.RIGHT
        );
    }

    @Override
    public void run(float deltaTime) {
        for (var entry : directions.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                for (var nextDirection : dominion.findCompositionsWith(NextForwardDirection.class)) {
                    nextDirection.value = entry.getValue();
                }
            }
        }
    }
}
