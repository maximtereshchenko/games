package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import java.util.Random;

final class FallingCookiesWidget extends WidgetGroup {

    private final Skin skin;
    private final Style style;
    private final Random random;
    private float offsetPercentage;

    FallingCookiesWidget(
        Skin skin,
        Random random
    ) {
        this.skin = skin;
        this.style = skin.get(Style.class);
        this.random = random;
        style.drawable.setRegion(style.drawable.getRegion());
        setLayoutEnabled(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var offset = style.drawable.getMinHeight() * offsetPercentage;
        style.drawable.draw(
            batch,
            getX(),
            getY() - offset,
            getWidth(),
            getHeight() + offset
        );
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        offsetPercentage += delta / 10;
        offsetPercentage %= 1;
    }

    void addFallingCookieWidget() {
        addActor(new FallingCookieWidget(skin, random, getWidth(), getHeight()));
    }

    private static final class Style {

        TiledDrawable drawable;
    }
}
