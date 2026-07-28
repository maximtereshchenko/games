package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.snakes.configuration.Mode;

final class WorldRenderingSystem implements System {

    private final Iterable<Entity> backgroundEntities;
    private final Iterable<Entity> foregroundEntities;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Mode mode;

    WorldRenderingSystem(
        World world,
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        Mode mode
    ) {
        this.backgroundEntities = world.entities(
            new Query().all(Colored.class, WorldPosition.class, Background.class)
        );
        this.foregroundEntities = world.entities(
            new Query().all(Colored.class, WorldPosition.class).none(Background.class)
        );
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.mode = mode;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (var entity : backgroundEntities) {
            draw(entity.component(Colored.class), entity.component(WorldPosition.class));
        }
        for (var entity : foregroundEntities) {
            draw(entity.component(Colored.class), entity.component(WorldPosition.class));
        }
        shapeRenderer.end();
    }

    private void draw(Colored colored, WorldPosition position) {
        shapeRenderer.setColor(mode.palette().get(colored));
        shapeRenderer.rect(position.x, position.y, 1, 1);
    }
}
