package com.github.maximtereshchenko.games.cookies.screen.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class BigDecimalFormatter {

    private final DecimalFormat lowValueFormat = new DecimalFormat("0.##");
    private final DecimalFormat highValueFormat = new DecimalFormat("0.00E0");
    private final BigDecimal boundary = new BigDecimal(10000);

    public String string(BigDecimal value) {
        return decimalFormat(value).format(value);
    }

    private DecimalFormat decimalFormat(BigDecimal value) {
        if (value.compareTo(boundary) < 0) {
            return lowValueFormat;
        }
        return highValueFormat;
    }
}
