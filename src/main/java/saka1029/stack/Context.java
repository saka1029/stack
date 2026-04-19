package saka1029.stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Context {
    
    public static final int STACK_SIZE = 500;
    public final Instruction[] stack;
    public int sp, bp;
    final java.util.List<Sequence> instructions;
    final Map<Symbol, Instruction> variables;
    public Consumer<String> output;
    public boolean trace = false;
    
    Context(Map<Symbol, Instruction> variables, Consumer<String> output) {
        this.stack = new Instruction[STACK_SIZE];
        this.sp = 0;
        this.bp = 0;
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
        return new Context(new HashMap<Symbol, Instruction>(), System.out::println);
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
    
    public Instruction peek() {
        return peek(0);
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
    
    public void stack() {
        if (output != null)
            output.accept("%s%n".formatted(this));
    }

    public void print(Instruction i) {
        if (output != null)
            output.accept(i.toString());
    }
    
    public void println(Instruction i) {
        if (output != null)
            output.accept("%s%n".formatted(i));
    }

    public static int asInt(Instruction i) {
        return ((Int)i).value;
    }

    public void frameStart() {
        push(Int.of(bp));   // 旧bp
        bp = sp - 1;        // 新bpは旧bpを指す
    }

    public void loadLocal(int offset) {
        push(stack[bp + offset]);
    }

    /**
     * 戻り値の設定とbpの回復
     * @param artSize 引数の数
     * @param resultSize 戻り値の数
     */
    public void frameEnd(int argSize, int resultSize) {
        int argStart = bp - argSize;
        int oldBp = asInt(stack[bp]);
        int from = sp - resultSize;
        sp = argStart;
        for (int i = 0; i < resultSize; ++i)
            push(stack[from++]);
        bp = oldBp;
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

    String currentStack = "";
    Instruction currentInstruction = null;

    public Terminal run0() {
        L0: while (!instructions.isEmpty()) {
            Sequence it = instructions.get(instructions.size() - 1);
            L1: while ((currentInstruction = it.next()) != null) {
                int oldSize = instructions.size();
                currentStack = toString();
                execute(currentInstruction);
                if (trace)
                    output.accept("%s sp=%d bp=%d %s".formatted(
                        currentInstruction, sp, bp,
                        IntStream.range(0, sp)
                            .mapToObj(i -> stack[i].toString())
                            .collect(Collectors.joining(" ", "[", "]"))));
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
    
    // static RuntimeException error(ClassCastException ex) {
    //     Matcher m = CLASS_CAST_EXCEPTION.matcher(ex.getMessage());
    //     if (m.find())
    //         return new RuntimeException("Cast error: %s to %s"
    //             .formatted(m.group(1), m.group(2)), ex);
    //     else
    //         return new RuntimeException(ex);
    // }

    public Terminal run() {
        try {
            return run0();
        } catch (ClassCastException cce) {
            // throw error(cce);
            Matcher m = CLASS_CAST_EXCEPTION.matcher(cce.getMessage());
            if (m.find())
                throw new RuntimeException("Cast error: %s to %s, fail '%s' in %s"
                    .formatted(m.group(1), m.group(2),
                        currentInstruction, currentStack, cce));
            else
                throw new RuntimeException(cce);
        } catch (Exception ex) {
            throw new RuntimeException("Fail '%s' in %s"
                .formatted(currentInstruction, currentStack), ex);
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
        return "Context(sp=%d, bp=%d stack=%s)".formatted(
            sp,
            bp,
            Stream.of(stack)
                .limit(sp)
                .map(e -> "" + e)
                .collect(Collectors.joining(" ", "[", "]")));
    }
}
