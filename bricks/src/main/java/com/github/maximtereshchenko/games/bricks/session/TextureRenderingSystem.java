package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class TextureRenderingSystem implements System {

    private final Iterable<Entity> entities;
    private final Configuration configuration;
    private final Viewport viewport;
    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;

    TextureRenderingSystem(
        Registry registry,
        Configuration configuration,
        Viewport viewport,
        AssetManager assetManager,
        SpriteBatch spriteBatch
    ) {
        this.entities = registry.entities(
            new Query()
                .all(Texture.class, WorldPosition.class)
                .one(
                    Rectangle.class,
                    Circle.class,
                    Star.class
                )
        );
        this.configuration = configuration;
        this.viewport = viewport;
        this.assetManager = assetManager;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        drawBackground();
        for (var entity : entities) {
            spriteBatch.setColor(tint(entity));
            draw(entity);
        }
        spriteBatch.end();
    }

    private void drawBackground() {
        spriteBatch.setColor(configuration.background());
        var world = configuration.world();
        spriteBatch.draw(
            assetManager.get(configuration.assets().textureAtlas())
                .findRegion("square"),
            0,
            0,
            world.width(),
            world.height()
        );
    }

    private Color tint(Entity entity) {
        var color = entity.component(Color.class);
        if (color == null) {
            return Color.WHITE;
        }
        return color;
    }

    private void draw(Entity entity) {
        var rectangle = entity.component(Rectangle.class);
        if (rectangle != null) {
            draw(entity, rectangle.halfWidth, rectangle.halfHeight);
            return;
        }
        var circle = entity.component(Circle.class);
        if (circle != null) {
            draw(entity, circle.radius(), circle.radius());
            return;
        }
        var star = entity.component(Star.class);
        draw(entity, star.radius(), star.radius());
    }

    private void draw(Entity entity, float halfWidth, float halfHeight) {
        var texture = entity.component(Texture.class);
        var worldPosition = entity.component(WorldPosition.class);
        var vector2 = worldPosition.vector2();
        spriteBatch.draw(
            assetManager.get(configuration.assets().textureAtlas())
                .findRegion(texture.name()),
            vector2.x - halfWidth,
            vector2.y - halfHeight,
            halfWidth * 2,
            halfHeight * 2
        );
    }
}
