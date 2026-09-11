package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.Screen;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.PlayerProgress;

public final class BakeryScreen implements Screen {

    private final Screen original;
    private final BakeryService bakeryService;
    private final PlayerProgress playerProgress;

    public BakeryScreen(
        Screen original,
        BakeryService bakeryService,
        PlayerProgress playerProgress
    ) {
        this.original = original;
        this.bakeryService = bakeryService;
        this.playerProgress = playerProgress;
    }

    @Override
    public void show() {
        original.show();
    }

    @Override
    public void render(float delta) {
        bakeryService.update();
        original.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void pause() {
        original.pause();
    }

    @Override
    public void resume() {
        original.resume();
    }

    @Override
    public void hide() {
        original.hide();
    }

    @Override
    public void dispose() {
        original.dispose();
        playerProgress.flush();
    }
}
