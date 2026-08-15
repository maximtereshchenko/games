package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallResettingSystem implements System {

    private final Iterable<Entity> ballEntities;
    private final Iterable<Entity> paddleEntities;

    BallResettingSystem(Registry registry) {
        this.ballEntities = registry.entities(
            new Query().all(Ball.class)
        );
        this.paddleEntities = registry.entities(
            new Query()
                .all(
                    Paddle.class,
                    WorldPosition.class,
                    Rectangle.class
                )
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (ballEntities.iterator().hasNext()) {
            return;
        }
        for (var paddleEntity : paddleEntities) {
            var worldPosition = paddleEntity.component(WorldPosition.class);
            var rectangle = paddleEntity.component(Rectangle.class);
            var vector2 = worldPosition.vector2();
            var ballCircle = new Circle(0.1f);
            registryEdit.addComponents(
                registryEdit.createEntity(),
                Ball.INSTANCE,
                BodyDef.BodyType.DynamicBody,
                new CollisionGroupIndex(-1),
                Attaching.INSTANCE,
                ballCircle,
                new WorldPosition(
                    new Vector2(
                        vector2.x,
                        vector2.y +
                        (rectangle.halfHeight + ballCircle.radius()) * 1.1f
                    )
                ),
                new Velocity(new Vector2()),
                new Speed(5),
                new Visible(Color.valueOf("#feffff"))
            );
        }
    }
}
