package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FlavorTextLabel extends Label {

    public FlavorTextLabel(Skin skin, String text) {
        super(text, skin, "flavor-text");
    }
}
