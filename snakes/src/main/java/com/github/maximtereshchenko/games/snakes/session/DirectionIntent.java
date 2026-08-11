package com.github.maximtereshchenko.games.snakes.session;

import java.util.Set;

final class DirectionIntent {

    final Set<RelativeDirection> legalRelativeDirections;
    Direction value;

    DirectionIntent(Set<RelativeDirection> legalRelativeDirections, Direction value) {
        this.legalRelativeDirections = legalRelativeDirections;
        this.value = value;
    }
}
