package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class PaddleShorteningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.view(new Query().all(UpdateWidthCommand.class));
    private final PaddleShorteningSystem paddleShorteningSystem =
        new PaddleShorteningSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(paddleShorteningSystem);
    }

    @Test
    void givenActivatedShortenPaddleBonus_thenWidthDecreased() {
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new Rectangle(5, 0.5f),
            new MinHalfWidth(2),
            new ResetWidthRemainingTime(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new ShortenPaddleBonus(1, 2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(commandEntities)
            .singleElement()
            .extracting(entity -> entity.component(UpdateWidthCommand.class))
            .isEqualTo(new UpdateWidthCommand(4));
    }

    @Test
    void givenShortenBelowMin_thenClampedToMinHalfWidth() {
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new Rectangle(3, 0.5f),
            new MinHalfWidth(2),
            new ResetWidthRemainingTime(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new ShortenPaddleBonus(5, 1),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(commandEntities)
            .singleElement()
            .extracting(entity -> entity.component(UpdateWidthCommand.class))
            .isEqualTo(new UpdateWidthCommand(2));
    }
}
