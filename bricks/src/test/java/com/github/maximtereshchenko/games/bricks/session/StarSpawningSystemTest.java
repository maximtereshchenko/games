package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
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

final class StarSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> starEntities =
        registry.view(new Query().all(IncrementStarsBonus.class, WorldPosition.class));
    private final Configuration configuration = mock();
    private final Blueprints blueprints =
        new Blueprints.Builder(
            Map.of(BricksBlueprints.INCREMENT_STARS_BONUS, List.of(IncrementStarsBonus.INSTANCE))
        ).build();
    private final Random random = mock();
    private final StarSpawningSystem starSpawningSystem =
        new StarSpawningSystem(registry, configuration, blueprints, random);

    @BeforeEach
    void setUp() {
        when(configuration.maxStars()).thenReturn(3);
        registry.addSystems(starSpawningSystem);
    }

    @Test
    void givenChance_thenStarSpawned() {
        when(random.nextFloat()).thenReturn(0f);
        var spawnedStars = new SpawnedStars(0);
        registry.addComponents(registry.createEntity(), spawnedStars);
        registry.addComponents(
            registry.createEntity(),
            Brick.INSTANCE,
            Removed.INSTANCE,
            new WorldPosition(new Vector2(1, 2))
        );
        registry.update(0);
        assertThat(starEntities).hasSize(1);
        assertThat(spawnedStars.accumulated).isEqualTo(1);
    }

    @Test
    void givenMaxStarsReached_thenStarNotSpawned() {
        when(random.nextFloat()).thenReturn(0f);
        registry.addComponents(registry.createEntity(), new SpawnedStars(3));
        registry.addComponents(
            registry.createEntity(),
            Brick.INSTANCE,
            Removed.INSTANCE,
            new WorldPosition(new Vector2(1, 2))
        );
        registry.update(0);
        assertThat(starEntities).isEmpty();
    }
}
