package com.github.maximtereshchenko.snakes.session;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.function.Consumer;

final class ScaledFont {

    private final BitmapFont bitmapFont;

    ScaledFont(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
    }

    void use(int scale, Consumer<BitmapFont> consumer) {
        var bitmapFontData = bitmapFont.getData();
        var scaleX = bitmapFontData.scaleX;
        var scaleY = bitmapFontData.scaleY;
        bitmapFontData.setScale(scale);
        consumer.accept(bitmapFont);
        bitmapFontData.setScale(scaleX, scaleY);
    }
}
