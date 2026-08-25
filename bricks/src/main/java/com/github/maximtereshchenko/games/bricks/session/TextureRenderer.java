package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;

public final class TextureRenderer {

    private final Iterable<Entity> entities;
    private final Configuration configuration;
    private final AssetManager assetManager;

    public TextureRenderer(
        Registry registry,
        Configuration configuration,
        AssetManager assetManager
    ) {
        this.entities = registry.view(
            new Query()
                .all(Texture.class, WorldPosition.class)
                .one(
                    Rectangle.class,
                    Circle.class,
                    Star.class
                )
        );
        this.configuration = configuration;
        this.assetManager = assetManager;
    }

    public void draw(Batch batch) {
        drawBackground(batch);
        for (var entity : entities) {
            batch.setColor(tint(entity));
            draw(batch, entity);
        }
    }

    private void drawBackground(Batch batch) {
        var session = configuration.background().session();
        batch.setColor(session.color());
        var dimensions = configuration.worldDimensions();
        batch.draw(
            assetManager.get(configuration.assets().textureAtlas())
                .findRegion(session.texture()),
            0,
            0,
            dimensions.width(),
            dimensions.height()
        );
    }

    private Color tint(Entity entity) {
        var color = entity.component(Color.class);
        if (color == null) {
            return Color.WHITE;
        }
        return color;
    }

    private void draw(Batch batch, Entity entity) {
        var rectangle = entity.component(Rectangle.class);
        if (rectangle != null) {
            draw(
                batch,
                entity,
                rectangle.halfWidth,
                rectangle.halfHeight
            );
            return;
        }
        var circle = entity.component(Circle.class);
        if (circle != null) {
            draw(
                batch,
                entity,
                circle.radius(),
                circle.radius()
            );
            return;
        }
        var star = entity.component(Star.class);
        draw(batch, entity, star.radius(), star.radius());
    }

    private void draw(
        Batch batch,
        Entity entity,
        float halfWidth,
        float halfHeight
    ) {
        var texture = entity.component(Texture.class);
        var worldPosition = entity.component(WorldPosition.class);
        var vector2 = worldPosition.vector2();
        batch.draw(
            assetManager.get(configuration.assets().textureAtlas())
                .findRegion(texture.name()),
            vector2.x - halfWidth,
            vector2.y - halfHeight,
            halfWidth * 2,
            halfHeight * 2
        );
    }
}
