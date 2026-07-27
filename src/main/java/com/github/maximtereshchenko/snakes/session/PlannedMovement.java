package com.github.maximtereshchenko.snakes.session;

import java.util.Set;

final class PlannedMovement {

    final Set<RelativeDirection> legalRelativeDirections;
    Direction direction;

    PlannedMovement(Set<RelativeDirection> legalRelativeDirections, Direction direction) {
        this.legalRelativeDirections = legalRelativeDirections;
        this.direction = direction;
    }
}
