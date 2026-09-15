package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Interval;

import java.util.Random;

final class GoldenCookieButton extends Button {

    private final BakeryService bakeryService;
    private final Random random;

    GoldenCookieButton(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Random random
    ) {
        super(skin, "golden-cookie");
        this.bakeryService = bakeryService;
        this.random = random;
        setTransform(true);
        setOrigin(Align.center);
        setVisible(false);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    var mousePointer = new Vector2(
                        Gdx.input.getX(),
                        Gdx.input.getY()
                    );
                    var stage = getStage();
                    stage.screenToStageCoordinates(mousePointer);
                    stage.addActor(
                        new GoldenCookieNotification(
                            skin,
                            bundle,
                            mousePointer.x,
                            mousePointer.y,
                            stage.getWidth(),
                            stage.getHeight(),
                            bakeryService,
                            bakeryService.goldenCookieEffect()
                        )
                    );
                }
            }
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        var interval = bakeryService.goldenCookieInterval();
        var isVisible = interval.remainingSeconds() != 0;
        if (!isVisible() && isVisible) {
            setPosition(
                random.nextFloat(getParent().getWidth() - getPrefWidth()),
                random.nextFloat(getParent().getHeight() - getPrefHeight())
            );
        }
        setVisible(isVisible);
        transform(interval);
    }

    private void transform(Interval interval) {
        if (!isVisible()) {
            return;
        }
        var curve = 1.0 - Math.pow(2 * interval.progress() - 1, 4);
        getColor().a = (float) curve;
        var wobble = 1 + 0.06 * Math.sin(7.5 * interval.remainingSeconds());
        setScale((float) (curve * wobble));
        setRotation((float) (5 * Math.sin(10.5 * interval.remainingSeconds())));
    }
}
