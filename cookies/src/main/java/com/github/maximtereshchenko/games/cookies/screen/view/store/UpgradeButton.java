package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.*;

import java.math.BigDecimal;

final class UpgradeButton extends Container<ImageButton> implements Subscriber<Event> {

    private final Upgrade upgrade;
    private final Style style;
    private final UpgradeTooltipPanel upgradeTooltip;
    private BigDecimal cookieBalance;
    private BigDecimal price;

    UpgradeButton(
        Skin skin,
        I18NBundle bundle,
        Upgrade upgrade,
        BakeryService bakeryService,
        EventBus<Event> eventBus
    ) {
        var buttonStyle = skin.get(upgrade.name(), Style.class);
        super(new ImageButton(buttonStyle));
        this.upgrade = upgrade;
        this.style = buttonStyle;
        this.upgradeTooltip = new UpgradeTooltipPanel(
            skin,
            bundle,
            upgrade,
            eventBus
        );
        this.cookieBalance = BigDecimal.ZERO;
        this.price = BigDecimal.ZERO;
        background(style.background);
        addListener(new TooltipWidget(skin, upgradeTooltip));
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    bakeryService.buyUpgrade(upgrade);
                    remove();
                }
            }
        );
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
        setDisabled(cookieBalance.compareTo(price) < 0);
    }

    private void setDisabled(boolean isDisabled) {
        var imageButton = getActor();
        imageButton.setDisabled(isDisabled);
        imageButton.getImage().setColor(color(isDisabled));
        upgradeTooltip.setDisabled(isDisabled);
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
