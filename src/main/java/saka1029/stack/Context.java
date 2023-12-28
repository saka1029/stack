package saka1029.stack;

import java.util.ArrayList;

public class Context {
    
    private final java.util.List<Instruction> stack;
    private final java.util.List<Iterator> instructions;
    private final java.util.Map<Symbol, Instruction> variables;
    
    Context(java.util.Map<Symbol, Instruction> variables) {
        this.stack = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.variables = variables;
    }
    
    public static Context of(java.util.Map<Symbol, Instruction> variables) {
        return new Context(variables);
    }

    public int size() {
        return stack.size();
    }

    public void push(Instruction i) {
        stack.addLast(i);
    }
    
    public Instruction pop() {
        return stack.removeLast();
    }
    
    public void dup(int index) {
        push(stack.get(size() - index - 1));
    }
    
    public void dup() {
        dup(0);
    }
    
    public void drop() {
        stack.removeLast();
    }
    
    public void pushInstruction(Iterator it) {
        instructions.addLast(it);
    }
    
    public Iterator peekInstruction() {
        return instructions.getLast();
    }
    
    public Iterator popInstruction() {
        return instructions.removeLast();
    }
    
    public Instruction variable(Symbol s) {
        return variables.get(s);
    }
    
    public void execute(Instruction instruction) {
        instruction.execute(this);
    }

    Terminal run() {
        
        return Terminal.END;
    }

    public Terminal run(List instructions) {
        execute(instructions);
        return run();
    }
    
    @Override
    public String toString() {
        return stack.toString();
    }
}
