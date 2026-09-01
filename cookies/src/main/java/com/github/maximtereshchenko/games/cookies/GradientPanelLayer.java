package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class GradientPanelLayer extends Container<Image> {

    GradientPanelLayer(Skin skin) {
        super(
            new Image(
                skin.get(Style.class).drawable
            )
        );
        fill()
            .height(Value.percentHeight(0.6f, this))
            .bottom();
    }

    private static final class Style {

        Drawable drawable;
    }
}
