package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.CookiesBaked;
import com.github.maximtereshchenko.games.cookies.domain.Event;

import java.util.Random;

final class FallingCookiesWidget extends WidgetGroup implements Subscriber<Event> {

    private final Skin skin;
    private final Style style;
    private final Random random;
    private float offsetPercentage;

    FallingCookiesWidget(
        Skin skin,
        Random random,
        EventBus<Event> eventBus
    ) {
        this.skin = skin;
        this.style = skin.get(Style.class);
        this.random = random;
        style.drawable.setRegion(style.drawable.getRegion());
        setLayoutEnabled(false);
        eventBus.subscribe(this);
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

    @Override
    public void onEvent(Event event) {
        if (event instanceof CookiesBaked) {
            addActor(new FallingCookieWidget(skin, random, getWidth(), getHeight()));
        }
    }

    private static final class Style {

        TiledDrawable drawable;
    }
}
