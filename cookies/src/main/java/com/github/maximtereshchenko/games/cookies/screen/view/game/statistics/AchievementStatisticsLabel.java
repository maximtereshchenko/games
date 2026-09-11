package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class AchievementStatisticsLabel extends PercentStatisticsLabel<Achievement> {

    AchievementStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(
            skin,
            bundle,
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
