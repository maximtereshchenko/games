package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class PaddleWideningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.entities(new Query().all(UpdateWidthCommand.class));
    private final PaddleWideningSystem paddleWideningSystem =
        new PaddleWideningSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(paddleWideningSystem);
    }

    @Test
    void givenActivatedWidenPaddleBonus_thenWidthIncreasedAndTimeExtended() {
        var remainingTime = new ResetWidthRemainingTime(1);
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new Rectangle(5, 0.5f),
            new MaxHalfWidth(10),
            remainingTime
        );
        registry.addComponents(
            registry.createEntity(),
            new WidenPaddleBonus(3, 2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(remainingTime.seconds).isEqualTo(3);
        assertThat(commandEntities)
            .singleElement()
            .extracting(entity -> entity.component(UpdateWidthCommand.class))
            .isEqualTo(new UpdateWidthCommand(8));
    }

    @Test
    void givenWidenBeyondMax_thenClampedToMaxHalfWidth() {
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new Rectangle(9, 0.5f),
            new MaxHalfWidth(10),
            new ResetWidthRemainingTime(0)
        );
        registry.addComponents(
            registry.createEntity(),
            new WidenPaddleBonus(5, 1),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(commandEntities)
            .singleElement()
            .extracting(entity -> entity.component(UpdateWidthCommand.class))
            .isEqualTo(new UpdateWidthCommand(10));
    }
}
