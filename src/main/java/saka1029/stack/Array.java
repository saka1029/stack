package saka1029.stack;

import java.util.Arrays;

public class Array extends List {

    final Instruction[] array;

    Array(Instruction[] array, Instruction value) {
        this.array = array;
        Arrays.fill(array, value);
    }

    public static Array of(int size, Instruction value) {
        Instruction[] array = new Instruction[size];
        return new Array(array, value);
    }

    public int size() {
        return array.length;
    }

    public Instruction get(int index) {
        return array[index];
    }

    public void set(int index, Instruction value) {
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
