package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

final class Flare extends Image {

    static final float CYCLE_TIME = 4;

    Flare(Skin skin) {
        super(skin.get(Style.class).drawable);
        addAction(
            Actions.forever(
                Actions.parallel(
                    Actions.forever(
                        Actions.rotateBy(60, CYCLE_TIME)
                    ),
                    Actions.forever(
                        Actions.sequence(
                            Actions.scaleTo(4, 4, CYCLE_TIME),
                            Actions.scaleTo(2, 2)
                        )
                    ),
                    Actions.forever(
                        Actions.sequence(
                            Actions.fadeIn(CYCLE_TIME / 2),
                            Actions.fadeOut(CYCLE_TIME / 2)
                        )
                    )
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
