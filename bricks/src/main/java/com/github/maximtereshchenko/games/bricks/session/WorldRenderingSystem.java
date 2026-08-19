package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class WorldRenderingSystem implements System {

    private final Iterable<Entity> entities;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;

    WorldRenderingSystem(
        Registry registry,
        Viewport viewport,
        ShapeRenderer shapeRenderer
    ) {
        this.entities = registry.entities(
            new Query()
                .all(Color.class, WorldPosition.class)
                .one(
                    Rectangle.class,
                    Circle.class,
                    Star.class
                )
        );
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.getColor().set(Color.valueOf("#21004e")); //TODO
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
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
        }
        var circle = entity.component(Circle.class);
        if (circle != null) {
            draw(vector2, circle);
        }
        var star = entity.component(Star.class);
        if (star != null) {
            draw(vector2, star);
        }
    }

    private void draw(Vector2 vector2, Star star) {
        var x = new float[5];
        var y = new float[5];
        for (var i = 0; i < 5; i++) {
            var angle = Math.PI / 2 + (i * 2 * Math.PI / 5f);
            x[i] = (float) (vector2.x + star.radius() * Math.cos(angle));
            y[i] = (float) (vector2.y + star.radius() * Math.sin(angle));
        }
        for (var i = 0; i < 5; i++) {
            var p2 = (i + 2) % 5;
            var p3 = (i + 3) % 5;
            shapeRenderer.triangle(x[i], y[i], x[p2], y[p2], x[p3], y[p3]);
        }
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
