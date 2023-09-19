package saka1029.stack;

public class Int implements Ordered {
    
    public final int value;
    
    Int(int value) {
        this.value = value;
    }
    
    public static Int of(int value) {
        return new Int(value);
    }
    
    @Override
    public int hashCode() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Int i && i.value == value;
    }

    @Override
    public int compareTo(Ordered o) {
        return Integer.compare(value, ((Int)o).value);
    }

    @Override
    public String toString() {
        return "" + value;
    }

	public static final Int ZERO = Int.of(0);
	public static final Int ONE = Int.of(1);
	public static final Int MINUS_ONE = Int.of(-1);
	public static final Int TWO = Int.of(2);
	public static final Int THREE = Int.of(3);
}
