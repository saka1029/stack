package saka1029;

import static saka1029.Iterables.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Stack {
	
	private Stack() {}
	
	public static class Context {
	    final Map<String, Value> globals = new HashMap<>();
		final Value[] stack;
		int sp = 0;
		
		Context(int stackSize) { this.stack = new Value[stackSize]; }
		
		public void push(Value v) { stack[sp++] = v; }
		public Value pop() { return stack[--sp]; }
		public Value peek(int offset) { return stack[sp - offset]; }
		public Value peek() { return peek(0); }
		
		public Value get(String name) { return globals.get(name); }
		public void put(String name, Evaluable body) { globals.put(name, new Word(name, body)); }

		@Override
		public String toString() {
		    return string("[", " ", "]", map(i -> stack[i], range(0, sp)));
		}
	}

	interface Evaluable {
		void eval(Context c);
	}
	
	interface Value extends Iterable<Value>, Evaluable {
	    @Override default void eval(Context c) { c.push(this); }
	    default void evlis(Context c) { eval(c); }
	    static RuntimeException error() { return new UnsupportedOperationException(); }
	    @Override default Iterator<Value> iterator() { throw error(); }
	    default Value plus(Value right) { throw error(); }
	    default Value minus(Value right) { throw error(); }
	    default Value div(Value right) { throw error(); }
	    default Value mult(Value right) { throw error(); }
	    default Value mod(Value right) { throw error(); }
	}
	
	public static class Int implements Value {
	    public final int value;
	    Int(int value) { this.value = value; }
	    @Override public int hashCode() { return value; }
	    @Override public boolean equals(Object obj) { return obj instanceof Int i && i.value == value; }
	    @Override public String toString() { return Integer.toString(value); }
	    @Override public Value plus(Value right) { return new Int(value + ((Int)right).value); }
	    @Override public Value minus(Value right) { return new Int(value - ((Int)right).value); }
	    @Override public Value div(Value right) { return new Int(value / ((Int)right).value); }
	    @Override public Value mult(Value right) { return new Int(value * ((Int)right).value); }
	    @Override public Value mod(Value right) { return new Int(value % ((Int)right).value); }
	}
	
	public static abstract class List implements Value {
	    public static final List NIL = new List() {
	    };
	    
	    public static List of(Value... values) {
	        List list = NIL;
	        for (int i = values.length - 1; i >= 0; --i)
	            list = new Cons(values[i], list);
	        return list;
	    }
	    
	    public static class Builder {
	        List first = NIL;
	        Cons last = null;
	        public Builder add(Value v) {
	            last = new Cons(v, NIL);
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
	    @Override public int hashCode() { return Objects.hash(car, cdr); }
	    @Override public boolean equals(Object obj) { return obj instanceof Cons c && c.car.equals(car) && c.cdr.equals(cdr); }
	}

	static class Word implements Value {
	    public final String name;
	    public final Evaluable body;
	    Word(String name, Evaluable body) { this.name = name; this.body = body; }
	    @Override public void eval(Context c) { body.eval(c); }
	    @Override public String toString() { return name; }
	}

	// static methods
    public static Int i(int i) { return new Int(i); }
    public static List list(Value... values) { return List.of(values); }
    public static List toList(Iterable<Value> it) {
        List.Builder builder = List.builder();
        for (Value v : it)
            builder.add(v);
        return builder.build();
    }
    public static Cons cons(Value a, Value b) { return new Cons(a, b); }
    public static final List NIL = List.NIL;
    public static Word word(String name, Evaluable body) { return new Word(name, body); }

}
