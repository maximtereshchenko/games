package com.github.maximtereshchenko.games.cookies.screen.view.cookie;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

final class StaticFallingCookiesWidget extends Widget {

    private final Style style;
    private float offsetPercentage;

    StaticFallingCookiesWidget(Skin skin) {
        this.style = skin.get(Style.class);
        style.drawable.setRegion(style.drawable.getRegion());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        var offset = style.drawable.getMinHeight() * offsetPercentage;
        style.drawable.draw(
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

    private static final class Style {

        TiledDrawable drawable;
    }
}
