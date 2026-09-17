package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class MilkStatisticsLabel extends StatisticsValueLabel {

    private final I18NBundle bundle;

    MilkStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        super(assetManager, assets, bakeryService);
        this.bundle = assetManager.get(assets.game().bundle());
    }

    @Override
    String text(BakeryService bakeryService) {
        return bundle.format(
            "statistics.achievements.milk.value",
            bakeryService.milk()
        );
    }
}
