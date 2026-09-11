package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.maximtereshchenko.games.cookies.screen.view.game.TooltipWidget;

abstract class ParentLeftTooltipWidget extends TooltipWidget {

    ParentLeftTooltipWidget(Skin skin, Table table) {
        super(skin, table);
    }

    @Override
    protected final float x(Actor actor) {
        var vector = new Vector2();
        actor.getParent().localToStageCoordinates(vector);
        return vector.x - getContainer().getWidth() - 30;
    }
}
