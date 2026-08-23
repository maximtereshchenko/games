package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class BrickCollisionSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> removedBrickEntities =
        registry.entities(new Query().all(Brick.class, Removed.class));
    private final BrickCollisionSystem brickCollisionSystem =
        new BrickCollisionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(brickCollisionSystem);
    }

    @Test
    void givenMutualCollision_thenBrickRemoved() {
        var ballId = registry.createEntity();
        var brickId = registry.createEntity();
        registry.addComponents(ballId, Ball.INSTANCE, new Collisions(Set.of(brickId)));
        registry.addComponents(brickId, Brick.INSTANCE, new Collisions(Set.of(ballId)));
        registry.update(0);
        assertThat(removedBrickEntities).hasSize(1);
    }

    @Test
    void givenOneSidedCollision_thenBrickNotRemoved() {
        var ballId = registry.createEntity();
        var brickId = registry.createEntity();
        registry.addComponents(ballId, Ball.INSTANCE, new Collisions(Set.of(brickId)));
        registry.addComponents(brickId, Brick.INSTANCE, new Collisions(Set.of()));
        registry.update(0);
        assertThat(removedBrickEntities).isEmpty();
    }
}
