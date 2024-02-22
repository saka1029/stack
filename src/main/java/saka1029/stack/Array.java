package saka1029.stack;

import java.util.ArrayList;
import java.util.Arrays;

public class Array extends List {

    final Instruction[] array;

    Array(Instruction[] array) {
        this.array = array;
    }

    public static Array of(List list) {
        java.util.List<Instruction> x = new ArrayList<>();
        for (Instruction i : list)
            x.add(i);
        return new Array(x.toArray(Instruction[]::new));
    }

    public static Array of(int size, Instruction value) {
        Instruction[] array = new Instruction[size];
        Arrays.fill(array, value);
        return new Array(array);
    }

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
