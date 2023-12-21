package saka1029.stack;

import java.util.Objects;

public class Int implements Comparable {
    
    public final int value;
    
    Int(int value) {
        this.value = value;
    }
    
    public static Int of(int value) {
        return new Int(value);
    }

    @Override
    public int compareTo(Instruction r) {
        return Integer.compare(value, ((Int)r).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Int i && value == i.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
