package com.github.maximtereshchenko.games.cookies.screen.view.display;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.LatchAction;

final class BuildingCountAction extends LatchAction {

    private final Building building;

    BuildingCountAction(
        BakeryService bakeryService,
        Building building,
        Action action
    ) {
        super(bakeryService, action);
        this.building = building;
    }

    @Override
    protected boolean isFinished(
        BakeryService bakeryService
    ) {
        return bakeryService.count(building) > 0;
    }
}
