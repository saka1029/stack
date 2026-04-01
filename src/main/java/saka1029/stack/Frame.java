package saka1029.stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Frame {

    public final Map<Symbol, Integer> offsets = new HashMap<>();
    public final java.util.List<Symbol> arguments = new ArrayList<>();
    public final java.util.List<Symbol> results = new ArrayList<>();
    public final java.util.List<Symbol> locals = new ArrayList<>();
    public final int argSize, resultSize;

    void checkPut(Symbol symbol, int offset) {
        if (offsets.containsKey(symbol))
            throw new RuntimeException(
                "arguments or local variables duplicate '%s".formatted(symbol));
        offsets.put(symbol, offset);
    }

    public Frame(java.util.List<Symbol> arguments, java.util.List<Symbol> results) {
        this.argSize = arguments.size();
        for (int i = 0; i < argSize; ++i) {
            this.arguments.add(arguments.get(i));
            checkPut(arguments.get(i), i - this.argSize);
        }
        this.resultSize = results.size();
        for (Symbol result : results)
            this.results.add(result);
    }

    public void locals(java.util.List<Symbol> locals) {
        int localSize = locals.size();
        for (int i = 0; i < localSize; ++i) {
            this.locals.add(locals.get(i));
            checkPut(locals.get(i), i + 1);
        }
    }

    public Instruction frameStart() {
        return new Instruction() {

            @Override
            public void execute(Context context) {
                context.frameStart();
            }

            @Override
            public String toString() {
                return ": %s -> %s".formatted(
                    arguments.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining(" ")),
                    results.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining(" ")));
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
                // return "frameEnd(%d,%d)".formatted(argSize, resultSize);
                return "";
            }
        };
    }
}
