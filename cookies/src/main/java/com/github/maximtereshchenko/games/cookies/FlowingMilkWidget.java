package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

final class FlowingMilkWidget extends Widget {

    private final Drawable drawable;
    private float offsetPercentage;

    FlowingMilkWidget(Skin skin) {
        this.drawable = skin.getDrawable("texture_milk_plain");
        addAction(
            new Action() {

                @Override
                public boolean act(float delta) {
                    offsetPercentage += delta / 4;
                    if (offsetPercentage > 1) {
                        offsetPercentage -= 1;
                    }
                    return false;
                }
            }
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawShiftedLeft(batch, getWidth());
        drawShiftedLeft(batch, 0);
    }

    private void drawShiftedLeft(Batch batch, float shift) {
        drawable.draw(
            batch,
            getX() + getWidth() * offsetPercentage - shift,
            getY() + getHeight() - drawable.getMinHeight(),
            getWidth(),
            drawable.getMinHeight()
        );
    }
}
