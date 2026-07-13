package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.dominion.ecs.api.Dominion;

import java.util.Map;

final class InputSystem implements Runnable {

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
    public void run() {
        for (var entry : directions.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                for (var nextDirection : dominion.findCompositionsWith(NextDirection.class)) {
                    nextDirection.value = entry.getValue();
                }
            }
        }
    }
}
