package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;

import java.util.Random;

final class CookiesView extends ScreenLayout {

    CookiesView(
        Skin skin,
        Random random,
        CookieService cookieService,
        EventBus<Event> eventBus
    ) {
        setBackground(skin.getTiledDrawable("tile_paper"));
        defaults().growY();
        add(new CookiePanel(skin, random, cookieService, eventBus))
            .width(Value.percentWidth(0.3f, this));
        addVerticalBeam(skin);
        add().growX();
        addVerticalBeam(skin);
        add(new StorePanel(skin));
    }

    private void addVerticalBeam(Skin skin) {
        add(new VerticalBeam(skin)).width(Value.prefWidth);
    }
}
