package saka1029.stack;

public class Bool implements Comparable {
    
    public static final Bool TRUE = new Bool(true);
    public static final Bool FALSE = new Bool(false);

    public final boolean value;
    
    private Bool(boolean value) {
        this.value = value;
    }
    
    public static Bool of(boolean value) {
        return value ? TRUE : FALSE;
    }
    
    @Override
    public int compareTo(Instruction r) {
        return Boolean.compare(value, ((Bool)r).value);
    }
    
    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
