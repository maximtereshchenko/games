package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;
import com.github.maximtereshchenko.games.cookies.domain.CookieService;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.screen.view.cookie.CookiePanel;
import com.github.maximtereshchenko.games.cookies.screen.view.generator.StorePanel;

import java.util.Random;

public final class CookiesView extends ScreenLayout {

    public CookiesView(
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
                bundle,
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
