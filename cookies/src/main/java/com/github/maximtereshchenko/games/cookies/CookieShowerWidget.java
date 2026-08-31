package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class CookieShowerWidget extends Widget {

    private final Drawable drawable;
    private float offsetPercentage;

    CookieShowerWidget(Skin skin) {
        this.drawable = skin.getTiledDrawable("tile_cookies_0");
        addAction(
            new Action() {

                @Override
                public boolean act(float delta) {
                    offsetPercentage += delta / 10;
                    if (offsetPercentage > 1) {
                        offsetPercentage -= 1;
                    }
                    return false;
                }
            }
        );
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
}
