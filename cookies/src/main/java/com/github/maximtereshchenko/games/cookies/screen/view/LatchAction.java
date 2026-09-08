package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

public abstract class LatchAction extends DelegateAction {

    private final BakeryService bakeryService;
    private final Building building;
    private boolean isFinished;

    protected LatchAction(
        BakeryService bakeryService,
        Building building,
        Action action
    ) {
        this.bakeryService = bakeryService;
        this.building = building;
        this.isFinished = false;
        setAction(action);
    }

    @Override
    protected final boolean delegate(float delta) {
        isFinished = isFinished ||
                     isFinished(bakeryService, building);
        if (isFinished) {
            return action.act(delta);
        }
        return false;
    }

    protected abstract boolean isFinished(
        BakeryService bakeryService,
        Building building
    );
}
