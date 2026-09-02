package com.github.maximtereshchenko.games.cookies.screen.view.cookie;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.CookiesPerSecondUpdated;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class CookieAmountPanel extends Table implements Subscriber<Event> {

    private final I18NBundle bundle;
    private final Label cookiesPerSecond;

    CookieAmountPanel(
        Skin skin,
        I18NBundle bundle,
        EventBus<Event> eventBus
    ) {
        this.bundle = bundle;
        this.cookiesPerSecond = new Label(
            "",
            skin,
            "label_cookiesPerSecond"
        );
        cookiesPerSecond.setAlignment(Align.center);
        background(skin.get(Style.class).background);
        defaults().pad(4).growX();
        add(new CookieAmountLabel(skin, eventBus)).row();
        add(new CookiesLabel(bundle.get("cookies.name"), skin)).row();
        add(cookiesPerSecond).row();
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof CookiesPerSecondUpdated cookiesPerSecondUpdated) {
            cookiesPerSecond.setText(
                bundle.format(
                    "cookies.cookiesPerSecond",
                    cookiesPerSecondUpdated.value().toPlainString()
                )
            );
        }
    }

    private static final class Style {

        Drawable background;
    }
}
