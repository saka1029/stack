package saka1029.stack;

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
    public String toString() {
        return Integer.toString(value);
    }
}
