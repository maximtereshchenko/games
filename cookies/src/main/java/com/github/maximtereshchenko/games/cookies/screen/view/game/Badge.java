package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

public class Badge extends Label {

    public Badge(AssetManager assetManager, Assets assets, String text) {
        super(text, assetManager.get(assets.game().skin()), "badge");
    }
}
