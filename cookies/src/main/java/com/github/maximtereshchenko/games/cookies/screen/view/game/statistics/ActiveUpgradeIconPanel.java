package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.TopCenterTooltipWidget;
import com.github.maximtereshchenko.games.cookies.screen.view.game.UpgradeIcon;

import java.util.Set;
import java.util.TreeSet;

final class ActiveUpgradeIconPanel extends HorizontalGroup {

    private final AssetManager assetManager;
    private final Assets assets;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;
    private final Set<Upgrade> upgrades;

    ActiveUpgradeIconPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        this.assetManager = assetManager;
        this.assets = assets;
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
        this.upgrades = new TreeSet<>();
        wrap();
        rowAlign(Align.left);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var size = upgrades.size();
        for (var upgrade : Upgrade.values()) {
            if (bakeryService.isActive(upgrade)) {
                upgrades.add(upgrade);
            }
        }
        if (size != upgrades.size()) {
            recreate();
        }
    }

    private void recreate() {
        clearChildren();
        for (var upgrade : upgrades) {
            var upgradeIcon = new UpgradeIcon(
                assetManager,
                assets,
                upgrade
            );
            upgradeIcon.addListener(
                new TopCenterTooltipWidget(
                    new PopUpFrame(
                        assetManager,
                        assets,
                        new ActiveUpgradeTooltipPanel(
                            assetManager,
                            assets,
                            bigDecimalFormatter,
                            bakeryService,
                            upgrade
                        )
                    )
                )
            );
            addActor(upgradeIcon);
        }
    }
}
