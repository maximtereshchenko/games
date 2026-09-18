package com.github.maximtereshchenko.games.cookies.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class ActiveBuffTest {

    private final ActiveBuff activeBuff = new ActiveBuff();
    private final BuffEffect buffEffect = new FrenzyEffect(7);

    @Test
    void whenCreated_thenIntervalEmpty() {
        assertThat(activeBuff.interval())
            .isEqualTo(new Interval(0, 0));
    }

    @Test
    void whenCreated_thenBuffEffectAbsent() {
        assertThat(activeBuff.buffEffect()).isNull();
    }

    @Test
    void givenEmptyBuff_whenUpdate_thenRemainingSecondsStayZero() {
        activeBuff.update(1.5f);
        assertThat(activeBuff.interval())
            .isEqualTo(new Interval(0, 0));
    }

    @Test
    void whenReset_thenBuffEffectAndFullInterval() {
        activeBuff.reset(buffEffect, 10);
        assertThat(activeBuff.buffEffect()).isSameAs(buffEffect);
        assertThat(activeBuff.interval())
            .isEqualTo(new Interval(10, 10));
    }

    @Test
    void givenResetBuff_whenUpdate_thenRemainingSecondsDecremented() {
        activeBuff.reset(buffEffect, 10);
        activeBuff.update(2.5f);
        assertThat(activeBuff.interval())
            .isEqualTo(new Interval(7.5f, 10));
        assertThat(activeBuff.buffEffect()).isSameAs(buffEffect);
    }

    @Test
    void givenResetBuff_whenUpdateExceedsDuration_thenRemainingSecondsZero() {
        activeBuff.reset(buffEffect, 10);
        activeBuff.update(11);
        assertThat(activeBuff.interval())
            .isEqualTo(new Interval(0, 10));
    }

    @Test
    void givenResetBuff_whenResetAgain_thenPreviousEffectReplaced() {
        activeBuff.reset(buffEffect, 10);
        activeBuff.update(4);
        var nextEffect = new ClickFrenzyEffect(777);
        activeBuff.reset(nextEffect, 3);
        assertThat(activeBuff.buffEffect()).isSameAs(nextEffect);
        assertThat(activeBuff.interval())
            .isEqualTo(new Interval(3, 3));
    }
}
