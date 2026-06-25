package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import dev.dominion.ecs.api.Dominion;

import java.awt.*;

final class EntityFactory {

    private static final Color DARK_GREEN = new Color(0x00dd00ff);

    private final Dominion dominion;

    EntityFactory(Dominion dominion) {
        this.dominion = dominion;
    }

    void createTurnStartedEvent() {
        dominion.createEntity(TurnStarted.INSTANCE, Event.INSTANCE);
    }

    void createHead(HeadDirection headDirection, Point point) {
        dominion.createEntity(headDirection, point, DARK_GREEN);
    }
}
