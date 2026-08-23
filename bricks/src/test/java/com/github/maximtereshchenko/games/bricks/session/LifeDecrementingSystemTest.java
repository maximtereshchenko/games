package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class LifeDecrementingSystemTest {

    private final Registry registry = new Registry();
    private final LifeDecrementingSystem lifeDecrementingSystem =
        new LifeDecrementingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(lifeDecrementingSystem);
    }

    @Test
    void givenDecrementLivesCommand_thenLivesDecremented() {
        var lives = new Lives(3);
        registry.addComponents(registry.createEntity(), lives, DecrementLivesCommand.INSTANCE);
        registry.update(0);
        assertThat(lives.value).isEqualTo(2);
    }
}
