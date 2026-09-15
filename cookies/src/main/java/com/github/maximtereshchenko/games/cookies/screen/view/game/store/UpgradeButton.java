package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.ColoredButton;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.UpgradeIcon;

final class UpgradeButton extends ColoredButton {

    private final Style style;
    private final UpgradeIcon upgradeIcon;
    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    UpgradeButton(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        var icon = new UpgradeIcon(skin, upgrade);
        super(skin, icon);
        this.style = skin.get(Style.class);
        this.upgradeIcon = icon;
        this.bakeryService = bakeryService;
        this.upgrade = upgrade;
        addListener(
            new UpgradeTooltipWidget(
                new PopUpFrame(
                    skin,
                    new UnlockedUpgradeTooltipPanel(
                        skin,
                        bundle,
                        bigDecimalFormatter,
                        bakeryService,
                        upgrade
                    )
                )
            )
        );
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    bakeryService.buyUpgrade(upgrade);
                    remove();
                }
            }
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var isDisabled = !bakeryService.canAfford(upgrade);
        setDisabled(isDisabled);
        upgradeIcon.setColor(color(isDisabled));
    }

    private Color color(boolean isDisabled) {
        if (isDisabled) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style {

        Color enabledColor;
        Color disabledColor;
    }
}
