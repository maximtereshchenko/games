package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.common.event.EventBus;

final class CookieAmountPanel extends Table {

    CookieAmountPanel(Skin skin, EventBus<Event> eventBus) {
        background(skin.get(Style.class).background);
        var cookiesPerSecond = new Label(
            "per second: 123",
            skin,
            "label_cookiesPerSecond"
        );
        cookiesPerSecond.setAlignment(Align.center);
        defaults().pad(4).growX();
        add(new CookieAmountLabel(skin, eventBus)).row();
        add(new CookiesLabel("cookies", skin)).row();
        add(cookiesPerSecond).row();
    }

    private static final class Style {

        Drawable background;
    }
}
