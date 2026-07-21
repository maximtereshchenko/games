package com.github.maximtereshchenko.games.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.snakes.Mode;
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
        var bitmapFontData = bitmapFont.getData();
        var scaleX = bitmapFontData.scaleX;
        var scaleY = bitmapFontData.scaleY;
        bitmapFontData.setScale(viewport.getWorldHeight() * 0.1f / bitmapFont.getCapHeight());
        for (var result : dominion.findEntitiesWith(Colored.class, FoodEatenCounter.class)) {
            var glyphLayout = new GlyphLayout(
                bitmapFont,
                String.valueOf(result.comp2().value),
                mode.palette().get(result.comp1()),
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
