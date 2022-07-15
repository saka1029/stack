package saka1029.stack;

public class Int extends Value {
	
	public final int value;
	
	Int(int value) {
		this.value = value;
	}
	
	public static Int of(int value) {
		return new Int(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Int i && value == i.value;
	}
	
	@Override
	public int hashCode() {
		return value;
	}
	
	@Override
	public String toString() {
		return "" + value;
	}
	
	@Override
	public Value plus(Value right) {
		if (right instanceof Int i)
            return Int.of(value + i.value);
		else
			throw error("+", right);
	}

}
