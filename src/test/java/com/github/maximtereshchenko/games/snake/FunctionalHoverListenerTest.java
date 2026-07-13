package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

final class FunctionalHoverListenerTest {

    private final AtomicBoolean hovered = new AtomicBoolean(false);
    private final FunctionalHoverListener functionalHoverListener = new FunctionalHoverListener(
        () -> hovered.set(true)
    );

    @Test
    void givenEnter_thenRunnableRun() {
        functionalHoverListener.enter(null, 0, 0, 0, null);
        assertThat(hovered).isTrue();
    }
}
