package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

final class FlowingMilkLayer extends Container<FlowingMilkWidget> {

    FlowingMilkLayer(Skin skin) {
        super(new FlowingMilkWidget(skin));
        fill()
            .height(Value.percentHeight(0.1f, this))
            .bottom();
    }
}
