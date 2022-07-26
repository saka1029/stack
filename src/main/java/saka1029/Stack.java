package saka1029;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

public class Stack {
	
	private Stack() {}
	
	public static class Context {
		final Value[] stack;
		int sp = 0;
		
		Context(int stackSize) { this.stack = new Value[stackSize]; }
		
		void push(Value v) { stack[sp++] = v; }
		Value pop() { return stack[--sp]; }
		Value peek(int offset) { return stack[sp - offset]; }
		Value peek() { return peek(0); }

		@Override
		public String toString() {
			return Arrays.stream(stack)
				.map(Value::toString)
				.collect(Collectors.joining(", ", "[", "]"));
		}
		
	}

	interface Evaluable {
		void eval(Context c);
	}
	
	interface Value extends Iterable<Value>, Evaluable {
	    @Override default void eval(Context c) { c.push(this); }
	    default void evlis(Context c) { eval(c); }
	    @Override default Iterator<Value> iterator() { throw new UnsupportedOperationException(); }
	}
	
	public static class Int implements Value {
	    public final int value;
	    Int(int value) { this.value = value; }
	    public static Int of(int value) { return new Int(value); }
	    @Override public int hashCode() { return value; }
	    @Override public boolean equals(Object obj) { return obj instanceof Int i && i.value == value; }
	    @Override public String toString() { return Integer.toString(value); }
	}
	
	public static abstract class List implements Value {
	    public static final List NIL = new List() {
	    };
	    
	    public static List of(Value... values) {
	        List list = NIL;
	        for (int i = values.length - 1; i >= 0; --i)
	            list = Cons.of(values[i], list);
	        return list;
	    }
	    
	    public static class Builder {
	        List first = NIL;
	        Cons last = null;
	        public Builder add(Value v) {
	            last = Cons.of(v, NIL);
	            if (first == NIL)
	                first = last;
	            else
	                ((Cons)first).cdr = last;
	            return this;
	        }
	        public List build() { return first; }
	    }

	    public static Builder builder() { return new Builder(); }
	    
	    @Override public void evlis(Context c) { for (Value v : this) v.eval(c); }

	    @Override
	    public Iterator<Value> iterator() {
	        return new Iterator<Value>() {
	            Value list = List.this;
                @Override public boolean hasNext() { return list != List.NIL; }
                @Override
                public Value next() {
                    Cons cons = (Cons)list;
                    Value result = cons.car;
                    list = cons.cdr;
                    return result;
                }
	        };
	    }
	    
	    @Override public String toString() { return Iterables.string("[", " ", "]", this); }
	}
	
	public static class Cons extends List {
	    public final Value car;
	    public Value cdr;
	    Cons(Value car, Value cdr) { this.car = car; this.cdr = cdr; }
	    public static Cons of(Value car, Value cdr) { return new Cons(car, cdr); }
	    @Override public int hashCode() { return Objects.hash(car, cdr); }
	    @Override
	    public boolean equals(Object obj) { return obj instanceof Cons c && c.car.equals(car) && c.cdr.equals(cdr); }
	}

}
