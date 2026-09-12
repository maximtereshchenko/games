package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;

public abstract class TooltipWidget extends Tooltip<PopUpFrame> {

    protected TooltipWidget(PopUpFrame popUpFrame) {
        super(popUpFrame, new InstantTooltipManager());
        getContainer().minWidth(380);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        var isHandled = super.mouseMoved(event, x, y);
        setPosition(event);
        return isHandled;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        setPosition(event);
    }

    protected abstract float x(Actor actor);

    protected abstract float y(Actor actor, float stageY);

    private void setPosition(InputEvent event) {
        getContainer()
            .setPosition(
                x(event.getListenerActor()),
                y(event.getListenerActor(), event.getStageY())
            );
    }

    private static final class InstantTooltipManager extends TooltipManager {

        InstantTooltipManager() {
            initialTime = 0;
            offsetX = 0;
            offsetY = 0;
            edgeDistance = 0;
            hideAll();
        }

        @Override
        protected void showAction(Tooltip tooltip) {
            tooltip.getContainer().setTransform(true);
        }

        @Override
        protected void hideAction(Tooltip tooltip) {
            tooltip.getContainer().remove();
        }
    }
}
