package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;

final class UpgradeTooltipWidget extends ParentLeftTooltipWidget {

    UpgradeTooltipWidget(PopUpFrame popUpFrame) {
        super(popUpFrame);
    }

    @Override
    protected float y(Actor actor, float stageY) {
        var vector = new Vector2(0, actor.getHeight());
        actor.localToStageCoordinates(vector);
        return vector.y - getContainer().getHeight();
    }
}
