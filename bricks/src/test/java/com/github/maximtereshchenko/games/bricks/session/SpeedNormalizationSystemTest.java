package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SpeedNormalizationSystemTest {

    private final Registry registry = new Registry();
    private final SpeedNormalizationSystem speedNormalizationSystem =
        new SpeedNormalizationSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(speedNormalizationSystem);
    }

    @Test
    void givenSpeedAndVelocity_thenVelocityNormalizedToSpeed() {
        var velocity = new Velocity(new Vector2(3, 4));
        registry.addComponents(registry.createEntity(), new Speed(10), velocity);
        registry.update(0);
        assertThat(velocity.vector2()).usingRecursiveComparison().isEqualTo(new Vector2(6, 8));
    }
}
