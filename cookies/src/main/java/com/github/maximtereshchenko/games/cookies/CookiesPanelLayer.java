package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

final class CookiesPanelLayer extends Table {

    CookiesPanelLayer(Skin skin) {
        add()
            .height(Value.percentHeight(0.1f, this))
            .row();
        add(new CookieAmountPanel(skin)).growX();
        top();
    }
}
