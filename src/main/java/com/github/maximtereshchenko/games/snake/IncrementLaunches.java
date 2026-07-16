package com.github.maximtereshchenko.games.snake;

final class IncrementLaunches implements Subscriber {

    private final UserProfile userProfile;

    IncrementLaunches(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof TitleScreenFinished) {
            userProfile.update(
                UserProfileStatistics.LAUNCHES,
                userProfile.value(UserProfileStatistics.LAUNCHES) + 1
            );
        }
    }
}
