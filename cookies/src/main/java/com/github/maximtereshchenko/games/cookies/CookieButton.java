package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

final class CookieButton extends Button {

    private static final float NORMAL_SCALE = 1;
    private static final float HOVERED_SCALE = 1.05f;
    private static final float PRESSED_SCALE = 0.98f;

    CookieButton(Skin skin, Random random) {
        super(skin, "button_cookie");
        setTransform(true);
        addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scale(PRESSED_SCALE);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (isOver()) {
                    scale(HOVERED_SCALE);
                } else {
                    scale(NORMAL_SCALE);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (pointer == -1) {
                    scale(HOVERED_SCALE);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (pointer == -1) {
                    scale(NORMAL_SCALE);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                var stage = getStage();
                stage.addActor(
                    new ClickCookieParticle(
                        skin,
                        random,
                        event.getStageX(),
                        event.getStageY()
                    )
                );
                stage.addActor(
                    new ClickAmountParticle(
                        skin,
                        random,
                        event.getStageX(),
                        event.getStageY()
                    )
                );
            }

            private void scale(float scale) {
                clearActions();
                addAction(
                    Actions.scaleTo(
                        scale,
                        scale,
                        1,
                        Interpolation.elasticOut
                    )
                );
            }
        });
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        var hit = super.hit(x, y, touchable);
        if (hit == this) {
            var radius = getWidth() / 2f;
            if (Vector2.dst2(radius, radius, x, y) <= radius * radius) {
                return this;
            }
            return null;
        }
        return hit;
    }

    @Override
    public void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
    }
}
