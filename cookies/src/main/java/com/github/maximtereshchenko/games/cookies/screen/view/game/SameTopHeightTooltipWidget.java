package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class SameTopHeightTooltipWidget extends ActorLeftTooltipWidget {

    public SameTopHeightTooltipWidget(Actor actor, PopUpFrame popUpFrame) {
        super(actor, popUpFrame);
    }

    @Override
    protected float y(Actor actor, float stageY) {
        var vector = new Vector2(0, actor.getHeight());
        actor.localToStageCoordinates(vector);
        return vector.y - getContainer().getHeight();
    }
}
