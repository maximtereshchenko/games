package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class BricksBlueprintsTest {

    @Test
    void givenMutableComponents_thenCopiesCreated() {
        var rectangle = new Rectangle(1, 2);
        var velocity = new Velocity(new Vector2(3, 4));
        var worldPosition = new WorldPosition(new Vector2(5, 6));
        var spawnedStars = new SpawnedStars(1);
        var collectedStars = new CollectedStars(2);
        var lives = new Lives(3);
        var resetWidthRemainingTime = new ResetWidthRemainingTime(4);
        var blueprints = new BricksBlueprints()
            .blueprints(
                Map.of(
                    "item",
                    List.of(
                        rectangle,
                        velocity,
                        worldPosition,
                        spawnedStars,
                        collectedStars,
                        lives,
                        resetWidthRemainingTime
                    )
                )
            );
        var components = blueprints.components("item");
        assertThat(components[0]).isNotSameAs(rectangle);
        assertThat(((Velocity) components[1]).vector2()).isNotSameAs(velocity.vector2());
        assertThat(((WorldPosition) components[2]).vector2()).isNotSameAs(worldPosition.vector2());
        assertThat(components[3]).isNotSameAs(spawnedStars);
        assertThat(components[4]).isNotSameAs(collectedStars);
        assertThat(components[5]).isNotSameAs(lives);
        assertThat(components[6]).isNotSameAs(resetWidthRemainingTime);
        assertThat(components)
            .usingRecursiveComparison()
            .isEqualTo(
                new Object[]{
                    new Rectangle(1, 2),
                    new Velocity(new Vector2(3, 4)),
                    new WorldPosition(new Vector2(5, 6)),
                    new SpawnedStars(1),
                    new CollectedStars(2),
                    new Lives(3),
                    new ResetWidthRemainingTime(4)
                }
            );
    }
}
