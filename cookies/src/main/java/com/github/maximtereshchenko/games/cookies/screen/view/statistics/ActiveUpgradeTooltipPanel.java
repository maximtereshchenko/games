package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.view.Badge;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.UpgradeTooltipPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class ActiveUpgradeTooltipPanel extends UpgradeTooltipPanel {

    ActiveUpgradeTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        super(
            skin,
            bundle,
            bigDecimalFormatter,
            bakeryService,
            upgrade
        );
    }

    @Override
    protected List<Badge> badges(Skin skin, I18NBundle bundle) {
        var badges = new ArrayList<>(super.badges(skin, bundle));
        badges.add(
            new Badge(
                skin,
                bundle.get("statistics.upgrades.badge")
            )
        );
        return badges;
    }

    @Override
    protected Label price(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        return new Label(
            bigDecimalFormatter.string(
                bakeryService.price(upgrade)
            ),
            skin,
            "upgrade-price-tooltip"
        );
    }

    @Override
    protected Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.empty();
    }
}
