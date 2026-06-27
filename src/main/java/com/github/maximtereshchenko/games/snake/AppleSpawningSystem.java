package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;

import java.awt.Point;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

final class AppleSpawningSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final Random random;
    private final FitViewport fitViewport;
    private final int maxApples;

    AppleSpawningSystem(
        Dominion dominion,
        Random random,
        FitViewport fitViewport,
        int maxApples
    ) {
        super(dominion);
        this.dominion = dominion;
        this.random = random;
        this.fitViewport = fitViewport;
        this.maxApples = maxApples;
    }

    @Override
    void onTurnStarted() {
        var points = dominion.findCompositionsWith(Point.class).stream().collect(Collectors.toSet());
        for (var i = currentApples(); enoughSpace(points) && i < maxApples; i++) {
            var point = point(points);
            points.add(point);
            dominion.createEntity(
                Apple.INSTANCE,
                point,
                Colors.APPLE
            );
        }
    }

    private boolean enoughSpace(Set<Point> points) {
        return points.size() < fitViewport.getWorldHeight() * fitViewport.getWorldWidth();
    }

    private Point point(Set<Point> points) {
        var point = new Point();
        do {
            point.x = random.nextInt((int) fitViewport.getWorldWidth());
            point.y = random.nextInt((int) fitViewport.getWorldHeight());
        } while (points.contains(point));
        return point;
    }

    private long currentApples() {
        return dominion.findEntitiesWith(Apple.class).stream().count();
    }
}
