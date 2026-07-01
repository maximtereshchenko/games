package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class ScreenFactoryTest {

    private final Dominion dominion = mock(Dominion.class);
    private final Scheduler scheduler = mock(Scheduler.class);
    private final ScreenFactory screenFactory = new ScreenFactory(() -> dominion);

    @Test
    void givenShapeRenderer_thenSnakeSessionScreen() {
        when(dominion.createScheduler()).thenReturn(scheduler);
        assertThat(screenFactory.snakeSessionScreen(mock(ShapeRenderer.class), () -> {}))
            .isNotNull();
        verify(dominion, atLeastOnce()).createEntity(any());
        verify(scheduler, atLeastOnce()).schedule(any());
    }
}