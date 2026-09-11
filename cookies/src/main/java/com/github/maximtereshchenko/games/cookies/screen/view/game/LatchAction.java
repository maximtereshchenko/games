package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

public abstract class LatchAction extends DelegateAction {

    private final BakeryService bakeryService;
    private boolean isFinished;

    protected LatchAction(
        BakeryService bakeryService,
        Action action
    ) {
        this.bakeryService = bakeryService;
        this.isFinished = false;
        setAction(action);
    }

    @Override
    protected final boolean delegate(float delta) {
        isFinished = isFinished ||
                     isFinished(bakeryService);
        if (isFinished) {
            return action.act(delta);
        }
        return false;
    }

    protected abstract boolean isFinished(
        BakeryService bakeryService
    );
}
