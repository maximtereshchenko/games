package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.snakes.configuration.Mode;

final class GameRenderingSystem implements System {

    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Iterable<Entity> backgroundEntities;
    private final Iterable<Entity> foregroundEntities;
    private final Mode mode;

    GameRenderingSystem(
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        World world,
        Mode mode
    ) {
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.backgroundEntities = world.entities(
            new Query().all(Colored.class, Position.class, Background.class)
        );
        this.foregroundEntities = world.entities(
            new Query().all(Colored.class, Position.class).none(Background.class)
        );
        this.mode = mode;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (var entity : backgroundEntities) {
            draw(entity.component(Colored.class), entity.component(Position.class));
        }
        for (var entity : foregroundEntities) {
            draw(entity.component(Colored.class), entity.component(Position.class));
        }
        shapeRenderer.end();
    }

    private void draw(Colored colored, Position position) {
        shapeRenderer.setColor(mode.palette().get(colored));
        shapeRenderer.rect(position.x, position.y, 1, 1);
    }
}
