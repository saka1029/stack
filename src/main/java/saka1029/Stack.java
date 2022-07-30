package saka1029;

import static saka1029.Iterables.*;

import java.io.Reader;
import java.util.Arrays;
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
		public void put(String name, Loadable body) { globals.put(name, new Word(name, body)); }

		@Override
		public String toString() {
		    return string("[", " ", "]", map(i -> stack[i], range(0, sp)));
		}
	}

	interface Loadable {
		void load(Context c);
	}
	
	interface Value extends Iterable<Value>, Loadable {
	    @Override default void load(Context c) { c.push(this); }
	    default void eval(Context c) { load(c); }
	    static RuntimeException error() { return new UnsupportedOperationException(); }
	    default Value not() { throw error(); }
	    default Value and(Value right) { throw error(); }
	    default Value or(Value right) { throw error(); }
	    default Value xor(Value right) { throw error(); }
	    default Value minus(Value right) { throw error(); }
	    default Value div(Value right) { throw error(); }
	    default Value mult(Value right) { throw error(); }
	    default Value mod(Value right) { throw error(); }
	    @Override default Iterator<Value> iterator() { throw error(); }
	    default Value size() { return new Int(count(this)); }
	    default Value plus(Value right) { throw error(); }
	    default Value car() { throw error(); }
	    default Value cdr() { throw error(); }
	    default Value cons(Value right) { return new Cons(this, right); }
	}
	
	public static class Bool implements Value {
	    public final boolean value;
	    public static final Bool TRUE = new Bool(true);
	    public static final Bool FALSE = new Bool(false);
	    Bool(boolean value) { this.value = value; }
	    @Override public int hashCode() { return value ? 1 : 0; }
	    @Override public boolean equals(Object obj) { return obj instanceof Bool b && b.value == value; }
	    static Bool bool(boolean v) { return v ? TRUE : FALSE; }
	    @Override public Value not() { return bool(!value); }
	    @Override public Value and(Value right) { return bool(value && ((Bool)right).value); }
	    @Override public Value or(Value right) { return bool(value || ((Bool)right).value); }
	    @Override public Value xor(Value right) { return bool(value ^ ((Bool)right).value); }
	    @Override public String toString() { return value ? "true" : "false"; }
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
	    
	    @Override public void eval(Context c) { for (Value v : this) v.load(c); }

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
	
	public static class Str implements Value {
	    public final int[] values;
	    Str(int[] codePoints) {
	        this.values = codePoints;
	    }
	    @Override public int hashCode() { return Arrays.hashCode(values); }
	    @Override public boolean equals(Object obj) { return obj instanceof Str s && Arrays.equals(s.values, values); }
	    @Override public String toString() { return new String(values, 0, values.length); }
	    @Override
	    public Iterator<Value> iterator() {
	        return Arrays.stream(values).mapToObj(c -> (Value)new Int(c)).iterator();
	    }
	}
	
	public static class Cons extends List {
	    public final Value car;
	    public Value cdr;
	    Cons(Value car, Value cdr) { this.car = car; this.cdr = cdr; }
	    @Override public int hashCode() { return Objects.hash(car, cdr); }
	    @Override public boolean equals(Object obj) { return obj instanceof Cons c && c.car.equals(car) && c.cdr.equals(cdr); }
	    @Override public Value car() { return car; }
	    @Override public Value cdr() { return cdr; }
	}

	static class Word implements Value {
	    public final String name;
	    public final Loadable body;
	    Word(String name, Loadable body) { this.name = name; this.body = body; }
	    @Override public void load(Context c) { body.load(c); }
	    @Override public String toString() { return name; }
	}
	
	public static class Parser {
	    final Reader reader;
	    Parser(Reader reader) { this.reader = reader; }
	    Value read() {
	        return null;
	    }
	}

	// static methods
	public static final Bool TRUE = Bool.TRUE;
	public static final Bool FALSE = Bool.FALSE;
    public static Int i(int i) { return new Int(i); }
    public static Str s(String s) { return new Str(s.codePoints().toArray()); }
    public static List list(Value... values) { return List.of(values); }
    public static List toList(Iterable<Value> it) {
        List.Builder builder = List.builder();
        for (Value v : it)
            builder.add(v);
        return builder.build();
    }
    public static Value car(Value a) { return a.car(); }
    public static Value cdr(Value a) { return a.cdr(); }
    public static Cons cons(Value a, Value b) { return new Cons(a, b); }
    public static final List NIL = List.NIL;
    public static Word word(String name, Loadable body) { return new Word(name, body); }

}
