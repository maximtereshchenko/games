package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class ScreenFactoryTest {

    private final Dominion dominion = mock(Dominion.class);
    private final Scheduler scheduler = mock(Scheduler.class);
    private final SnakeSessionFactory snakeSessionFactory = new SnakeSessionFactory(() -> dominion);

    @Test
    void givenShapeRenderer_thenSnakeSessionScreen() {
        when(dominion.createScheduler()).thenReturn(scheduler);
        assertThat(
            snakeSessionFactory.snakeSession(
                new FitViewport(0, 0),
                mock(ShapeRenderer.class),
                new WorldDimensions(0, 0)
            )
        )
            .satisfies(
                snakeSession -> assertThat(snakeSession.dominion()).isEqualTo(dominion),
                snakeSession -> assertThat(snakeSession.scheduler()).isEqualTo(scheduler),
                snakeSession -> assertThat(snakeSession.standaloneRenderingSystem()).isNotNull()
            );
        verify(dominion, atLeastOnce()).createEntity(any());
        verify(scheduler, atLeastOnce()).schedule(any());
    }
}