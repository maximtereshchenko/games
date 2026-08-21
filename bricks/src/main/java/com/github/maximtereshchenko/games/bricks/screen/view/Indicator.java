package com.github.maximtereshchenko.games.bricks.screen.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

public final class Indicator extends Table {

    private final TextureRegion textureRegion;

    public Indicator(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        defaults()
            .pad(Value.percentWidth(0.02f, this));
    }

    public void update(int value) {
        clearChildren();
        for (var i = 0; i < value; i++) {
            add(new Image(textureRegion));
        }
    }
}
