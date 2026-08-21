package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;

final class SessionScreen extends ScreenAdapter {

    private final Screen original;
    private final World world;

    SessionScreen(Screen original, World world) {
        this.original = original;
        this.world = world;
    }

    @Override
    public void render(float delta) {
        original.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void show() {
        original.show();
    }

    @Override
    public void hide() {
        original.hide();
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
    public void dispose() {
        world.dispose();
    }
}
