package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

final class GeneratorButton extends Button {

    GeneratorButton(Skin skin) {
        super(skin, "button_generator_0");
        add(new Image(skin, "icon_cursor_tilted"));
        add(new GeneratorDetailsPanel(skin)).growX();
        add(new Label("123", skin, "label_generatorAmount")).padRight(4);
    }
}
