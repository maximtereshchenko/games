package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class FlowingMilkWidget extends Widget {

    private final Style style;
    private float offsetPercentage;

    FlowingMilkWidget(Skin skin) {
        this.style = skin.get(Style.class);
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
            getY() + getHeight() - style.drawable.getMinHeight(),
            getWidth(),
            style.drawable.getMinHeight()
        );
    }

    private static final class Style {

        Drawable drawable;
    }
}
