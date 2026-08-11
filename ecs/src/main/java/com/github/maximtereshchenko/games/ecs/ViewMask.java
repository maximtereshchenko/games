package com.github.maximtereshchenko.games.ecs;

import java.util.BitSet;
import java.util.Objects;

final class ViewMask {

    private final BitSet allMask;
    private final BitSet oneMask;
    private final BitSet noneMask;

    ViewMask(BitSet allMask, BitSet oneMask, BitSet noneMask) {
        this.allMask = allMask;
        this.oneMask = oneMask;
        this.noneMask = noneMask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(allMask, oneMask, noneMask);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof ViewMask queryMask &&
               Objects.equals(allMask, queryMask.allMask) &&
               Objects.equals(oneMask, queryMask.oneMask) &&
               Objects.equals(noneMask, queryMask.noneMask);
    }

    boolean matches(BitSet mask) {
        return (allMask.isEmpty() || containsAllMask(mask)) &&
               (oneMask.isEmpty() || oneMask.intersects(mask)) &&
               (noneMask.isEmpty() || !noneMask.intersects(mask));
    }

    private boolean containsAllMask(BitSet mask) {
        for (
            var i = allMask.nextSetBit(0);
            i != -1;
            i = allMask.nextSetBit(i + 1)
        ) {
            if (!mask.get(i)) {
                return false;
            }
        }
        return true;
    }
}
