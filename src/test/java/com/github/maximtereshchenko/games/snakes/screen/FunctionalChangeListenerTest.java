package com.github.maximtereshchenko.games.snakes.screen;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

final class FunctionalChangeListenerTest {

    private final AtomicBoolean changed = new AtomicBoolean(false);
    private final FunctionalChangeListener functionalChangeListener = new FunctionalChangeListener(
        () -> changed.set(true)
    );

    @Test
    void givenChanged_thenRunnableRun() {
        functionalChangeListener.changed(null, null);
        assertThat(changed).isTrue();
    }
}
