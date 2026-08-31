package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;

import java.util.concurrent.ThreadLocalRandom;

final class CookiesView extends ScreenLayout {

    CookiesView(Skin skin) {
        setBackground(skin.getTiledDrawable("tile_paper"));
        defaults().growY();
        add(new CookiePanel(skin, ThreadLocalRandom.current()))
            .width(Value.percentWidth(0.3f, this));
        add(new VerticalSeparator(skin)).width(Value.prefWidth);
        add().growX();
        add(new VerticalSeparator(skin)).width(Value.prefWidth);
        add(new StorePanel(skin));
    }
}
