package saka1029.stack;

import java.util.Arrays;
import java.util.Objects;

public class Str extends Value {
	
	final int[] codePoints;
	
	Str(String value) {
		this.codePoints = value.codePoints().toArray();
	}
	
	Str(int[] codePoints) {
		this.codePoints = codePoints;
	}
	
	public static Str of(String value) {
		return new Str(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Str i && Arrays.equals(codePoints, i.codePoints);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(codePoints);
	}
	
	@Override
	public String toString() {
		return new String(codePoints, 0, codePoints.length);
	}
	
	@Override
	public Value plus(Value right) {
		if (right instanceof Str s) {
			int ll = codePoints.length, lr = s.codePoints.length;
			int[] n = Arrays.copyOf(codePoints, ll + lr);
			System.arraycopy(s.codePoints, 0, n, ll, lr);
			return new Str(n);
		} else
			throw error("+", right);
	}

}
