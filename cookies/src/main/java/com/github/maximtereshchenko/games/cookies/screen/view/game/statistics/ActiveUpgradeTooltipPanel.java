package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.Badge;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.UpgradeTooltipPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class ActiveUpgradeTooltipPanel extends UpgradeTooltipPanel {

    ActiveUpgradeTooltipPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        super(
            assetManager,
            assets,
            bigDecimalFormatter,
            bakeryService,
            upgrade
        );
    }

    @Override
    protected List<Badge> badges(AssetManager assetManager, Assets assets) {
        var badges = new ArrayList<>(super.badges(assetManager, assets));
        badges.add(
            new Badge(
                assetManager,
                assets,
                assetManager.get(assets.game().bundle()).get("statistics.upgrades.badge")
            )
        );
        return badges;
    }

    @Override
    protected Label price(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        return new Label(
            bigDecimalFormatter.string(
                bakeryService.price(upgrade)
            ),
            assetManager.get(assets.game().skin()),
            "upgrade-price-tooltip"
        );
    }

    @Override
    protected Optional<Actor> footer(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.empty();
    }
}
