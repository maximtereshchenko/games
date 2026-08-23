package com.github.maximtereshchenko.games.bricks.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class BlueprintsTest {

    @Test
    void givenCopyFunction_thenCopiedComponentReturned() {
        var original = new Mutable(1);
        var blueprints = new Blueprints.Builder(Map.of("item", List.of(original)))
            .copyFunction(Mutable.class, mutable -> new Mutable(mutable.value))
            .build();
        var components = blueprints.components("item");
        assertThat(components).hasSize(1);
        assertThat(components[0]).isNotSameAs(original);
        assertThat(components[0]).usingRecursiveComparison().isEqualTo(new Mutable(1));
    }

    @Test
    void givenNoCopyFunction_thenSameComponentReturned() {
        var original = new Mutable(1);
        var blueprints = new Blueprints.Builder(Map.of("item", List.of(original))).build();
        assertThat(blueprints.components("item")[0]).isSameAs(original);
    }

    @Test
    void givenExtraComponents_thenAppended() {
        var blueprints = new Blueprints.Builder(Map.of("item", List.of("first"))).build();
        assertThat(blueprints.components("item", "second")).containsExactly("first", "second");
    }

    @Test
    void givenMergedDuplicateKeys_thenComponentsConcatenated() {
        var blueprints = new Blueprints.Builder(Map.of("item", List.of("first")))
            .build()
            .merged(Map.of("item", List.of("second")));
        assertThat(blueprints.components("item")).containsExactly("first", "second");
    }

    private static final class Mutable {

        private final int value;

        private Mutable(int value) {
            this.value = value;
        }
    }
}
