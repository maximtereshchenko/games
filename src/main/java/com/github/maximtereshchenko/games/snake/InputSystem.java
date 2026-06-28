package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.dominion.ecs.api.Dominion;

import java.util.Map;

final class InputSystem implements Runnable {

    private final Dominion dominion;
    private final Map<Integer, Head.Direction> directions;

    InputSystem(Dominion dominion) {
        this.dominion = dominion;
        this.directions = Map.of(
            Input.Keys.W, Head.Direction.UP,
            Input.Keys.S, Head.Direction.DOWN,
            Input.Keys.A, Head.Direction.LEFT,
            Input.Keys.D, Head.Direction.RIGHT
        );
    }

    @Override
    public void run() {
        for (var entry : directions.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                for (var head : dominion.findCompositionsWith(Head.class)) {
                    var direction = entry.getValue();
                    if (head.current.opposite() != direction) {
                        head.next = direction;
                    }
                }
            }
        }
    }
}
