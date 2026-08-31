package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.*;

final class GeneratorDetailsPanel extends Table {

    GeneratorDetailsPanel(Skin skin) {
        add(new Label("Cursor", skin, "label_generator"))
            .colspan(2)
            .left()
            .padBottom(4)
            .row();
        add(new Image(skin, "icon_cookie_small"))
            .width(Value.prefWidth)
            .padTop(2);
        add(new Label("123", skin, "label_generatorCookies"))
            .expandX()
            .left();
    }
}
