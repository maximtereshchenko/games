package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class UnlockBuildingAction extends DelegateAction {

    private final BakeryService bakeryService;
    private final Building building;
    private boolean isUnlocked;

    UnlockBuildingAction(
        BakeryService bakeryService,
        Building building,
        Action action
    ) {
        this.bakeryService = bakeryService;
        this.building = building;
        this.isUnlocked = false;
        setAction(action);
    }

    @Override
    protected boolean delegate(float delta) {
        isUnlocked = isUnlocked || cumulativeBakedGreaterThanTransactionValue();
        if (isUnlocked) {
            return action.act(delta);
        }
        return false;
    }

    private boolean cumulativeBakedGreaterThanTransactionValue() {
        return bakeryService.cumulativeBaked()
                   .compareTo(
                       bakeryService.transactionValue(building)
                   ) >= 0;
    }
}
