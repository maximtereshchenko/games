package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelCompleted;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.common.event.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class LevelCompletedPublishingSystemTest {

    private final Registry registry = new Registry();
    private final EventBus<Event> eventBus = mock();
    private final LevelCompletedPublishingSystem levelCompletedPublishingSystem =
        new LevelCompletedPublishingSystem(registry, eventBus, "easy", 2);

    @BeforeEach
    void setUp() {
        registry.addSystems(levelCompletedPublishingSystem);
    }

    @Test
    void givenNoBricksOrStars_thenLevelCompletedPublished() {
        registry.addComponents(registry.createEntity(), new CollectedStars(3));
        registry.update(0);
        verify(eventBus).publish(new LevelCompleted("easy", 2, 3));
    }

    @Test
    void givenBrick_thenLevelCompletedNotPublished() {
        registry.addComponents(registry.createEntity(), Brick.INSTANCE);
        registry.addComponents(registry.createEntity(), new CollectedStars(1));
        registry.update(0);
        verifyNoInteractions(eventBus);
    }

    @Test
    void givenIncrementStarsBonus_thenLevelCompletedNotPublished() {
        registry.addComponents(registry.createEntity(), IncrementStarsBonus.INSTANCE);
        registry.addComponents(registry.createEntity(), new CollectedStars(1));
        registry.update(0);
        verifyNoInteractions(eventBus);
    }
}
