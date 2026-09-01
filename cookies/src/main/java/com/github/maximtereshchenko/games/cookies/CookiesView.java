package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;

import java.util.Random;

final class CookiesView extends ScreenLayout {

    CookiesView(
        Skin skin,
        I18NBundle bundle,
        Random random,
        CookieService cookieService,
        EventBus<Event> eventBus
    ) {
        setBackground(skin.get(Style.class).background);
        defaults().growY();
        add(
            new CookiePanel(
                skin,
                random,
                cookieService,
                eventBus
            )
        )
            .width(Value.percentWidth(0.3f, this));
        addVerticalBeam(skin);
        add().growX();
        addVerticalBeam(skin);
        add(
            new StorePanel(
                skin,
                bundle,
                cookieService,
                eventBus
            )
        );
    }

    private void addVerticalBeam(Skin skin) {
        add(new Beam(skin, "image_view")).width(Value.prefWidth);
    }

    private static final class Style {

        Drawable background;
    }
}
