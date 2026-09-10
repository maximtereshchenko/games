package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

final class UpgradeTooltipWidget extends ParentLeftTooltipWidget {

    UpgradeTooltipWidget(Skin skin, Table table) {
        super(skin, table);
    }

    @Override
    protected float y(Actor actor, float stageY) {
        var vector = new Vector2(0, actor.getHeight());
        actor.localToStageCoordinates(vector);
        return vector.y - getContainer().getHeight();
    }
}
