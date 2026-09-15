package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class ActorLeftTooltipWidget extends TooltipWidget {

    private final Actor actor;

    protected ActorLeftTooltipWidget(Actor actor, PopUpFrame popUpFrame) {
        this.actor = actor;
        super(popUpFrame);
    }

    @Override
    protected final float x(Actor actor) {
        var vector = new Vector2();
        this.actor.localToStageCoordinates(vector);
        return vector.x - getContainer().getWidth() - 30;
    }
}
