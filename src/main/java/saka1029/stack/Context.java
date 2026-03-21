package saka1029.stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Context {
    
    public static final int STACK_SIZE = 500;
    public final Instruction[] stack;
    public int sp;
    final java.util.List<Sequence> instructions;
    final Map<Symbol, Instruction> variables;
    Consumer<String> output;
    
    Context(Map<Symbol, Instruction> variables, Consumer<String> output) {
        this.stack = new Instruction[STACK_SIZE];
        this.sp = 0;
        this.instructions = new ArrayList<>();
        this.variables = variables;
        this.output = output;
    }
    
    public static Context of(Map<Symbol, Instruction> variables, Consumer<String> output) {
        return new Context(variables, output);
    }
    
    public static Context of(Map<Symbol, Instruction> variables) {
        return new Context(variables, System.out::print);
    }
    
    public static Context of() {
        return new Context(new HashMap<Symbol, Instruction>(), System.out::print);
    }
    
    public Context fork() {
        return of(variables, output);
    }

    public void push(Instruction value) {
        stack[sp++] = value;
    }
    
    public Instruction pop() {
        return stack[--sp];
    }
    
    public Instruction peek(int index) {
        return stack[sp - 1 - index];
    }

    public void dup(int index) {
        push(peek(index));
    }
    
    public void dup() {
        dup(0);
    }
    
    public void swap() {
        var top = stack[sp - 1];
        stack[sp - 1] = stack[sp - 2];
        stack[sp - 2] = top;
    }
    
    public void drop() {
        drop(1);
    }
    
    public void drop(int n) {
        sp -= n;
    }
    
    /**
     * A, B, C -> B, C, A
     */
    public void rot() {
        Instruction temp = stack[sp - 3];
        stack[sp - 3] = stack[sp - 2];
        stack[sp - 2] = stack[sp - 1];
        stack[sp - 1] = temp;
    }
    
    /**
     * A, B, C -> C, A, B
     */
    public void rrot() {
        Instruction temp = stack[sp - 1];
        stack[sp - 1] = stack[sp - 2];
        stack[sp - 2] = stack[sp - 3];
        stack[sp - 3] = temp;
    }
    
    public void ret(int n) {
        Instruction top = pop();
        sp -= n;
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
        instructions.add(it);
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
            Sequence it = instructions.get(instructions.size() - 1);
            Instruction ins;
            L1: while ((ins = it.next()) != null) {
                int oldSize = instructions.size();
                execute(ins);
                if (sp > 0 && peek(0) instanceof Terminal terminal) {
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
                if (instructions.size() != oldSize || instructions.get(instructions.size() - 1) != it)
                    continue L0;
            }
            instructions.remove(instructions.size() - 1);
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
        int oldSp = sp;
        run(instructions);
        if (sp - 1 != oldSp)
            throw new RuntimeException("Illegal stack size %s".formatted(this));
        return pop();
    }
    
    @Override
    public String toString() {
        return "Context(sp=%d, stack=%s)".formatted(
            sp,
            Stream.of(stack)
                .limit(sp)
                .map(e -> "" + e)
                .collect(Collectors.joining(", ", "[", "]")));
    }
}
