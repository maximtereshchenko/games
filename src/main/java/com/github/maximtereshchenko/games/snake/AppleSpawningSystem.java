package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

final class AppleSpawningSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final Random random;
    private final int maxApples;

    AppleSpawningSystem(
        Dominion dominion,
        Random random,
        int maxApples
    ) {
        super(dominion);
        this.dominion = dominion;
        this.random = random;
        this.maxApples = maxApples;
    }

    @Override
    void onTurnStarted() {
        var positions = dominion.findCompositionsWith(Position.class)
            .stream()
            .collect(Collectors.toSet());
        for (var worldDimensions : dominion.findCompositionsWith(WorldDimensions.class)) {
            for (var i = currentApples(); positions.size() < worldDimensions.space() && i < maxApples; i++) {
                var position = position(positions, worldDimensions);
                positions.add(position);
                dominion.createEntity(Apple.INSTANCE, position, new Visible(Colors.APPLE));
            }
        }
    }

    private Position position(Set<Position> positions, WorldDimensions worldDimensions) {
        while (true) {
            var position = new Position(
                random.nextInt(worldDimensions.width()),
                random.nextInt(worldDimensions.height())
            );
            if (!positions.contains(position)) {
                return position;
            }
        }
    }

    private long currentApples() {
        return dominion.findEntitiesWith(Apple.class).stream().count();
    }
}
