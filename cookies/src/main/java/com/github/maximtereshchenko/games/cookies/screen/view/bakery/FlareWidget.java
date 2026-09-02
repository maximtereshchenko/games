package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

final class FlareWidget extends Image {

    static final float CYCLE_TIME = 4;

    FlareWidget(Skin skin) {
        super(skin.get(Style.class).drawable);
        addAction(initialScale());
        addAction(
            Actions.parallel(
                Actions.forever(
                    Actions.rotateBy(60, CYCLE_TIME)
                ),
                Actions.forever(
                    Actions.sequence(
                        Actions.scaleTo(4, 4, CYCLE_TIME),
                        initialScale()
                    )
                ),
                Actions.forever(
                    Actions.sequence(
                        Actions.fadeIn(CYCLE_TIME / 2),
                        Actions.fadeOut(CYCLE_TIME / 2)
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

    private Action initialScale() {
        return Actions.scaleTo(2, 2);
    }

    private static final class Style {

        Drawable drawable;
    }
}
