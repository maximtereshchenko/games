package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

class FlavorTextLabel extends Label {

    FlavorTextLabel(Skin skin, String text) {
        super(text, skin, "flavor-text");
    }
}
