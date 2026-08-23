package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class LifeIncrementingSystemTest {

    private final Registry registry = new Registry();
    private final LifeIncrementingSystem lifeIncrementingSystem =
        new LifeIncrementingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(lifeIncrementingSystem);
    }

    @Test
    void givenActivatedIncrementLivesBonus_thenLivesIncremented() {
        var lives = new Lives(2);
        registry.addComponents(registry.createEntity(), lives);
        registry.addComponents(
            registry.createEntity(),
            IncrementLivesBonus.INSTANCE,
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(lives.value).isEqualTo(3);
    }
}
