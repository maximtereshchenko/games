package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

final class FunctionalInputListenerTest {

    private final AtomicBoolean keyDown = new AtomicBoolean(false);
    private final FunctionalInputListener functionalInputListener = new FunctionalInputListener(
        Input.Keys.A,
        () -> keyDown.set(true)
    );

    @Test
    void givenKeyEquals_thenRunnableRun() {
        assertThat(functionalInputListener.keyDown(null, Input.Keys.A)).isTrue();
        assertThat(keyDown).isTrue();
    }

    @Test
    void givenKeyNotEqual_thenFalse() {
        assertThat(functionalInputListener.keyDown(null, Input.Keys.B)).isFalse();
        assertThat(keyDown).isFalse();
    }
}