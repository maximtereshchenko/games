package com.github.maximtereshchenko.games.snakes.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;

public final class TitleView extends ScreenLayout {

    public TitleView(I18NBundle bundle, Skin skin) {
        var label = new Label(bundle.get("screens.title.text"), skin);
        label.setAlignment(Align.center);
        add(label);
    }
}
