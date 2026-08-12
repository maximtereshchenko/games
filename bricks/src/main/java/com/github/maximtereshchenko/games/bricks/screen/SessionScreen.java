package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Registry;

final class SessionScreen extends ScreenAdapter {

    private final Viewport viewport;
    private final Registry registry;
    private final World world;

    SessionScreen(
        Viewport viewport,
        Registry registry,
        World world
    ) {
        this.viewport = viewport;
        this.registry = registry;
        this.world = world;
    }

    @Override
    public void render(float delta) {
        registry.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
