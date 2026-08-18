package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.configuration.CellDefinition;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;

import java.util.List;

public final class ScreenFactory {

    private final SessionFactory sessionFactory;

    public ScreenFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Screen sessionScreen(
        Blueprints blueprints,
        List<List<CellDefinition>> cellDefinitions
    ) {
        var viewport = new FitViewport(50, 75);
        var physicsWorld = sessionFactory.world(viewport);
        return new SessionScreen(
            viewport,
            sessionFactory.registry(
                viewport,
                blueprints,
                cellDefinitions,
                physicsWorld
            ),
            physicsWorld
        );
    }
}
