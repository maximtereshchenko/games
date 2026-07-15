package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

final class InterfaceRenderingSystem implements System {

    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private final BitmapFont bitmapFont;
    private final Dominion dominion;

    InterfaceRenderingSystem(
        Viewport viewport,
        SpriteBatch spriteBatch,
        BitmapFont bitmapFont,
        Dominion dominion
    ) {
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
        this.bitmapFont = bitmapFont;
        this.dominion = dominion;
    }

    @Override
    public void run(float deltaTime) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        var bitmapFontData = bitmapFont.getData();
        var scaleX = bitmapFontData.scaleX;
        var scaleY = bitmapFontData.scaleY;
        bitmapFontData.setScale(viewport.getWorldHeight() * 0.1f / bitmapFont.getCapHeight());
        for (var foodEatenCounter : dominion.findCompositionsWith(FoodEatenCounter.class)) {
            var glyphLayout = new GlyphLayout(
                bitmapFont,
                String.valueOf(foodEatenCounter.value),
                Colors.FOOD_EATEN_COUNTER,
                0,
                Align.left,
                false
            );
            bitmapFont.draw(
                spriteBatch,
                glyphLayout,
                (viewport.getWorldWidth() - glyphLayout.width) / 2,
                viewport.getWorldHeight()
            );
        }
        bitmapFontData.setScale(scaleX, scaleY);
        spriteBatch.end();
    }
}
