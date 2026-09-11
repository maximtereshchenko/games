package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;

public final class LoadingView extends ScreenLayout {

    public LoadingView(Skin skin, I18NBundle bundle) {
        setBackground(skin.get(Style.class).background);
        add(new LoadingPanel(skin, bundle)).grow();
    }

    private static final class Style {

        Drawable background;
    }
}
