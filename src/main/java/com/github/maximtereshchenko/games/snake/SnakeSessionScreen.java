package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

final class SnakeSessionScreen extends ScreenAdapter {

    private final Set<Viewport> viewports;
    private final ApplicationEvents applicationEvents;
    private final Dominion dominion;
    private final List<System> systems;

    SnakeSessionScreen(
        Set<Viewport> viewports,
        ApplicationEvents applicationEvents,
        Dominion dominion,
        List<System> systems
    ) {
        this.viewports = viewports;
        this.applicationEvents = applicationEvents;
        this.dominion = dominion;
        this.systems = systems;
    }

    @Override
    public void render(float delta) {
        if (value(Session.class, session -> session.status == Session.Status.ENDED, false)) {
            applicationEvents.publish(
                new SnakeSessionEnded(
                    value(
                        LeftTurnsCounter.class,
                        leftTurns -> leftTurns.value,
                        0
                    )
                )
            );
            return;
        }
        systems.forEach(system -> system.run(delta));
    }

    @Override
    public void resize(int width, int height) {
        viewports.forEach(viewport -> viewport.update(width, height, true));
    }

    private <T, R> R value(Class<T> type, Function<T, R> function, R defaultValue) {
        for (var component : dominion.findCompositionsWith(type)) {
            return function.apply(component);
        }
        return defaultValue;
    }
}
