package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.bricks.screen.ScreenFactory;
import com.github.maximtereshchenko.games.bricks.session.PhysicsObjectFactory;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;

final class BricksGameAdapter implements ApplicationListener {

    private BricksGame original;

    static void main() {
        World.setVelocityThreshold(0);
        new Lwjgl3Application(new BricksGameAdapter());
    }

    @Override
    public void create() {
        var shapeRenderer = new ShapeRenderer();
        var screenFactory = new ScreenFactory(
            new SessionFactory(
                shapeRenderer,
                new PhysicsObjectFactory()
            )
        );
        original = new BricksGame(shapeRenderer);
        original.setScreen(screenFactory.sessionScreen());
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void render() {
        original.render();
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
        original.dispose();
    }
}
