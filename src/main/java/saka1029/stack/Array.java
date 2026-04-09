package saka1029.stack;

import java.util.Arrays;

public class Array extends List {

    final Instruction[] array;

    Array(Instruction[] array) {
        this.array = array;
    }

    public static Array of(List list) {
        Instruction[] elements = new Instruction[list.size()];
        int i = 0;
        for (Instruction e : list)
            elements[i++] = e;
        return new Array(elements);
    }

    public static Array of(Instruction... elements) {
        return new Array(elements.clone());
    }

    public static Array of(int size, Instruction value) {
        Instruction[] array = new Instruction[size];
        Arrays.fill(array, value);
        return new Array(array);
    }

    @Override
    public int size() {
        return array.length;
    }

    public Instruction at(int index) {
        return array[index];
    }

    public void put(int index, Instruction value) {
        array[index] = value;
    }

    @Override
    public Sequence sequence() {
        return new Sequence() {
            int i = 0;
            @Override
            public Instruction next() {
                return i < array.length ? array[i++] : null;
            }
        };
    }
}
