package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;

import java.util.List;
import java.util.Map;

public final class BricksBlueprints {

    static final String PADDLE = "paddle";
    static final String BALL = "ball";
    static final String BRICK = "brick";
    static final String WALL = "wall";
    static final String BARRIER = "barrier";
    static final String INCREMENT_STARS_BONUS = "incrementStarsBonus";

    public Blueprints blueprints(Map<String, List<Object>> components) {
        return new Blueprints.Builder(components)
            .copyFunction(
                Rectangle.class,
                original -> new Rectangle(
                    original.halfWidth,
                    original.halfHeight
                )
            )
            .copyFunction(
                ResetWidthRemainingTime.class,
                original -> new ResetWidthRemainingTime(
                    original.seconds
                )
            )
            .copyFunction(
                Velocity.class,
                original -> new Velocity(
                    new Vector2(original.vector2())
                )
            )
            .copyFunction(
                SpawnedStars.class,
                original -> new SpawnedStars(
                    original.accumulated
                )
            )
            .copyFunction(
                CollectedStars.class,
                original -> new CollectedStars(original.value)
            )
            .copyFunction(
                Lives.class,
                original -> new Lives(original.value)
            )
            .copyFunction(
                WorldPosition.class,
                original -> new WorldPosition(new Vector2(original.vector2()))
            )
            .build();
    }
}
