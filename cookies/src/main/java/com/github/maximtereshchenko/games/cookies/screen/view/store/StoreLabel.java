package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

final class StoreLabel extends Label {

    StoreLabel(Skin skin, I18NBundle bundle) {
        super(bundle.get("store"), skin, "label_store");
        setAlignment(Align.center);
    }
}
