package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class ShapeRenderingSystem implements System {

    private final Iterable<Entity> entities;
    private final Configuration configuration;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;

    ShapeRenderingSystem(
        Registry registry,
        Configuration configuration,
        Viewport viewport,
        ShapeRenderer shapeRenderer
    ) {
        this.entities = registry.entities(
            new Query()
                .all(Color.class, WorldPosition.class)
                .one(
                    Rectangle.class,
                    Circle.class
                )
        );
        this.configuration = configuration;
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.getColor().set(configuration.background());
        var world = configuration.world();
        shapeRenderer.rect(0, 0, world.width(), world.height());
        for (var entity : entities) {
            var color = entity.component(Color.class);
            var worldPosition = entity.component(WorldPosition.class);
            shapeRenderer.getColor().set(color);
            draw(entity, worldPosition.vector2());
        }
        shapeRenderer.end();
    }

    private void draw(Entity entity, Vector2 vector2) {
        var rectangle = entity.component(Rectangle.class);
        if (rectangle != null) {
            draw(vector2, rectangle);
            return;
        }
        var circle = entity.component(Circle.class);
        draw(vector2, circle);
    }

    private void draw(Vector2 vector2, Circle circle) {
        shapeRenderer.circle(
            vector2.x,
            vector2.y,
            circle.radius(),
            16
        );
    }

    private void draw(Vector2 vector2, Rectangle rectangle) {
        shapeRenderer.rect(
            vector2.x - rectangle.halfWidth,
            vector2.y - rectangle.halfHeight,
            rectangle.halfWidth * 2,
            rectangle.halfHeight * 2
        );
    }
}
