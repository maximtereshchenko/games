package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;

import java.awt.*;

final class HeadMovementSystem implements Runnable {

    private final Dominion dominion;
    private final FitViewport fitViewport;

    HeadMovementSystem(Dominion dominion, FitViewport fitViewport) {
        this.dominion = dominion;
        this.fitViewport = fitViewport;
    }

    @Override
    public void run() {
        if (!dominion.findEntitiesWith(TurnStarted.class).iterator().hasNext()) {
            return;
        }
        for (var result : dominion.findEntitiesWith(HeadDirection.class, Point.class)) {
            var point = result.comp2();
            var yMax = (int) fitViewport.getWorldHeight();
            var xMax = (int) fitViewport.getWorldWidth();
            switch (result.comp1()) {
                case UP -> point.y = adjusted(point.y + 1, yMax);
                case DOWN -> point.y = adjusted(point.y - 1, yMax);
                case LEFT -> point.x = adjusted(point.x - 1, xMax);
                case RIGHT -> point.x = adjusted(point.x + 1, xMax);
            }
        }
    }

    private int adjusted(int value, int max) {
        return (value + max) % max;
    }
}
