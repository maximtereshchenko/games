package com.github.maximtereshchenko.ecs;

import java.util.Arrays;

final class DenseArray {

    private Object[] elements;
    private int size;

    DenseArray(int initialSize) {
        this.elements = new Object[initialSize];
        this.size = 0;
    }

    DenseArray() {
        this(16);
    }

    public Object element(int index) {
        if (index >= size) {
            return null;
        }
        return elements[index];
    }

    public int size() {
        return size;
    }

    Object remove(int index) {
        if (index >= size) {
            return null;
        }
        var last = elements[--size];
        elements[size] = null;
        if (index == size) {
            return null;
        }
        elements[index] = last;
        return last;
    }

    void set(int index, Object element) {
        while (index >= elements.length) {
            elements = Arrays.copyOf(elements, elements.length << 1);
        }
        elements[index] = element;
        if (index >= size) {
            size = index + 1;
        }
    }
}
