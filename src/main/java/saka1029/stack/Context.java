package saka1029.stack;

import java.util.ArrayList;
import java.util.Collections;

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
    
    public Instruction peek(int index) {
        return stack.get(size() - index - 1);
    }

    public void dup(int index) {
        push(peek(index));
    }
    
    public void dup() {
        dup(0);
    }
    
    public void swap() {
        int top = size() - 1, second = top - 1;
        Collections.swap(stack, top, second);
    }
    
    public void drop() {
        stack.removeLast();
    }
    
    public void instruction(Iterator it) {
        instructions.addLast(it);
    }
    
    public Instruction variable(Symbol s) {
        return variables.get(s);
    }
    
    public void variable(Symbol s, Instruction instruction) {
        variables.put(s, instruction);
    }
    
    public void execute(Instruction instruction) {
        instruction.execute(this);
    }

    Terminal run() {
        L0: while (!instructions.isEmpty()) {
            Iterator it = instructions.getLast();
            Instruction ins;
            while ((ins = it.next()) != null) {
                int oldSize = instructions.size();
                execute(ins);
                if (instructions.size() != oldSize || instructions.getLast() != it)
                    continue L0;
            }
            instructions.removeLast();
        }
        return Terminal.END;
    }

    Terminal run(List instructions) {
        execute(instructions);
        return run();
    }
    
    Instruction eval(List instructions) {
        int oldSize = size();
        run(instructions);
        if (size() - 1 != oldSize)
            throw new RuntimeException("Illegal stack size %s".formatted(this));
        return pop();
    }
    
    @Override
    public String toString() {
        return stack.toString();
    }
}
