package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

class CookiesLabel extends Label {

    CookiesLabel(String text, Skin skin) {
        super(text, skin, "label_cookies");
        setAlignment(Align.center);
    }
}
