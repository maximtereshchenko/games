package com.github.maximtereshchenko.games.cookies.screen.view.loading;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

final class Spinner extends Image {

    Spinner(Skin skin, String styleName, float rotateDurationSeconds) {
        super(skin.get(styleName, Style.class).drawable);
        setOrigin(Align.center);
        addAction(
            Actions.forever(
                Actions.rotateBy(
                    -360,
                    rotateDurationSeconds,
                    Interpolation.pow2
                )
            )
        );
    }

    private static final class Style {

        Drawable drawable;
    }
}
