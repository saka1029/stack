package saka1029.stack;

import java.util.ArrayList;

public class Context {
    
    public final java.util.List<Instruction> stack;
    public final java.util.List<Iterator> codes;
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
    
    public void pushCode(Iterator it) {
        codes.addLast(it);
    }
    
    public Iterator peekCode() {
        return codes.getLast();
    }
    
    public Iterator popCode() {
        return codes.removeLast();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
