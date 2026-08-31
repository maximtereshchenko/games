package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;

final class VerticalBeam extends Stack {

    VerticalBeam(Skin skin) {
        var tiledDrawable = skin.getTiledDrawable("tile_beam_vertical");
        tiledDrawable.setAlign(Align.top);
        var container = new Container<>();
        container.setBackground(tiledDrawable);
        add(container);
        add(new Image(skin, "gradient_top"));
        add(new Image(skin, "gradient_bottom"));
    }
}
