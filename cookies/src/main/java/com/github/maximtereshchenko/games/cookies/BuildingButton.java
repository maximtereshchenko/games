package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.*;

final class BuildingButton extends Button {

    BuildingButton(Skin skin) {
        super(skin, "button_generator_0");
        var cookieIcon = new Image(skin, "icon_cookie_small");
        var detailsTable = new Table();
        detailsTable.add(new Label("Cursor", skin, "label_generator")).colspan(2).left().padBottom(4).row();
        detailsTable.add(cookieIcon).width(cookieIcon.getPrefWidth()).padTop(2);
        detailsTable.add(new Label("123", skin, "label_generatorCookies")).expandX().left();
        add(new Image(skin, "icon_cursor_tilted"));
        add(detailsTable).growX();
        add(new Label("123", skin, "label_generatorAmount")).padRight(4);
    }
}
