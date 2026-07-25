package com.github.maximtereshchenko.ecs;

import java.util.Arrays;

final class DenseIntArray {

    static final int EMPTY_VALUE = -1;

    private int[] elements;
    private int size;

    DenseIntArray(int initialSize) {
        this.elements = new int[initialSize];
        this.size = 0;
        Arrays.fill(elements, EMPTY_VALUE);
    }

    DenseIntArray() {
        this(16);
    }

    int size() {
        return size;
    }

    int remove(int index) {
        if (index >= size) {
            return EMPTY_VALUE;
        }
        var last = elements[--size];
        elements[size] = EMPTY_VALUE;
        if (index == size) {
            return EMPTY_VALUE;
        }
        elements[index] = last;
        return last;
    }

    void set(int index, int element) {
        if (index >= elements.length) {
            int oldLength = elements.length;
            int newLength = oldLength << 1;
            while (index >= newLength) {
                newLength <<= 1;
            }
            elements = Arrays.copyOf(elements, newLength);
            Arrays.fill(elements, oldLength, newLength, EMPTY_VALUE);
        }
        elements[index] = element;
        if (index >= size) {
            size = index + 1;
        }
    }

    int element(int index) {
        if (index >= size) {
            return EMPTY_VALUE;
        }
        return elements[index];
    }
}
