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

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

final class GoldenCookieButton extends Button {

    private final BakeryService bakeryService;
    private final Random random;
    private Instant start;

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
        var isVisible = bakeryService.lastUpdatedTimestamp()
            .isBefore(bakeryService.goldenCookieTimestamp());
        if (!isVisible() && isVisible) {
            start = bakeryService.lastUpdatedTimestamp();
            setPosition(
                random.nextFloat(getParent().getWidth() - getPrefWidth()),
                random.nextFloat(getParent().getHeight() - getPrefHeight())
            );
        }
        setVisible(isVisible);
        transform();
    }

    private void transform() {
        if (!isVisible()) {
            return;
        }
        var millisPastStart = millisBetween(start, bakeryService.lastUpdatedTimestamp());
        var progress = (double) millisPastStart /
                       millisBetween(start, bakeryService.goldenCookieTimestamp());
        var curve = (float) (1.0 - Math.pow(2 * progress - 1, 4));
        getColor().a = curve;
        var wobble = (float) (1 + 0.06 * Math.sin(0.0075 * millisPastStart));
        setScale(curve * wobble);
        setRotation((float) (5 * Math.sin(0.0105 * millisPastStart)));
    }

    private long millisBetween(Instant from, Instant to) {
        return Duration.between(from, to).toMillis();
    }
}
