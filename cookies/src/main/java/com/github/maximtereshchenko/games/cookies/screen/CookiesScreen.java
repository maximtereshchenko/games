package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.Screen;
import com.github.maximtereshchenko.games.cookies.domain.CookieService;

public final class CookiesScreen implements Screen {

    private final Screen original;
    private final CookieService cookieService;

    public CookiesScreen(Screen original, CookieService cookieService) {
        this.original = original;
        this.cookieService = cookieService;
    }

    @Override
    public void show() {
        original.show();
    }

    @Override
    public void render(float delta) {
        cookieService.update(delta);
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
    }
}
