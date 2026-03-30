package saka1029.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Frame {

    public final Map<Symbol, Integer> offsets = new HashMap<>();
    public final java.util.List<Symbol> arguments;
    public final java.util.List<Symbol> locals;
    public final int argSize, resultSize;

    void checkPut(Symbol symbol, int offset) {
        if (offsets.containsKey(symbol))
            throw new RuntimeException(
                "arguments or local variables duplicate '%s".formatted(symbol));
        offsets.put(symbol, offset);
    }

    public Frame(java.util.List<Symbol> arguments, java.util.List<Symbol> locals, int resultSize) {
        this.arguments = arguments;
        this.locals = locals;
        this.argSize = arguments.size();
        for (int i = 0; i < argSize; ++i) 
            checkPut(arguments.get(i), i - this.argSize);
        int localSize = locals.size();
        for (int i = 0; i < localSize; ++i)
            checkPut(locals.get(i), i + 1);
        this.resultSize = resultSize;
    }

    public Instruction frameStart() {
        return new Instruction() {

            @Override
            public void execute(Context context) {
                context.frameStart();
            }

            @Override
            public String toString() {
                return "frameStart(%s->%d)".formatted(
                    arguments.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining(",")), resultSize);
            }
        };
    }

    public Instruction frameEnd() {
        return new Instruction() {

            @Override
            public void execute(Context context) {
                context.frameEnd(argSize, resultSize);
            }

            @Override
            public String toString() {
                return "frameEnd(%d,%d)".formatted(argSize, resultSize);
            }
        };
    }
}
