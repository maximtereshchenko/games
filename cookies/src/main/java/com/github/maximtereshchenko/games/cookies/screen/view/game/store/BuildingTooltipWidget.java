package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;

final class BuildingTooltipWidget extends ParentLeftTooltipWidget {

    BuildingTooltipWidget(PopUpFrame popUpFrame) {
        super(popUpFrame);
    }

    @Override
    protected float y(Actor actor, float stageY) {
        return stageY - getContainer().getHeight() + actor.getHeight() / 2;
    }
}
