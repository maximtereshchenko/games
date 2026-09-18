package com.github.maximtereshchenko.games.cookies.screen.view.game.bakery;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Interval;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.util.Random;

final class GoldenCookieButton extends Button {

    private final AssetManager assetManager;
    private final Assets assets;
    private final BakeryService bakeryService;
    private final Random random;

    GoldenCookieButton(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random
    ) {
        var gameAssets = assets.game();
        super(assetManager.get(gameAssets.skin()), "golden-cookie");
        this.assetManager = assetManager;
        this.assets = assets;
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
                            assetManager,
                            assets,
                            mousePointer.x,
                            mousePointer.y,
                            stage.getWidth(),
                            stage.getHeight(),
                            bigDecimalFormatter,
                            bakeryService,
                            bakeryService.goldenCookieEffect()
                        )
                    );
                    assetManager.get(
                            gameAssets.goldenCookieConsumeSound()
                        )
                        .play(bakeryService.volume());
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
            assetManager.get(
                    assets.game()
                        .goldenCookieSpawnSound()
                )
                .play(bakeryService.volume());
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
