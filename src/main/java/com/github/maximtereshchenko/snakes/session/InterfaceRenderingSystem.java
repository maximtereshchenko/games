package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;
import com.github.maximtereshchenko.snakes.configuration.Mode;

final class InterfaceRenderingSystem implements System {

    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private final BitmapFont bitmapFont;
    private final Iterable<Entity> foodEatenCounterEntities;
    private final Iterable<Entity> airCounterEntities;
    private final Mode mode;

    InterfaceRenderingSystem(
        Viewport viewport,
        SpriteBatch spriteBatch,
        BitmapFont bitmapFont,
        World world,
        Mode mode
    ) {
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.bitmapFont = bitmapFont;
        this.foodEatenCounterEntities = world.entities(
            new Query().all(Colored.class, FoodEatenCounter.class)
        );
        this.airCounterEntities = world.entities(
            new Query().all(Colored.class, AirCounter.class)
        );
        this.mode = mode;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        scaled(0.05f, this::drawAirCounter);
        scaled(0.1f, this::drawFoodEatenCounter);
        spriteBatch.end();
    }

    private void scaled(float scale, Runnable runnable) {
        var bitmapFontData = bitmapFont.getData();
        var scaleX = bitmapFontData.scaleX;
        var scaleY = bitmapFontData.scaleY;
        bitmapFontData.setScale(
            viewport.getWorldHeight() * scale / bitmapFont.getCapHeight()
        );
        runnable.run();
        bitmapFontData.setScale(scaleX, scaleY);
    }

    private void drawFoodEatenCounter() {
        for (var entity : foodEatenCounterEntities) {
            var glyphLayout = glyphLayout(
                String.valueOf(entity.component(FoodEatenCounter.class).value),
                entity.component(Colored.class)
            );
            bitmapFont.draw(
                spriteBatch,
                glyphLayout,
                (viewport.getWorldWidth() - glyphLayout.width) / 2,
                viewport.getWorldHeight() - 35
            );
        }
    }

    private void drawAirCounter() {
        for (var entity : airCounterEntities) {
            bitmapFont.draw(
                spriteBatch,
                glyphLayout(
                    "AIR: " + entity.component(AirCounter.class).value,
                    entity.component(Colored.class)
                ),
                45,
                viewport.getWorldHeight() - 40
            );
        }
    }

    private GlyphLayout glyphLayout(String text, Colored colored) {
        return new GlyphLayout(
            bitmapFont,
            text,
            mode.palette().get(colored),
            0,
            Align.left,
            false
        );
    }
}
