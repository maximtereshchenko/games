package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class AchievementStatisticsLabel extends PercentStatisticsLabel<Achievement> {

    AchievementStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        super(
            assetManager,
            assets,
            "statistics.achievements.unlocked.value",
            bakeryService,
            Achievement.values()
        );
    }

    @Override
    boolean isCounted(BakeryService bakeryService, Achievement value) {
        return bakeryService.isUnlocked(value);
    }
}
