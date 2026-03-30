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

    public void arguments(java.util.List<Symbol> arguments) {
        int argSize = arguments.size();
        for (int i = 0; i < argSize; ++i) 
            checkPut(arguments.get(i), i - argSize);
    }

    public void locals(java.util.List<Symbol> locals) {
        int localSize = locals.size();
        for (int i = 0; i < localSize; ++i)
            checkPut(locals.get(i), i + 1);
    }
}
