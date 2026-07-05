package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Screen;

import java.util.function.Supplier;

final class LazyScreen implements Screen {

    private final Supplier<Screen> supplier;
    private Screen original;

    LazyScreen(Supplier<Screen> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void show() {
        screen().show();
    }

    @Override
    public void render(float delta) {
        screen().render(delta);
    }

    @Override
    public void resize(int width, int height) {
        screen().resize(width, height);
    }

    @Override
    public void pause() {
        screen().pause();
    }

    @Override
    public void resume() {
        screen().resume();
    }

    @Override
    public void hide() {
        screen().hide();
    }

    @Override
    public void dispose() {
        screen().dispose();
    }

    private Screen screen() {
        if (original == null) {
            original = supplier.get();
        }
        return original;
    }
}
