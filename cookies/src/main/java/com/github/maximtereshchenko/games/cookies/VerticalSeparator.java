package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;

final class VerticalSeparator extends Stack {

    VerticalSeparator(Skin skin) {
        add(background(skin));
        add(new Image(skin, "gradient_top"));
        add(new Image(skin, "gradient_bottom"));
    }

    private Container<?> background(Skin skin) {
        var tiledDrawable = skin.getTiledDrawable("tile_beam_vertical");
        tiledDrawable.setAlign(Align.top);
        var container = new Container<>();
        container.setBackground(tiledDrawable);
        return container;
    }
}
