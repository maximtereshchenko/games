package com.github.maximtereshchenko.games.cookies.domain;

import com.badlogic.gdx.Preferences;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class PlayerProgressTest {

    private final Preferences preferences = mock();
    private final Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

    @Test
    void givenEmptyPreferences_whenCreated_thenDefaults() {
        when(preferences.getFloat("volume", 0.75f)).thenReturn(0.75f);
        var playerProgress = new PlayerProgress(preferences, clock);
        assertThat(playerProgress.createdTimestamp).isEqualTo(Instant.EPOCH);
        assertThat(playerProgress.lastFlushTimestamp).isEqualTo(Instant.EPOCH);
        assertThat(playerProgress.balance).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(playerProgress.cumulativeBaked).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(playerProgress.cumulativeManuallyBaked)
            .isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(playerProgress.cumulativeClicks).isZero();
        assertThat(playerProgress.cumulativeGoldenCookies).isZero();
        assertThat(playerProgress.volume).isEqualTo(0.75f);
        assertThat(playerProgress.buildingCounts)
            .containsOnlyKeys(Building.values())
            .containsValue(0);
        assertThat(playerProgress.unlockedUpgrades).isEmpty();
        assertThat(playerProgress.activeUpgrades).isEmpty();
        assertThat(playerProgress.unlockedAchievements).isEmpty();
    }

    @Test
    void givenPreferences_whenCreated_thenValuesFromPreferences() {
        when(preferences.contains("created-timestamp")).thenReturn(true);
        when(preferences.getString("created-timestamp"))
            .thenReturn("1970-01-01T00:00:01Z");
        when(preferences.contains("last-updated-timestamp")).thenReturn(true);
        when(preferences.getString("last-updated-timestamp"))
            .thenReturn("1970-01-01T00:00:02Z");
        when(preferences.contains("balance")).thenReturn(true);
        when(preferences.getString("balance")).thenReturn("10");
        when(preferences.contains("cumulative-baked")).thenReturn(true);
        when(preferences.getString("cumulative-baked")).thenReturn("20");
        when(preferences.contains("cumulative-manually-baked")).thenReturn(true);
        when(preferences.getString("cumulative-manually-baked")).thenReturn("3");
        when(preferences.getLong("cumulative-clicks")).thenReturn(4L);
        when(preferences.getInteger("cumulative-clicks")).thenReturn(5);
        when(preferences.getFloat("volume", 0.75f)).thenReturn(0.25f);
        when(preferences.getInteger("buildings.CURSOR.count")).thenReturn(6);
        when(preferences.getBoolean("upgrades.CLICK_TIER_0.unlocked")).thenReturn(true);
        when(preferences.getBoolean("upgrades.CLICK_TIER_1.active")).thenReturn(true);
        when(preferences.getBoolean("achievements.MATHEMATICIAN.unlocked"))
            .thenReturn(true);
        var playerProgress = new PlayerProgress(preferences, clock);
        assertThat(playerProgress.createdTimestamp)
            .isEqualTo(Instant.parse("1970-01-01T00:00:01Z"));
        assertThat(playerProgress.lastFlushTimestamp)
            .isEqualTo(Instant.parse("1970-01-01T00:00:02Z"));
        assertThat(playerProgress.balance).isEqualByComparingTo("10");
        assertThat(playerProgress.cumulativeBaked).isEqualByComparingTo("20");
        assertThat(playerProgress.cumulativeManuallyBaked).isEqualByComparingTo("3");
        assertThat(playerProgress.cumulativeClicks).isEqualTo(4L);
        assertThat(playerProgress.cumulativeGoldenCookies).isEqualTo(5);
        assertThat(playerProgress.volume).isEqualTo(0.25f);
        assertThat(playerProgress.buildingCounts).containsEntry(Building.CURSOR, 6);
        assertThat(playerProgress.unlockedUpgrades).containsExactly(Upgrade.CLICK_TIER_0);
        assertThat(playerProgress.activeUpgrades).containsExactly(Upgrade.CLICK_TIER_1);
        assertThat(playerProgress.unlockedAchievements)
            .containsExactly(Achievement.MATHEMATICIAN);
    }

    @Test
    void whenFlush_thenPreferencesClearedAndWritten() {
        when(preferences.getFloat("volume", 0.75f)).thenReturn(0.5f);
        var playerProgress = new PlayerProgress(preferences, clock);
        playerProgress.buildingCounts.put(Building.GRANDMA, 2);
        playerProgress.unlockedUpgrades.add(Upgrade.CLICK_TIER_0);
        playerProgress.activeUpgrades.add(Upgrade.CLICK_TIER_1);
        playerProgress.unlockedAchievements.add(Achievement.BASE_10);
        playerProgress.balance = new BigDecimal("11");
        playerProgress.cumulativeBaked = new BigDecimal("22");
        playerProgress.cumulativeManuallyBaked = new BigDecimal("33");
        playerProgress.cumulativeClicks = 44;
        playerProgress.cumulativeGoldenCookies = 55;
        playerProgress.volume = 0.1f;
        playerProgress.flush();
        assertThat(playerProgress.lastFlushTimestamp).isEqualTo(Instant.EPOCH);
        verify(preferences).clear();
        verify(preferences).putInteger("buildings.GRANDMA.count", 2);
        verify(preferences).putInteger("buildings.CURSOR.count", 0);
        verify(preferences).putBoolean("upgrades.CLICK_TIER_0.unlocked", true);
        verify(preferences).putBoolean("upgrades.CLICK_TIER_1.active", true);
        verify(preferences).putBoolean("achievements.BASE_10.unlocked", true);
        verify(preferences).putString("created-timestamp", Instant.EPOCH.toString());
        verify(preferences).putString(
            "last-updated-timestamp",
            Instant.EPOCH.toString()
        );
        verify(preferences).putString("balance", "11");
        verify(preferences).putString("cumulative-baked", "22");
        verify(preferences).putString("cumulative-manually-baked", "33");
        verify(preferences).putLong("cumulative-clicks", 44);
        verify(preferences).putInteger("cumulative-golden-cookies", 55);
        verify(preferences).putFloat("volume", 0.1f);
        verify(preferences).flush();
    }
}
