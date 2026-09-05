package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class TooltipWidget extends Tooltip<Stack> {

    TooltipWidget(Skin skin, Table table) {
        super(new Stack(), new InstantTooltipManager());
        var style = skin.get(Style.class);
        var stack = getActor();
        for (var drawable : style.drawables) {
            stack.add(new Image(drawable));
        }
        stack.add(
            new Container<>(table)
                .fill()
                .pad(8, 16, 8, 16)
        );
        getContainer().minWidth(380);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        var isHandled = super.mouseMoved(event, x, y);
        setPosition(event.getListenerActor());
        return isHandled;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        setPosition(event.getListenerActor());
    }

    private void setPosition(Actor actor) {
        var container = getContainer();
        var vector = new Vector2(0, actor.getHeight());
        actor.getParent().localToStageCoordinates(vector);
        container.setPosition(
            vector.x - container.getWidth() - 30,
            vector.y - container.getHeight()
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

    private static final class Style {

        Drawable[] drawables;
    }
}
