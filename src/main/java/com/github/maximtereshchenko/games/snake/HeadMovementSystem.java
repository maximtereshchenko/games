package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;

import java.awt.Point;

final class HeadMovementSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final FitViewport fitViewport;

    HeadMovementSystem(Dominion dominion, FitViewport fitViewport) {
        super(dominion);
        this.dominion = dominion;
        this.fitViewport = fitViewport;
    }

    @Override
    void onTurnStarted() {
        for (var result : dominion.findEntitiesWith(Head.class, Point.class, Color.class)) {
            var currentHead = result.entity();
            var head = result.comp1();
            currentHead.removeType(Head.class);
            currentHead.removeType(Color.class);
            currentHead.add(
                    new Next(
                        dominion.createEntity(
                            new Head(head.next),
                            nextHeadPoint(result.comp2(), head.next),
                            new Previous(currentHead),
                            result.comp3()
                        )
                    )
                )
                .add(Colors.SEGMENT);
        }
    }

    private Point nextHeadPoint(Point point, Head.Direction direction) {
        var nextHeadPoint = new Point(point);
        var yMax = (int) fitViewport.getWorldHeight();
        var xMax = (int) fitViewport.getWorldWidth();
        switch (direction) {
            case Head.Direction.UP -> nextHeadPoint.y = adjusted(nextHeadPoint.y + 1, yMax);
            case Head.Direction.DOWN -> nextHeadPoint.y = adjusted(nextHeadPoint.y - 1, yMax);
            case Head.Direction.LEFT -> nextHeadPoint.x = adjusted(nextHeadPoint.x - 1, xMax);
            case Head.Direction.RIGHT -> nextHeadPoint.x = adjusted(nextHeadPoint.x + 1, xMax);
        }
        return nextHeadPoint;
    }

    private int adjusted(int value, int max) {
        return (value + max) % max;
    }
}
