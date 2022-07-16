package saka1029.stack;

import java.util.Iterator;

public abstract class Value implements Evaluable, Iterable<Value> {

	UnsupportedOperationException error() {
		return new UnsupportedOperationException();
	}

	UnsupportedOperationException error(String operator, Value right) {
		return new UnsupportedOperationException("%s %s %s".formatted(this, operator, right));
	}

	@Override
	public void eval(Context context) {
		context.push(this);
	}
	
	public void evlis(Context context) {
		eval(context);
	}
	
	// Collections
	public Int size() { throw error(); }
	public Value at(Value index) { throw error(); }
	@Override public Iterator<Value> iterator() { throw error(); }
	
	// Arithmetic
	public Value plus(Value right) { throw error(); }
}
