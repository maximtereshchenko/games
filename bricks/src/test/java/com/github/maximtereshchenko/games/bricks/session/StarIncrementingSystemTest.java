package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class StarIncrementingSystemTest {

    private final Registry registry = new Registry();
    private final StarIncrementingSystem starIncrementingSystem =
        new StarIncrementingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(starIncrementingSystem);
    }

    @Test
    void givenActivatedIncrementStarsBonus_thenStarsIncremented() {
        var collectedStars = new CollectedStars(1);
        registry.addComponents(registry.createEntity(), collectedStars);
        registry.addComponents(
            registry.createEntity(),
            IncrementStarsBonus.INSTANCE,
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(collectedStars.value).isEqualTo(2);
    }
}
