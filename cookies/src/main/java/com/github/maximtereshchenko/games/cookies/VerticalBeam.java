package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class VerticalBeam extends Stack {

    VerticalBeam(Skin skin) {
        var style = skin.get(Style.class);
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
