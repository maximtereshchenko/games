package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.domain.UpgradePriceUpdated;

final class UpgradePriceLabel extends Label implements Subscriber<Event> {

    private final Style style;
    private final Upgrade upgrade;

    UpgradePriceLabel(
        Skin skin,
        Upgrade upgrade,
        EventBus<Event> eventBus
    ) {
        var labelStyle = skin.get(Style.class);
        super("", labelStyle.labelStyle(true));
        this.style = labelStyle;
        this.upgrade = upgrade;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof UpgradePriceUpdated upgradePriceUpdated &&
            upgradePriceUpdated.upgrade() == upgrade
        ) {
            setText(upgradePriceUpdated.price().toString());
        }
    }

    void setDisabled(boolean isDisabled) {
        setStyle(style.labelStyle(isDisabled));
    }

    private static final class Style {

        private BitmapFont font;
        private Color enabledFontColor;
        private Color disabledFontColor;

        LabelStyle labelStyle(boolean isDisabled) {
            return new LabelStyle(font, color(isDisabled));
        }

        private Color color(boolean isDisabled) {
            if (isDisabled) {
                return disabledFontColor;
            }
            return enabledFontColor;
        }
    }
}
