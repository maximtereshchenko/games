package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.UpgradeIcon;

import java.util.HashSet;
import java.util.Set;

final class ActiveUpgradeIconPanel extends HorizontalGroup {

    private final Skin skin;
    private final I18NBundle bundle;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final BakeryService bakeryService;
    private final Set<Upgrade> upgrades;

    ActiveUpgradeIconPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        this.skin = skin;
        this.bundle = bundle;
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.bakeryService = bakeryService;
        this.upgrades = new HashSet<>();
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
                skin,
                upgrade
            );
            upgradeIcon.addListener(
                new StatisticsTooltipWidget(
                    skin,
                    new ActiveUpgradeTooltipPanel(
                        skin,
                        bundle,
                        bigDecimalFormatter,
                        bakeryService,
                        upgrade
                    )
                )
            );
            addActor(upgradeIcon);
        }
    }
}
