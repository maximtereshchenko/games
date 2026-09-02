package com.github.maximtereshchenko.games.cookies.screen.view.cookie;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class CookiesPanelLayer extends Table {

    CookiesPanelLayer(
        Skin skin,
        I18NBundle bundle,
        EventBus<Event> eventBus
    ) {
        add()
            .height(Value.percentHeight(0.1f, this))
            .row();
        add(new CookieAmountPanel(skin, bundle, eventBus))
            .growX();
        top();
    }
}
