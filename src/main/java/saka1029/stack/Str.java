package saka1029.stack;

public class Str implements Ordered {
    
    public final String value;
    
    Str(String value) {
        this.value = value;
    }
    
    public static Str of(String value) {
        return new Str(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Str s && s.value.equals(value);
    }

	@Override
	public int compareTo(Ordered o) {
		return value.compareTo(((Str)o).value);
	}

    @Override
    public String toString() {
        return value;
    }
}
