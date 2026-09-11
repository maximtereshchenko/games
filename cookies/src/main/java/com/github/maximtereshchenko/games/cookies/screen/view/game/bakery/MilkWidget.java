package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class MilkWidget extends Widget {

    private final Style style;
    private final BakeryService bakeryService;
    private float offsetPercentage;

    MilkWidget(Skin skin, BakeryService bakeryService) {
        this.style = skin.get(Style.class);
        this.bakeryService = bakeryService;
        setTouchable(Touchable.disabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawShiftedLeft(batch, getWidth());
        drawShiftedLeft(batch, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        offsetPercentage += delta / 4;
        offsetPercentage %= 1;
    }

    private void drawShiftedLeft(Batch batch, float shift) {
        style.drawable.draw(
            batch,
            getX() + getWidth() * offsetPercentage - shift,
            getY() - Math.max(0, 1 - bakeryService.milk()) *
                     style.drawable.getMinHeight(),
            getWidth(),
            style.drawable.getMinHeight()
        );
    }

    private static final class Style {

        Drawable drawable;
    }
}
