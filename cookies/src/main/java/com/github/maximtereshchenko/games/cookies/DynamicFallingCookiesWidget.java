package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import java.util.Random;

final class DynamicFallingCookiesWidget extends WidgetGroup {

    private final Skin skin;
    private final Random random;

    DynamicFallingCookiesWidget(Skin skin, Random random) {
        this.skin = skin;
        this.random = random;
        setLayoutEnabled(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (random.nextFloat() > 0.1f) {
            return;
        }
        addActor(new CookieIcon(skin, random, getWidth(), getHeight()));
    }
}
