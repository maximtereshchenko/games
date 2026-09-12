package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.TooltipWidget;

final class StatisticsTooltipWidget extends TooltipWidget {

    StatisticsTooltipWidget(PopUpFrame popUpFrame) {
        super(popUpFrame);
    }

    @Override
    protected float x(Actor actor) {
        var vector = new Vector2();
        actor.localToStageCoordinates(vector);
        return vector.x +
               actor.getWidth() / 2 -
               getContainer().getWidth() / 2;
    }

    @Override
    protected float y(Actor actor, float stageY) {
        var vector = new Vector2(0, actor.getHeight() * 1.2f);
        actor.localToStageCoordinates(vector);
        return vector.y;
    }
}
