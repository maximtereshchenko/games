package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class BonusSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> bonusEntities =
        registry.entities(new Query().all(Bonus.class, WorldPosition.class));
    private final Blueprints blueprints =
        new Blueprints.Builder(Map.of("life", List.of(Bonus.INSTANCE))).build();
    private final Random random = mock();
    private final BonusSpawningSystem bonusSpawningSystem =
        new BonusSpawningSystem(registry, blueprints, random);

    @BeforeEach
    void setUp() {
        registry.addSystems(bonusSpawningSystem);
    }

    @Test
    void givenChanceAndWeight_thenBonusSpawnedAtBrickPosition() {
        when(random.nextFloat()).thenReturn(0.1f, 0.05f);
        registry.addComponents(
            registry.createEntity(),
            new BonusSpawnPolicy(0.5f, Map.of("life", 0.2f))
        );
        registry.addComponents(
            registry.createEntity(),
            Brick.INSTANCE,
            Removed.INSTANCE,
            new WorldPosition(new Vector2(3, 4))
        );
        registry.update(0);
        assertThat(bonusEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class).vector2())
            .usingRecursiveComparison()
            .isEqualTo(new Vector2(3, 4));
    }

    @Test
    void givenChanceMissed_thenBonusNotSpawned() {
        when(random.nextFloat()).thenReturn(0.9f);
        registry.addComponents(
            registry.createEntity(),
            new BonusSpawnPolicy(0.5f, Map.of("life", 1f))
        );
        registry.addComponents(
            registry.createEntity(),
            Brick.INSTANCE,
            Removed.INSTANCE,
            new WorldPosition(new Vector2(3, 4))
        );
        registry.update(0);
        assertThat(bonusEntities).isEmpty();
    }
}
