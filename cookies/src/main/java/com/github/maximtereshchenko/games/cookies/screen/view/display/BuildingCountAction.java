package com.github.maximtereshchenko.games.cookies.screen.view.display;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.LatchAction;

final class BuildingCountAction extends LatchAction {

    BuildingCountAction(
        BakeryService bakeryService,
        Building building,
        Action action
    ) {
        super(bakeryService, building, action);
    }

    @Override
    protected boolean isFinished(
        BakeryService bakeryService,
        Building building
    ) {
        return bakeryService.count(building) > 0;
    }
}
