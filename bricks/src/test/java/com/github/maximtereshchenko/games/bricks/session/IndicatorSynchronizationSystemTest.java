package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.screen.view.Indicator;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class IndicatorSynchronizationSystemTest {

    private final Registry registry = new Registry();
    private final Indicator indicator = mock();
    private final IndicatorSynchronizationSystem<Lives> indicatorSynchronizationSystem =
        new IndicatorSynchronizationSystem<>(
            registry,
            indicator,
            Lives.class,
            lives -> lives.value
        );

    @BeforeEach
    void setUp() {
        registry.addSystems(indicatorSynchronizationSystem);
    }

    @Test
    void givenLives_thenIndicatorUpdated() {
        registry.addComponents(registry.createEntity(), new Lives(4));
        registry.update(0);
        verify(indicator).update(4);
    }
}
