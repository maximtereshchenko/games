package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

final class CookieAmountPanel extends Table {

    CookieAmountPanel(Skin skin) {
        var cookiesPerSecond = new Label(
            "per second: 123",
            skin,
            "label_cookiesPerSecond"
        );
        cookiesPerSecond.setAlignment(Align.center);
        background(skin.getDrawable("tile_black_transparent40"));
        defaults().pad(4).growX();
        add(cookiesLabel("123,456", skin)).row();
        add(cookiesLabel("cookies", skin)).row();
        add(cookiesPerSecond).row();
    }

    private Label cookiesLabel(String text, Skin skin) {
        var label = new Label(text, skin, "label_cookies");
        label.setAlignment(Align.center);
        return label;
    }
}
