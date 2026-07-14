package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class ClassicSnakeSessionFactoryTest {

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
        try (
            var _ = mockStatic(Dominion.class);
            var dominionConstruction = mockConstruction(
                SameThreadDominion.class,
                (dominion, _) -> when(dominion.createScheduler()).thenReturn(scheduler)
            )
        ) {
            var snakeSession = snakeSessionFactory.snakeSession(
                new FitViewport(0, 0),
                mock(ShapeRenderer.class),
                new WorldDimensions(0, 0)
            );
            var dominion = dominionConstruction.constructed().getFirst();
            assertThat(snakeSession).isEqualTo(new SnakeSession(dominion, scheduler));
            verify(dominion, atLeastOnce()).createEntity(any());
            verify(scheduler, atLeastOnce()).schedule(any());
        }
    }
}