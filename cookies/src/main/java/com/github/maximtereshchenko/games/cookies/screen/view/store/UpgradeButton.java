package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.CookieBalanceUpdated;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.domain.UpgradePriceUpdated;

import java.math.BigDecimal;

final class UpgradeButton extends Container<ImageButton> implements Subscriber<Event> {

    private final Upgrade upgrade;
    private final Style style;
    private BigDecimal cookieBalance;
    private BigDecimal price;

    UpgradeButton(
        Skin skin,
        Upgrade upgrade,
        EventBus<Event> eventBus
    ) {
        var buttonStyle = skin.get(upgrade.name(), Style.class);
        super(new ImageButton(buttonStyle));
        this.upgrade = upgrade;
        this.style = buttonStyle;
        this.cookieBalance = BigDecimal.ZERO;
        this.price = BigDecimal.ZERO;
        background(style.background);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        switch (event) {
            case CookieBalanceUpdated cookieBalanceUpdated -> {
                cookieBalance = cookieBalanceUpdated.value();
                setState();
            }
            case UpgradePriceUpdated upgradePriceUpdated
                when upgradePriceUpdated.upgrade() == upgrade -> {
                price = upgradePriceUpdated.price();
                setState();
            }
            default -> {
                //empty
            }
        }
    }

    private void setState() {
        var isDisabled = cookieBalance.compareTo(price) < 0;
        var imageButton = getActor();
        imageButton.setDisabled(isDisabled);
        imageButton.getImage().setColor(color(isDisabled));
    }

    private Color color(boolean isDisabled) {
        if (isDisabled) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style extends ImageButton.ImageButtonStyle {

        Drawable background;
        Color enabledColor;
        Color disabledColor;
    }
}
