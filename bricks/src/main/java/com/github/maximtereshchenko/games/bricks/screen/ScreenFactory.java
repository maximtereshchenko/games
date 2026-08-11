package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;

//It is impossibly hard to test LibGDX UI
//Therefore, the whole package remains untested
public final class ScreenFactory {

    private final SessionFactory sessionFactory;

    public ScreenFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Screen sessionScreen() {
        var viewport = new FitViewport(10.37f, 15.55f);
        var physicsWorld = sessionFactory.physicsWorld(viewport);
        return new SessionScreen(
            viewport,
            sessionFactory.registry(viewport, physicsWorld),
            physicsWorld
        );
    }
}
