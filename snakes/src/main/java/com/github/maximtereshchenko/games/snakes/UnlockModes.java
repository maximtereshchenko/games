package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.event.Subscriber;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import com.github.maximtereshchenko.games.snakes.configuration.ModeUnlockRequirements;
import com.github.maximtereshchenko.games.snakes.event.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

final class UnlockModes implements Subscriber<ApplicationEvent> {

    private final UserProfile userProfile;
    private final List<Mode> modes;

    UnlockModes(UserProfile userProfile, List<Mode> modes) {
        this.userProfile = userProfile;
        this.modes = modes;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent) {
            case SessionEnded sessionEnded -> unlock(
                modeUnlockRequirements -> isSatisfied(modeUnlockRequirements, sessionEnded)
            );
            case TitleScreenFinished _, CreditsScreenFinished _ -> unlock(
                this::isSatisfied
            );
            default -> {
                //empty
            }
        }
    }

    private boolean isSatisfied(ModeUnlockRequirements modeUnlockRequirements) {
        return userProfileThresholdsSatisfied(modeUnlockRequirements) &&
               modeUnlockRequirements.sessionThresholds().isEmpty();
    }

    private boolean isSatisfied(
        ModeUnlockRequirements modeUnlockRequirements,
        SessionEnded sessionEnded
    ) {
        return userProfileThresholdsSatisfied(modeUnlockRequirements) &&
               isSatisfied(
                   modeUnlockRequirements.sessionThresholds(),
                   sessionEnded.statistics()::get
               );
    }

    private void unlock(Predicate<ModeUnlockRequirements> predicate) {
        for (var mode : modes) {
            if (predicate.test(mode.modeUnlockRequirements())) {
                userProfile.unlock(mode);
            }
        }
    }

    private boolean userProfileThresholdsSatisfied(
        ModeUnlockRequirements modeUnlockRequirements
    ) {
        return isSatisfied(
            modeUnlockRequirements.userProfileThresholds(),
            userProfile::value
        );
    }

    private <T> boolean isSatisfied(
        Map<T, Integer> statistics,
        Function<T, Integer> function
    ) {
        return statistics.entrySet()
            .stream()
            .allMatch(entry -> function.apply(entry.getKey()) >= entry.getValue());
    }
}
