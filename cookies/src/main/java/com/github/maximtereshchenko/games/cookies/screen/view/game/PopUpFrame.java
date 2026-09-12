package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public final class PopUpFrame extends Stack {

    public PopUpFrame(Skin skin, Table table) {
        var style = skin.get(Style.class);
        for (var drawable : style.drawables) {
            add(new Image(drawable));
        }
        add(
            new Container<>(table)
                .fill()
                .pad(8, 16, 8, 16)
        );
    }

    private static final class Style {

        Drawable[] drawables;
    }
}
