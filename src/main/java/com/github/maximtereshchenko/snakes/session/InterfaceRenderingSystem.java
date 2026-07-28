package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.snakes.configuration.Mode;

final class InterfaceRenderingSystem implements System {

    private final Iterable<Entity> interfaceEntities;
    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private final BitmapFont bitmapFont;
    private final Mode mode;

    InterfaceRenderingSystem(
        World world,
        Viewport viewport,
        SpriteBatch spriteBatch,
        BitmapFont bitmapFont,
        Mode mode
    ) {
        this.interfaceEntities = world.entities(
            new Query().all(InterfaceText.class, InterfacePosition.class, Colored.class)
        );
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.bitmapFont = bitmapFont;
        this.mode = mode;

    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (var interfaceEntity : interfaceEntities) {
            var interfaceText = interfaceEntity.component(InterfaceText.class);
            var interfacePosition = interfaceEntity.component(InterfacePosition.class);
            var bitmapFontData = bitmapFont.getData();
            var scaleX = bitmapFontData.scaleX;
            var scaleY = bitmapFontData.scaleY;
            bitmapFontData.setScale(interfaceText.scale);
            bitmapFont.setColor(
                mode.palette()
                    .get(interfaceEntity.component(Colored.class))
            );
            bitmapFont.draw(
                spriteBatch,
                interfaceText.value,
                interfacePosition.x,
                interfacePosition.y
            );
            bitmapFontData.setScale(scaleX, scaleY);
        }
        spriteBatch.end();
    }
}
