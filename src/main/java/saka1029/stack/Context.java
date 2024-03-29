package saka1029.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Context {
    
    private final java.util.List<Instruction> stack;
    private final java.util.List<Sequence> instructions;
    private final java.util.Map<Symbol, Instruction> variables;
    private Consumer<String> output;
    
    Context(java.util.Map<Symbol, Instruction> variables, Consumer<String> output) {
        this.stack = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.variables = variables;
        this.output = output;
    }
    
    public static Context of(java.util.Map<Symbol, Instruction> variables, Consumer<String> output) {
        return new Context(variables, output);
    }
    
    public static Context of(java.util.Map<Symbol, Instruction> variables) {
        return new Context(variables, System.out::print);
    }
    
    public static Context of() {
        return new Context(new java.util.HashMap<Symbol, Instruction>(), System.out::print);
    }
    
    public Context fork() {
        return of(variables, output);
    }

    public int size() {
        return stack.size();
    }
    
    public void clear() {
        stack.clear();
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
    
    public void drop(int n) {
        for (int i = 0; i < n; ++i)
            drop();
    }
    
    public void rot() {
        int size = size();
        Instruction temp = stack.get(size - 3);
        stack.set(size - 3, stack.get(size - 2));
        stack.set(size - 2, stack.get(size - 1));
        stack.set(size - 1, temp);
    }
    
    public void rrot() {
        int size = size();
        Instruction temp = stack.get(size - 1);
        stack.set(size - 1, stack.get(size - 2));
        stack.set(size - 2, stack.get(size - 3));
        stack.set(size - 3, temp);
    }
    
    public void ret(int n) {
        Instruction top = stack.removeLast();
        for (int i = 0; i < n; ++i)
            stack.removeLast();
        push(top);
    }
    
    public void output(Consumer<String> output) {
        this.output = output;
    }
    
    public void print(Instruction i) {
        if (output != null)
            output.accept(i.toString());
    }
    
    public void println(Instruction i) {
        if (output != null)
            output.accept("%s%n".formatted(i));
    }

    public void instruction(Sequence it) {
        instructions.addLast(it);
    }
    
    public Instruction variable(Symbol s) {
        return variables.get(s);
    }
    
    public void variable(Symbol s, Instruction instruction) {
        variables.put(s, instruction);
    }
    
    public void variable(String name, Instruction instruction) {
        variables.put(Symbol.of(name), instruction);
    }
    
    public void execute(Instruction instruction) {
        instruction.execute(this);
    }

    public Terminal run0() {
        L0: while (!instructions.isEmpty()) {
            Sequence it = instructions.getLast();
            Instruction ins;
            L1: while ((ins = it.next()) != null) {
                int oldSize = instructions.size();
                execute(ins);
                if (size() != 0 && peek(0) instanceof Terminal terminal) {
                    drop(); // drop Terminal;
                    switch (terminal) {
                        case BREAK:
                            break L1;
                        case YIELD:
                            return Terminal.YIELD;
                        default:
                            throw new RuntimeException("unexpected terminal '%' found".formatted(terminal));
                    }
                }
                if (instructions.size() != oldSize || instructions.getLast() != it)
                    continue L0;
            }
            instructions.removeLast();
        }
        return Terminal.END;
    }

    
    static final Pattern CLASS_CAST_EXCEPTION = Pattern.compile(
        "class \\S*\\.(\\S+) cannot be cast to class \\S*\\.(\\S+).*");
    
    static RuntimeException error(ClassCastException ex) {
        Matcher m = CLASS_CAST_EXCEPTION.matcher(ex.getMessage());
        if (m.find())
            return new RuntimeException("Cast error: %s to %s"
                .formatted(m.group(1), m.group(2)), ex);
        else
            return new RuntimeException(ex);
    }

    public Terminal run() {
        try {
            return run0();
        } catch (ClassCastException ex) {
            throw error(ex);
        }
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
