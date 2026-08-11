package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;

final class WorldRenderingSystem implements System {

    private final Iterable<Entity> backgroundEntities;
    private final Iterable<Entity> foregroundEntities;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Mode mode;

    WorldRenderingSystem(
        Registry registry,
        Viewport viewport,
        ShapeRenderer shapeRenderer,
        Mode mode
    ) {
        this.backgroundEntities = registry.entities(
            new Query()
                .all(
                    PaletteColor.class,
                    Opacity.class,
                    WorldPosition.class,
                    Background.class
                )
        );
        this.foregroundEntities = registry.entities(
            new Query()
                .all(PaletteColor.class, Opacity.class, WorldPosition.class)
                .none(Background.class)
        );
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.mode = mode;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (var entity : backgroundEntities) {
            draw(
                entity.component(PaletteColor.class),
                entity.component(Opacity.class).value,
                entity.component(WorldPosition.class)
            );
        }
        for (var entity : foregroundEntities) {
            draw(
                entity.component(PaletteColor.class),
                entity.component(Opacity.class).value,
                entity.component(WorldPosition.class)
            );
        }
        shapeRenderer.end();
    }

    private void draw(PaletteColor paletteColor, float alpha, WorldPosition position) {
        var color = shapeRenderer.getColor();
        color.set(mode.palette().get(paletteColor));
        color.a = alpha;
        shapeRenderer.rect(position.x, position.y, 1, 1);
    }
}
