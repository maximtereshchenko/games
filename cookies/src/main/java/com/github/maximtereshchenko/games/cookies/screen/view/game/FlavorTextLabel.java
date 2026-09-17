package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

public class FlavorTextLabel extends Label {

    public FlavorTextLabel(AssetManager assetManager, Assets assets, String text) {
        super(text, assetManager.get(assets.game().skin()), "flavor-text");
        setAlignment(Align.right);
        setWrap(true);
    }
}
