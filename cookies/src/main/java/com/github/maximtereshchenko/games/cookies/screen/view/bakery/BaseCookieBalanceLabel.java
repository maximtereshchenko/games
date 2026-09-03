package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

abstract class BaseCookieBalanceLabel extends Label {

    BaseCookieBalanceLabel(String text, Skin skin) {
        super(text, skin, "label_cookieBalance");
        setAlignment(Align.center);
    }
}
