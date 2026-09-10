package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.LatchAction;

final class UnlockAchievementAction extends LatchAction {

    private final Achievement achievement;

    UnlockAchievementAction(
        BakeryService bakeryService,
        Achievement achievement,
        Action action
    ) {
        super(bakeryService, action);
        this.achievement = achievement;
    }

    @Override
    protected boolean isFinished(BakeryService bakeryService) {
        return bakeryService.isUnlocked(achievement);
    }
}
