package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class PopUpFrame extends Stack {

    public PopUpFrame(AssetManager assetManager, Assets assets, Table table) {
        var style = assetManager.get(assets.game().skin()).get(Style.class);
        for (var drawable : style.drawables) {
            add(new Image(drawable));
        }
        add(new Container<>(table).fill());
    }

    private static final class Style {

        Drawable[] drawables;
    }
}
