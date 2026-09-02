package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class MilkWidget extends Widget {

    private final Style style;
    private float offsetPercentage;

    MilkWidget(Skin skin) {
        this.style = skin.get(Style.class);
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
            getY() - 0.6f * style.drawable.getMinHeight(),
            getWidth(),
            style.drawable.getMinHeight()
        );
    }

    private static final class Style {

        Drawable drawable;
    }
}
