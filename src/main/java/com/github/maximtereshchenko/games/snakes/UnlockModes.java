package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.event.Subscriber;
import com.github.maximtereshchenko.games.snakes.event.TitleScreenFinished;

import java.util.List;
import java.util.function.Predicate;

final class UnlockModes implements Subscriber {

    private final UserProfile userProfile;
    private final List<Mode> modes;

    UnlockModes(UserProfile userProfile, List<Mode> modes) {
        this.userProfile = userProfile;
        this.modes = modes;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent) {
            case SnakeSessionEnded snakeSessionEnded -> unlock(
                modeUnlockRequirements -> modeUnlockRequirements.isSatisfied(snakeSessionEnded)
            );
            case TitleScreenFinished _ -> unlock(ModeUnlockRequirements::isSatisfied);
            default -> {
                //empty
            }
        }
    }

    private void unlock(Predicate<ModeUnlockRequirements> predicate) {
        for (var mode : modes) {
            if (predicate.test(mode.modeUnlockRequirements())) {
                userProfile.unlock(mode);
            }
        }
    }
}
