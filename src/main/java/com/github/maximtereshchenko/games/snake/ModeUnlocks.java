package com.github.maximtereshchenko.games.snake;

import java.util.Map;
import java.util.function.Predicate;

final class ModeUnlocks implements Subscriber {

    private final UserProfile userProfile;
    private final Map<Mode, Predicate<SnakeSessionEnded>> requirements;

    ModeUnlocks(UserProfile userProfile, Map<Mode, Predicate<SnakeSessionEnded>> requirements) {
        this.userProfile = userProfile;
        this.requirements = requirements;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        if (!(applicationEvent instanceof SnakeSessionEnded snakeSessionEnded)) {
            return;
        }
        for (var entry : requirements.entrySet()) {
            if (entry.getValue().test(snakeSessionEnded)) {
                userProfile.unlock(entry.getKey());
            }
        }
    }
}
