package saka1029.stack;

import java.util.Arrays;

public class Context {
	
	public int sp = 0;
	public final Value[] stack;
	
	Context(int stackSize) {
		this.stack = new Value[stackSize];
	}
	
	public static Context of(int stackSize) {
		return new Context(stackSize);
	}
	
	public void push(Value value) { stack[sp++] = value; }
	public Value pop() { return stack[--sp]; }
	public Value peek() { return peek(0); }
	public Value peek(int index) { return stack[sp - index - 1]; }
	
	@Override
	public String toString() {
		return Arrays.toString(Arrays.copyOf(stack, sp));
	}
}
