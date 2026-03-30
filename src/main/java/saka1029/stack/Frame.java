package saka1029.stack;

import java.util.HashMap;
import java.util.Map;

public class Frame {

    public final Map<Symbol, Integer> offsets = new HashMap<>();

    void checkPut(Symbol symbol, int offset) {
        if (offsets.containsKey(symbol))
            throw new RuntimeException(
                "arguments or local variables duplicate '%s".formatted(symbol));
        offsets.put(symbol, offset);
    }

    public Frame(java.util.List<Symbol> arguments, java.util.List<Symbol> locals) {
        int argSize = arguments.size();
        for (int i = 0; i < argSize; ++i) 
            checkPut(arguments.get(i), i - argSize);
        int localSize = locals.size();
        for (int i = 0; i < localSize; ++i)
            checkPut(locals.get(i), i + 1);
    }
}
