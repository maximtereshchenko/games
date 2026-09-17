package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class BuildingsCountStatisticsLabel extends StatisticsValueLabel {

    BuildingsCountStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        super(assetManager, assets, bakeryService);
    }

    @Override
    String text(BakeryService bakeryService) {
        return String.valueOf(bakeryService.totalBuildingCount());
    }
}
