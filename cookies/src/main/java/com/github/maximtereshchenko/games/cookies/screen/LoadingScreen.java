package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public final class LoadingScreen implements Screen {

    private final Screen original;
    private final AssetManager assetManager;
    private final ScreenFactory screenFactory;
    private final Game game;

    public LoadingScreen(
        Screen original,
        AssetManager assetManager,
        ScreenFactory screenFactory,
        Game game
    ) {
        this.original = original;
        this.assetManager = assetManager;
        this.screenFactory = screenFactory;
        this.game = game;
    }

    @Override
    public void show() {
        original.show();
    }

    @Override
    public void render(float delta) {
        original.render(delta);
        if (assetManager.update()) {
            game.setScreen(screenFactory.bakeryScreen());
        }
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
    }
}
