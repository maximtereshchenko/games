package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class ViperSnakeSessionFactoryTest {

    private final Dominion dominion = mock();
    private final SnakeSessionFactory snakeSessionFactory = new ViperSnakeSessionFactory();

    @Test
    void whenMode_thenViper() {
        assertThat(snakeSessionFactory.mode()).isEqualTo(Mode.VIPER);
    }

    @ParameterizedTest
    @EnumSource
    void givenRightDirection_whenSetCurrentDirection_thenTrue(Direction current) {
        assertThat(snakeSessionFactory.setCurrentDirection(current, current.right())).isTrue();
    }

    @Test
    void givenNotRightDirection_whenSetCurrentDirection_thenFalse() {
        assertThat(snakeSessionFactory.setCurrentDirection(Direction.RIGHT, Direction.UP)).isFalse();
    }

    @Test
    void givenShapeRenderer_thenSnakeSessionScreen() {
        try (var dominionStatic = mockStatic(Dominion.class)) {
            dominionStatic.when(Dominion::create).thenReturn(dominion);
            var snakeSession = snakeSessionFactory.snakeSession(
                new FitViewport(0, 0),
                mock(ShapeRenderer.class),
                new WorldDimensions(0, 0)
            );
            assertThat(snakeSession.dominion()).isEqualTo(dominion);
            assertThat(snakeSession.systems()).isNotEmpty();
            verify(dominion, atLeastOnce()).createEntity(any());
        }
    }
}