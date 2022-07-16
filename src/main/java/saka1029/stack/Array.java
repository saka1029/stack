package saka1029.stack;

import java.util.Arrays;
import java.util.Iterator;

public class Array extends Value {

	final Value[] elements;
	
	Array(Value... elements) {
		this.elements = elements.clone();
	}
	
	public static Array of(Value... elements) {
		return new Array(elements);
	}
	
	@Override
	public Int size() {
		return Int.of(elements.length);
	}
	
	@Override
	public Value at(Value index) {
		return elements[((Int)index).value];
	}
	
	@Override
	public Iterator<Value> iterator() {
		return Arrays.stream(elements).iterator();
	}
}
