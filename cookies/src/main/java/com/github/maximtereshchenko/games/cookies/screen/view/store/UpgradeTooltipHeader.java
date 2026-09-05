package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeTooltipHeader extends Table {

    private final UpgradePriceLabel upgradePriceLabel;

    UpgradeTooltipHeader(
        Skin skin,
        I18NBundle bundle,
        Upgrade upgrade,
        EventBus<Event> eventBus
    ) {
        this.upgradePriceLabel = new UpgradePriceLabel(
            skin,
            upgrade,
            eventBus
        );
        add(
            new UpgradeNameLabel(
                skin,
                bundle,
                upgrade
            )
        )
            .left();
        add().growX();
        add(new Image(skin.get(Style.class).icon))
            .width(Value.prefWidth);
        add(upgradePriceLabel).row();
        add(new UpgradeLabel(skin, bundle)).left();
    }

    void setDisabled(boolean isDisabled) {
        upgradePriceLabel.setDisabled(isDisabled);
    }

    private static final class Style {

        Drawable icon;
    }
}
