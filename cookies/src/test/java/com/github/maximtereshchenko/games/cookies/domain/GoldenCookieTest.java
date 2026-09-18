package com.github.maximtereshchenko.games.cookies.domain;

import com.badlogic.gdx.Preferences;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class GoldenCookieTest {

    private final Configuration.GoldenCookieConfiguration configuration =
        new Configuration.GoldenCookieConfiguration(10, 10, 13, Map.of());
    private final Preferences preferences = mock();
    private final PlayerProgress playerProgress = new PlayerProgress(
        preferences,
        Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)
    );
    private final Random random = mock();
    private final GoldenCookie goldenCookie = new GoldenCookie(
        configuration,
        playerProgress,
        random
    );

    @Test
    void whenCreated_thenIntervalEmpty() {
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(0, 0));
    }

    @Test
    void givenCooldownNotElapsed_whenUpdate_thenGoldenCookieNotSpawned() {
        goldenCookie.update(9);
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(0, 0));
        verifyNoInteractions(random);
    }

    @Test
    void givenSpawnRollFails_whenUpdate_thenGoldenCookieNotSpawned() {
        when(random.nextDouble()).thenReturn(1.0);
        goldenCookie.update(21);
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(0, 0));
    }

    @Test
    void givenSpawnRollSucceeds_whenUpdate_thenGoldenCookieSpawned() {
        when(random.nextDouble()).thenReturn(0.5);
        goldenCookie.update(21);
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(13, 13));
    }

    @Test
    void givenSpawnWindowFullyElapsed_whenUpdate_thenGoldenCookieSpawned() {
        when(random.nextDouble()).thenReturn(1.0);
        goldenCookie.update(21);
        goldenCookie.update(1);
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(13, 13));
    }

    @Test
    void givenSpawnedGoldenCookie_whenUpdate_thenRemainingSecondsDecremented() {
        when(random.nextDouble()).thenReturn(0.5);
        goldenCookie.update(21);
        goldenCookie.update(3);
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(10, 13));
        verify(random, times(1)).nextDouble();
    }

    @Test
    void givenSpawnedGoldenCookie_whenReset_thenRemainingSecondsZero() {
        when(random.nextDouble()).thenReturn(0.5);
        goldenCookie.update(21);
        goldenCookie.reset();
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(0, 13));
    }

    @Test
    void givenGoldenCookieUpgrades_whenSpawned_thenDurationDoubledPerUpgrade() {
        playerProgress.activeUpgrades.add(Upgrade.GOLDEN_COOKIE_TIER_0);
        playerProgress.activeUpgrades.add(Upgrade.GOLDEN_COOKIE_TIER_1);
        when(random.nextDouble()).thenReturn(0.5);
        goldenCookie.update(6);
        assertThat(goldenCookie.interval())
            .isEqualTo(new Interval(52, 52));
    }
}
