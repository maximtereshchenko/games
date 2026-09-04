package com.github.maximtereshchenko.games.cookies.domain;

public sealed interface Event
    permits CookieBalanceUpdated,
    BakingRateUpdated,
    BakingPowerUpdated,
    BuildingUnlocked,
    TransactionModeUpdated,
    TransactionValueUpdated,
    BuildingCountUpdated,
    CookiesBaked,
    UpgradeUnlocked,
    UpgradePriceUpdated{}
