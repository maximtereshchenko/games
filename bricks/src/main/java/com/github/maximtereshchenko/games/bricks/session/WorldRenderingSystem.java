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
                .all(Visible.class, WorldPosition.class)
                .one(Rectangle.class, Circle.class)
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
        shapeRenderer.getColor().set(Color.valueOf("#22004f"));
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        for (var entity : entities) {
            var visible = entity.component(Visible.class);
            var worldPosition = entity.component(WorldPosition.class);
            shapeRenderer.getColor().set(visible.color());
            draw(entity, worldPosition.vector2());
        }
        shapeRenderer.end();
    }

    private void draw(Entity entity, Vector2 vector2) {
        var rectangle = entity.component(Rectangle.class);
        if (rectangle == null) {
            var circle = entity.component(Circle.class);
            shapeRenderer.circle(
                vector2.x,
                vector2.y,
                circle.radius(),
                16
            );
        } else {
            shapeRenderer.rect(
                vector2.x - rectangle.width / 2,
                vector2.y - rectangle.height / 2,
                rectangle.width,
                rectangle.height
            );
        }
    }
}
