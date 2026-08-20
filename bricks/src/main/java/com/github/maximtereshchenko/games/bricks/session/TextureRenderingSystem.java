package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (var entity : entities) {
            var texture = entity.component(Texture.class);
            var worldPosition = entity.component(WorldPosition.class);
            var radius = radius(entity);
            var vector2 = worldPosition.vector2();
            spriteBatch.draw(
                assetManager.get(configuration.assets().textureAtlas())
                    .findRegion(texture.name()),
                vector2.x - radius,
                vector2.y - radius,
                radius * 2,
                radius * 2
            );
        }
        spriteBatch.end();
    }

    private float radius(Entity entity) {
        var circle = entity.component(Circle.class);
        if (circle != null) {
            return circle.radius();
        }
        var star = entity.component(Star.class);
        return star.radius();
    }
}
