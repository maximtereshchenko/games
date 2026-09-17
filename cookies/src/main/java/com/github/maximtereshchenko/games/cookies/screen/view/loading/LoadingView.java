package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class LoadingView extends Table {

    public LoadingView(AssetManager assetManager, Assets assets) {
        setBackground(assetManager.get(assets.loading().skin()).get(Style.class).background);
        add(new LoadingPanel(assetManager, assets)).grow();
    }

    private static final class Style {

        Drawable background;
    }
}
