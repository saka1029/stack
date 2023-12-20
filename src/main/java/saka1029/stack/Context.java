package saka1029.stack;

import java.util.ArrayList;

public class Context {
    
    public final java.util.List<Instruction> stack;
    public final java.util.List<Iter> codes;
    public final java.util.Map<Symbol, Instruction> variables;
    
    Context(java.util.Map<Symbol, Instruction> variables) {
        this.stack = new ArrayList<>();
        this.codes = new ArrayList<>();
        this.variables = variables;
    }
    
    public Context of(java.util.Map<Symbol, Instruction> variables) {
        return new Context(variables);
    }

    public void push(Instruction i) {
        stack.addLast(i);
    }
    
    public Instruction pop() {
        return stack.removeLast();
    }
    
    public void pushCode(Iter it) {
        codes.addLast(it);
    }
    
    public Iter peekCode() {
        return codes.getLast();
    }
    
    public Iter popCode() {
        return codes.removeLast();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
