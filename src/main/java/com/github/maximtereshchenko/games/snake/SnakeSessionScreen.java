package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.dominion.ecs.api.Dominion;

import java.util.List;
import java.util.function.Function;

final class SnakeSessionScreen extends ScreenAdapter {

    private final Viewport viewport;
    private final ApplicationEvents applicationEvents;
    private final Dominion dominion;
    private final List<System> systems;

    SnakeSessionScreen(
        Viewport viewport,
        ApplicationEvents applicationEvents,
        Dominion dominion,
        List<System> systems
    ) {
        this.viewport = viewport;
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
                        LeftTurns.class,
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
        viewport.update(width, height, true);
    }

    private <T, R> R value(Class<T> type, Function<T, R> function, R defaultValue) {
        for (var component : dominion.findCompositionsWith(type)) {
            return function.apply(component);
        }
        return defaultValue;
    }
}
