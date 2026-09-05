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

final class UpgradeTooltipPanel extends Table {

    private final UpgradeTooltipHeader upgradeTooltipHeader;

    UpgradeTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        Upgrade upgrade,
        EventBus<Event> eventBus
    ) {
        this.upgradeTooltipHeader = new UpgradeTooltipHeader(
            skin,
            bundle,
            upgrade,
            eventBus
        );
        var style = skin.get(upgrade.name(), Style.class);
        defaults().padBottom(8);
        add(new Image(style.upgradeIcon))
            .width(Value.prefWidth);
        add(upgradeTooltipHeader).growX().row();
        add(new Image(style.separator))
            .colspan(2)
            .growX()
            .row();
    }

    void setDisabled(boolean isDisabled) {
        upgradeTooltipHeader.setDisabled(isDisabled);
    }

    private static final class Style {

        Drawable upgradeIcon;
        Drawable separator;
    }
}
