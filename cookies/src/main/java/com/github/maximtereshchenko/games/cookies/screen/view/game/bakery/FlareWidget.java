package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Buff;

final class FlareWidget extends Image {

    static final float CYCLE_TIME_SECONDS = 2;

    private final Style style;
    private final BakeryService bakeryService;

    FlareWidget(Skin skin, BakeryService bakeryService) {
        this.style = skin.get(Style.class);
        this.bakeryService = bakeryService;
        var initialScale = 2;
        setScale(initialScale);
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
                    Actions.scaleTo(initialScale, initialScale)
                )
            )
        );
    }

    @Override
    public void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setDrawable(drawable());
    }

    private Drawable drawable() {
        if (hasActiveBuff()) {
            return style.activeBuffDrawable;
        }
        return style.standardDrawable;
    }

    private boolean hasActiveBuff() {
        for (var buff : Buff.values()) {
            if (bakeryService.buffInterval(buff).progress() < 1) {
                return true;
            }
        }
        return false;
    }

    private static final class Style {

        Drawable standardDrawable;
        Drawable activeBuffDrawable;
    }
}
