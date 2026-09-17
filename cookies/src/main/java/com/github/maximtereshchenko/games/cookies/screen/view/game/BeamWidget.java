package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

public class BeamWidget extends Stack {

    public BeamWidget(AssetManager assetManager, Assets assets, String styleName) {
        var style = assetManager.get(assets.game().skin()).get(styleName, Style.class);
        var container = new Container<>();
        container.background(style.background);
        add(container);
        for (var drawable : style.drawables) {
            add(new Image(drawable));
        }
    }

    private static final class Style {

        Drawable background;
        Drawable[] drawables;
    }
}
