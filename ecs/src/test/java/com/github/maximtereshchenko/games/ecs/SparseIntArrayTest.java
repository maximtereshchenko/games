package com.github.maximtereshchenko.games.ecs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SparseIntArrayTest {

    @Test
    void givenEmptyArray_thenNull() {
        assertThat(new SparseIntArray().element(0)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenElementSet_thenElement() {
        var sparseArray = new SparseIntArray();
        sparseArray.set(0, 1);
        assertThat(sparseArray.element(0)).isOne();
    }

    @Test
    void givenPageCreated_thenElement() {
        var sparseArray = new SparseIntArray(1, 1);
        sparseArray.set(0, 1);
        sparseArray.set(1, 2);
        assertThat(sparseArray.element(1)).isEqualTo(2);
    }

    @Test
    void givenPageCreated_thenPageContainsEmptyValue() {
        var sparseArray = new SparseIntArray(1, 1);
        sparseArray.set(0, 1);
        assertThat(sparseArray.element(1)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenRequiredPageOutsideInitialSize_thenElement() {
        var sparseArray = new SparseIntArray(0, 1);
        sparseArray.set(1, 0);
        assertThat(sparseArray.element(1)).isZero();
    }

    @Test
    void givenRequiredPageOutsideCurrentPages_thenEmptyValue() {
        var sparseArray = new SparseIntArray(0, 1);
        assertThat(sparseArray.element(1)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenNoElement_thenNothingRemoved() {
        var sparseArray = new SparseIntArray();
        sparseArray.remove(0);
        assertThat(sparseArray.element(0)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenPageCreated_thenNothingRemoved() {
        var sparseArray = new SparseIntArray(1, 1);
        sparseArray.set(0, 1);
        sparseArray.remove(1);
        assertThat(sparseArray.element(1)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenElement_thenElementRemoved() {
        var sparseArray = new SparseIntArray();
        sparseArray.set(0, 1);
        sparseArray.remove(0);
        assertThat(sparseArray.element(0)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenRequiredPageOutsideCurrentPages_thenNothingRemoved() {
        var sparseArray = new SparseIntArray(0, 1);
        sparseArray.remove(1);
        assertThat(sparseArray.element(1)).isEqualTo(SparseIntArray.EMPTY_VALUE);
    }

    @Test
    void givenMultipleElements_thenElements() {
        var sparseArray = new SparseIntArray();
        sparseArray.set(0, 1);
        sparseArray.set(1, 2);
        assertThat(sparseArray.element(0)).isEqualTo(1);
        assertThat(sparseArray.element(1)).isEqualTo(2);
    }
}