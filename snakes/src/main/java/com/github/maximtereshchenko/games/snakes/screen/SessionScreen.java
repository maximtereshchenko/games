package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.event.EventBus;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.event.SessionEnded;
import com.github.maximtereshchenko.games.snakes.session.Dead;
import com.github.maximtereshchenko.games.snakes.session.Statistics;

import java.util.Set;

final class SessionScreen extends ScreenAdapter {

    private final Set<Viewport> viewports;
    private final EventBus<ApplicationEvent> eventBus;
    private final Registry registry;
    private final Iterable<Entity> deadEntities;
    private final Iterable<Entity> statisticsEntities;

    SessionScreen(
        Set<Viewport> viewports,
        EventBus<ApplicationEvent> eventBus,
        Registry registry
    ) {
        this.viewports = viewports;
        this.eventBus = eventBus;
        this.registry = registry;
        this.deadEntities = registry.entities(
            new Query().all(Dead.class)
        );
        this.statisticsEntities = registry.entities(
            new Query().all(Statistics.class)
        );
    }

    @Override
    public void render(float delta) {
        registry.update(delta);
        for (var _ : deadEntities) {
            for (var statisticsEntity : statisticsEntities) {
                eventBus.publish(
                    new SessionEnded(
                        statisticsEntity.component(Statistics.class)
                            .value
                    )
                );
                return;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewports.forEach(viewport -> viewport.update(width, height, true));
    }

    @Override
    public void show() {
        registry.update(0);
    }
}
