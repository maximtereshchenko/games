package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class WidthResettingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.entities(new Query().all(UpdateWidthCommand.class));
    private final WidthResettingSystem widthResettingSystem =
        new WidthResettingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(widthResettingSystem);
    }

    @Test
    void givenRemainingTime_thenTimeDecreasedWithoutReset() {
        var remainingTime = new ResetWidthRemainingTime(2);
        registry.addComponents(
            registry.createEntity(),
            remainingTime,
            new BaseHalfWidth(5)
        );
        registry.update(1);
        assertThat(remainingTime.seconds).isEqualTo(1);
        assertThat(commandEntities).isEmpty();
    }

    @Test
    void givenTimeExpired_thenBaseHalfWidthCommandAdded() {
        registry.addComponents(
            registry.createEntity(),
            new ResetWidthRemainingTime(0.5f),
            new BaseHalfWidth(5)
        );
        registry.update(1);
        assertThat(commandEntities)
            .singleElement()
            .extracting(entity -> entity.component(UpdateWidthCommand.class))
            .isEqualTo(new UpdateWidthCommand(5));
    }
}
