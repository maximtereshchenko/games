package com.github.maximtereshchenko.snakes.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.snakes.event.SessionEnded;
import com.github.maximtereshchenko.snakes.session.Dead;
import com.github.maximtereshchenko.snakes.session.Statistics;

import java.util.Set;

final class SessionScreen extends ScreenAdapter {

    private final Set<Viewport> viewports;
    private final ApplicationEvents applicationEvents;
    private final World world;
    private final Iterable<Entity> deadEntities;
    private final Iterable<Entity> statisticsEntities;

    SessionScreen(
        Set<Viewport> viewports,
        ApplicationEvents applicationEvents,
        World world
    ) {
        this.viewports = viewports;
        this.applicationEvents = applicationEvents;
        this.world = world;
        this.deadEntities = world.entities(
            new Query().all(Dead.class)
        );
        this.statisticsEntities = world.entities(
            new Query().all(Statistics.class)
        );
    }

    @Override
    public void render(float delta) {
        world.update(delta);
        for (var _ : deadEntities) {
            for (var statisticsEntity : statisticsEntities) {
                applicationEvents.publish(
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
}
