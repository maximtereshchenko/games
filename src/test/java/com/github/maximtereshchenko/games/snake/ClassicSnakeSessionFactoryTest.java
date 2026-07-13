package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class ClassicSnakeSessionFactoryTest {

    private final Dominion dominion = mock();
    private final Scheduler scheduler = mock();
    private final SnakeSessionFactory snakeSessionFactory = new ClassicSnakeSessionFactory();

    @Test
    void whenMode_thenClassic() {
        assertThat(snakeSessionFactory.mode()).isEqualTo(Mode.CLASSIC);
    }

    @ParameterizedTest
    @EnumSource
    void givenOppositeDirection_whenSetCurrentDirection_thenFalse(Direction current) {
        assertThat(snakeSessionFactory.setCurrentDirection(current, current.opposite())).isFalse();
    }

    @Test
    void givenNotOppositeDirection_whenSetCurrentDirection_thenTrue() {
        assertThat(snakeSessionFactory.setCurrentDirection(Direction.RIGHT, Direction.UP)).isTrue();
    }

    @Test
    void givenShapeRenderer_thenSnakeSessionScreen() {
        try (MockedStatic<Dominion> dominionStatic = mockStatic(Dominion.class)) {
            dominionStatic.when(Dominion::create).thenReturn(dominion);
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
}