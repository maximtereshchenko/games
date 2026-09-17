package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.*;

import java.util.Random;

final class UpgradeButton extends ColoredButton {

    private final Style style;
    private final UpgradeIcon upgradeIcon;
    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    UpgradeButton(
        Actor parent,
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random,
        Upgrade upgrade
    ) {
        var icon = new UpgradeIcon(assetManager, assets, upgrade);
        super(assetManager, assets, icon);
        var gameAssets = assets.game();
        this.style = assetManager.get(gameAssets.skin()).get(Style.class);
        this.upgradeIcon = icon;
        this.bakeryService = bakeryService;
        this.upgrade = upgrade;
        addListener(
            new SameTopHeightTooltipWidget(
                parent,
                new PopUpFrame(
                    assetManager,
                    assets,
                    new UnlockedUpgradeTooltipPanel(
                        assetManager,
                        assets,
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
                    var buildingSounds = gameAssets.buildingSounds();
                    assetManager.get(
                            buildingSounds.get(
                                random.nextInt(buildingSounds.size())
                            )
                        )
                        .play();
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
