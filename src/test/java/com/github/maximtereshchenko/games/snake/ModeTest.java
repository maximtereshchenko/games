package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

final class ModeTest {

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenOppositeDirection_whenClassicIsLegal_thenFalse(Direction direction) {
        assertThat(Mode.CLASSIC.isLegal(direction, direction.opposite())).isFalse();
    }

    @Test
    void givenNonOppositeDirection_whenClassicIsLegal_thenTrue() {
        assertThat(Mode.CLASSIC.isLegal(Direction.UP, Direction.RIGHT)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void givenRightDirection_whenViperIsLegal_thenTrue(Direction direction) {
        assertThat(Mode.VIPER.isLegal(direction, direction.right())).isTrue();
    }

    @Test
    void givenNonRightDirection_whenViperIsLegal_thenFalse() {
        assertThat(Mode.VIPER.isLegal(Direction.UP, Direction.LEFT)).isFalse();
    }
}