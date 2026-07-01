package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;

import java.util.concurrent.TimeUnit;

final class SnakeSessionScreen extends ScreenAdapter {

    private final FitViewport fitViewport;
    private final Dominion dominion;
    private final Scheduler scheduler;
    private final StandaloneRenderingSystem standaloneRenderingSystem;
    private final Runnable onSessionEnd;

    SnakeSessionScreen(
        FitViewport fitViewport,
        Dominion dominion,
        Scheduler scheduler,
        StandaloneRenderingSystem standaloneRenderingSystem,
        Runnable onSessionEnd
    ) {
        this.fitViewport = fitViewport;
        this.dominion = dominion;
        this.scheduler = scheduler;
        this.standaloneRenderingSystem = standaloneRenderingSystem;
        this.onSessionEnd = onSessionEnd;
    }

    @Override
    public void render(float delta) {
        for (var game : dominion.findCompositionsWith(Session.class)) {
            if (game.status == Session.Status.ENDED) {
                onSessionEnd.run();
                return;
            }
        }
        scheduler.tick((long) (TimeUnit.SECONDS.toNanos(1) * delta));
        standaloneRenderingSystem.render();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
    }

    @Override
    public void hide() {
        scheduler.shutDown();
    }
}
