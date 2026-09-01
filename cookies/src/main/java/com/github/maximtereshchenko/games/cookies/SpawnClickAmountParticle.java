package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.Subscriber;

import java.util.Random;

final class SpawnClickAmountParticle implements Subscriber<Event> {

    private final Skin skin;
    private final Stage stage;
    private final Random random;

    SpawnClickAmountParticle(
        Skin skin,
        Stage stage,
        Random random
    ) {
        this.skin = skin;
        this.stage = stage;
        this.random = random;
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof CookiesClicked cookiesClicked)) {
            return;
        }
        var vector2 = new Vector2(
            Gdx.input.getX(),
            Gdx.input.getY()
        );
        stage.screenToStageCoordinates(vector2);
        stage.addActor(
            new ClickAmountParticle(
                skin,
                random,
                vector2.x,
                vector2.y,
                cookiesClicked.value()
            )
        );
    }
}
