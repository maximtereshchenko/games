package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

final class FlareWidget extends Image {

    static final float CYCLE_TIME_SECONDS = 2;

    FlareWidget(Skin skin) {
        super(skin.get(Style.class).drawable);
        scaleBy(2);
        getColor().a = 0;
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.parallel(
                        Actions.rotateBy(30, CYCLE_TIME_SECONDS),
                        Actions.scaleTo(3, 3, CYCLE_TIME_SECONDS),
                        Actions.fadeIn(CYCLE_TIME_SECONDS)
                    ),
                    Actions.parallel(
                        Actions.rotateBy(30, CYCLE_TIME_SECONDS),
                        Actions.scaleTo(4, 4, CYCLE_TIME_SECONDS),
                        Actions.fadeOut(CYCLE_TIME_SECONDS)
                    ),
                    Actions.scaleTo(2, 2)
                )
            )
        );
    }

    @Override
    public void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
    }

    private static final class Style {

        Drawable drawable;
    }
}
