package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

final class FoodSpawningSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final EntityFactory entityFactory;
    private final Random random;
    private final int maxFood;

    FoodSpawningSystem(
        Dominion dominion, EntityFactory entityFactory,
        Random random,
        int maxFood
    ) {
        super(dominion);
        this.dominion = dominion;
        this.entityFactory = entityFactory;
        this.random = random;
        this.maxFood = maxFood;
    }

    @Override
    void onTurnStarted() {
        var positions = dominion.findEntitiesWith(Position.class)
            .stream()
            .filter(results -> !results.entity().has(Background.class))
            .map(Results.With1::comp)
            .collect(Collectors.toSet());
        for (var worldDimensions : dominion.findCompositionsWith(WorldDimensions.class)) {
            for (var i = currentFood(); positions.size() < worldDimensions.space() && i < maxFood; i++) {
                var position = position(positions, worldDimensions);
                positions.add(position);
                entityFactory.createFood(dominion, position);
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

    private long currentFood() {
        return dominion.findEntitiesWith(Food.class).stream().count();
    }
}
