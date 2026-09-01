package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

class CookieLabel extends Label {

    CookieLabel(String text, Skin skin) {
        super(text, skin, "label_cookies");
        setAlignment(Align.center);
    }
}
