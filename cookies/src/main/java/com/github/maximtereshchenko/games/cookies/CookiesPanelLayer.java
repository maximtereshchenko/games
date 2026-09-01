package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.maximtereshchenko.games.common.event.EventBus;

final class CookiesPanelLayer extends Table {

    CookiesPanelLayer(Skin skin, EventBus<Event> eventBus) {
        add()
            .height(Value.percentHeight(0.1f, this))
            .row();
        add(new CookieAmountPanel(skin, eventBus)).growX();
        top();
    }
}
