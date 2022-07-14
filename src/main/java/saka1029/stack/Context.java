package saka1029.stack;

public class Context {
	
	public int sp = 0;
	public final Value[] stack;
	
	public Context(int stackSize) {
		this.stack = new Value[stackSize];
	}
	
	public void push(Value value) { stack[sp++] = value; }
	public Value pop() { return stack[--sp]; }
	public Value peek() { return peek(0); }
	public Value peek(int index) { return stack[sp - index - 1]; }
}
