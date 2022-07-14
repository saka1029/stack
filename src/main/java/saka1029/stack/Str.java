package saka1029.stack;

import java.util.Objects;

public class Str extends Value {
	
	public final String value;
	
	Str(String value) {
		this.value = value;
	}
	
	public static Str of(String value) {
		return new Str(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Str i && Objects.equals(value, i.value);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public Value plus(Value right) {
		return Str.of(value + right);
	}

}
