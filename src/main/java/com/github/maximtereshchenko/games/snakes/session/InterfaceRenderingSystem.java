package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;

final class InterfaceRenderingSystem implements System {

    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private final BitmapFont bitmapFont;
    private final Dominion dominion;
    private final Mode mode;

    InterfaceRenderingSystem(
        Viewport viewport,
        SpriteBatch spriteBatch,
        BitmapFont bitmapFont,
        Dominion dominion,
        Mode mode
    ) {
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.bitmapFont = bitmapFont;
        this.dominion = dominion;
        this.mode = mode;
    }

    @Override
    public void run(float deltaTime) {
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
        bitmapFontData.setScale(viewport.getWorldHeight() * scale / bitmapFont.getCapHeight());
        runnable.run();
        bitmapFontData.setScale(scaleX, scaleY);
    }

    private void drawFoodEatenCounter() {
        for (var result : dominion.findEntitiesWith(Colored.class, FoodEatenCounter.class)) {
            var glyphLayout = glyphLayout(
                String.valueOf(result.comp2().value),
                result.comp1()
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
        for (var result : dominion.findEntitiesWith(Colored.class, AirCounter.class)) {
            bitmapFont.draw(
                spriteBatch,
                glyphLayout(
                    "AIR: " + result.comp2().value,
                    result.comp1()
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
