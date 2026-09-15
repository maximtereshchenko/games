package com.github.maximtereshchenko.games.cookies.domain;

final class FrenzyBuff extends ActiveBuff {

    private final Configuration.FrenzyBuffConfiguration frenzyBuffConfiguration;

    FrenzyBuff(Configuration.FrenzyBuffConfiguration frenzyBuffConfiguration) {
        this.frenzyBuffConfiguration = frenzyBuffConfiguration;
    }

    @Override
    float durationSeconds() {
        return frenzyBuffConfiguration.baseDurationSeconds();
    }

    @Override
    BuffDescription buffDescription() {
        return new FrenzyDescription(
            frenzyBuffConfiguration.multiplier(),
            frenzyBuffConfiguration.baseDurationSeconds()
        );
    }
}
