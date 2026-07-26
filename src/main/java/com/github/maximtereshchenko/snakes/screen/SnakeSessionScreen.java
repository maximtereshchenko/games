package com.github.maximtereshchenko.snakes.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.snakes.session.Session;
import com.github.maximtereshchenko.snakes.session.SessionStatisticsAccumulator;

import java.util.Set;

final class SnakeSessionScreen extends ScreenAdapter {

    private final Set<Viewport> viewports;
    private final ApplicationEvents applicationEvents;
    private final World world;
    private final Iterable<Entity> sessionEntities;
    private final Iterable<Entity> statisticsAccumulatorEntities;

    SnakeSessionScreen(
        Set<Viewport> viewports,
        ApplicationEvents applicationEvents,
        World world
    ) {
        this.viewports = viewports;
        this.applicationEvents = applicationEvents;
        this.world = world;
        this.sessionEntities = world.entities(
            new Query()
                .all(Session.class)
        );
        this.statisticsAccumulatorEntities = world.entities(
            new Query().all(SessionStatisticsAccumulator.class)
        );
    }

    @Override
    public void render(float delta) {
        if (sessionEnded()) {
            applicationEvents.publish(
                new SnakeSessionEnded(
                    statisticsAccumulatorEntities
                        .iterator()
                        .next()
                        .component(SessionStatisticsAccumulator.class)
                        .value
                )
            );
            return;
        }
        world.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewports.forEach(viewport -> viewport.update(width, height, true));
    }

    private boolean sessionEnded() {
        return sessionEntities.iterator()
                   .next()
                   .component(Session.class)
                   .status == Session.Status.ENDED;
    }
}
