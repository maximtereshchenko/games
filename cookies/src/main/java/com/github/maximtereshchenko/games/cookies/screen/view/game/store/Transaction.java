package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.TransactionMode;

import java.math.BigDecimal;

final class Transaction {

    private final BakeryService bakeryService;
    private TransactionMode mode;
    private int amount;

    Transaction(BakeryService bakeryService) {
        this.bakeryService = bakeryService;
        this.mode = TransactionMode.BUY;
        this.amount = 1;
    }

    void update(TransactionMode mode, int amount) {
        this.mode = mode;
        this.amount = amount;
    }

    TransactionMode mode() {
        return mode;
    }

    BigDecimal value(Building building) {
        return bakeryService.transactionValue(
            building,
            mode,
            amount
        );
    }

    boolean canAfford(Building building) {
        return bakeryService.canAfford(
            building,
            mode,
            amount
        );
    }

    void complete(Building building) {
        bakeryService.completeTransaction(
            building,
            mode,
            amount
        );
    }
}
