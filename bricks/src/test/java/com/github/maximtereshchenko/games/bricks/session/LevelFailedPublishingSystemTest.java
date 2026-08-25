package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelFailed;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class LevelFailedPublishingSystemTest {

    private final Registry registry = new Registry();
    private final EventBus<Event> eventBus = mock();
    private final LevelFailedPublishingSystem levelFailedPublishingSystem =
        new LevelFailedPublishingSystem(registry, eventBus);

    @BeforeEach
    void setUp() {
        registry.addSystems(levelFailedPublishingSystem);
    }

    @Test
    void givenZeroLives_thenLevelFailedPublished() {
        registry.addComponents(registry.createEntity(), new Lives(0));
        registry.update(0);
        verify(eventBus).publish(new LevelFailed());
    }

    @Test
    void givenLivesRemaining_thenLevelFailedNotPublished() {
        registry.addComponents(registry.createEntity(), new Lives(1));
        registry.update(0);
        verifyNoInteractions(eventBus);
    }
}
