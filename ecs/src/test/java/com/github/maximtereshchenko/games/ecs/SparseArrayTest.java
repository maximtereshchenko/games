package com.github.maximtereshchenko.games.ecs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SparseArrayTest {

    @Test
    void givenEmptyArray_thenNull() {
        assertThat(new SparseArray<>().element(0)).isNull();
    }

    @Test
    void givenElementSet_thenElement() {
        var sparseArray = new SparseArray<String>();
        sparseArray.set(0, "value");
        assertThat(sparseArray.element(0)).isEqualTo("value");
    }

    @Test
    void givenPageCreated_thenElement() {
        var sparseArray = new SparseArray<String>(1, 1);
        sparseArray.set(0, "first");
        sparseArray.set(1, "second");
        assertThat(sparseArray.element(1)).isEqualTo("second");
    }

    @Test
    void givenRequiredPageOutsideInitialSize_thenElement() {
        var sparseArray = new SparseArray<String>(0, 1);
        sparseArray.set(1, "value");
        assertThat(sparseArray.element(1)).isEqualTo("value");
    }

    @Test
    void givenRequiredPageOutsideCurrentPages_thenNullElement() {
        var sparseArray = new SparseArray<String>(0, 1);
        assertThat(sparseArray.element(1)).isNull();
    }

    @Test
    void givenNoElement_thenNothingRemoved() {
        var sparseArray = new SparseArray<>();
        sparseArray.remove(0);
        assertThat(sparseArray.element(0)).isNull();
    }

    @Test
    void givenPageCreated_thenNothingRemoved() {
        var sparseArray = new SparseArray<String>(1, 1);
        sparseArray.set(0, "first");
        sparseArray.remove(1);
        assertThat(sparseArray.element(1)).isNull();
    }

    @Test
    void givenElement_thenElementRemoved() {
        var sparseArray = new SparseArray<String>();
        sparseArray.set(0, "value");
        sparseArray.remove(0);
        assertThat(sparseArray.element(0)).isNull();
    }

    @Test
    void givenRequiredPageOutsideCurrentPages_thenNothingRemoved() {
        var sparseArray = new SparseArray<String>(0, 1);
        sparseArray.remove(1);
        assertThat(sparseArray.element(1)).isNull();
    }

    @Test
    void givenMultipleElements_thenElements() {
        var sparseArray = new SparseArray<String>();
        sparseArray.set(0, "first");
        sparseArray.set(1, "second");
        assertThat(sparseArray.element(0)).isEqualTo("first");
        assertThat(sparseArray.element(1)).isEqualTo("second");
    }
}