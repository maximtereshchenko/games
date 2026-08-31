package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class StaticFallingCookiesWidget extends Widget {

    private final Drawable drawable;
    private float offsetPercentage;

    StaticFallingCookiesWidget(Skin skin) {
        this.drawable = skin.getTiledDrawable("tile_cookies_0");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        var offset = drawable.getMinHeight() * offsetPercentage;
        drawable.draw(
            batch,
            getX(),
            getY() - offset,
            getWidth(),
            getHeight() + offset
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        offsetPercentage += delta / 10;
        offsetPercentage %= 1;
    }
}
