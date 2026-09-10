package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.LatchAction;

final class UnlockBuildingAction extends LatchAction {

    private final Building building;

    UnlockBuildingAction(
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
        return bakeryService.cumulativeBaked()
                   .compareTo(
                       bakeryService.transactionValue(building)
                   ) >= 0;
    }
}
