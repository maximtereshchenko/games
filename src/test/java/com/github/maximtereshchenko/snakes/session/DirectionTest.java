package com.github.maximtereshchenko.snakes.session;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

final class DirectionTest {

    private static Stream<Arguments> relativeDirections() {
        return Stream.concat(
            Stream.of(Direction.values())
                .map(direction -> arguments(direction, RelativeDirection.LEFT, direction.left())),
            Stream.of(Direction.values())
                .map(direction -> arguments(direction, RelativeDirection.RIGHT, direction.right()))
        );
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, DOWN
                    DOWN, UP
                    LEFT, RIGHT
                    RIGHT, LEFT
                    """
    )
    void whenOpposite_thenOppositeDirection(Direction direction, Direction expected) {
        assertThat(direction.opposite()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, LEFT
                    DOWN, RIGHT
                    LEFT, DOWN
                    RIGHT, UP
                    """
    )
    void whenLeft_thenLeftDirection(Direction direction, Direction expected) {
        assertThat(direction.left()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, RIGHT
                    DOWN, LEFT
                    LEFT, UP
                    RIGHT, DOWN
                    """
    )
    void whenRight_thenRightDirection(Direction direction, Direction expected) {
        assertThat(direction.right()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("relativeDirections")
    void whenRelative_thenRelativeDirection(
        Direction direction,
        RelativeDirection relativeDirection,
        Direction expected
    ) {
        assertThat(direction.relative(relativeDirection)).isEqualTo(expected);
    }
}