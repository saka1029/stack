package saka1029.stack;

import java.util.HashMap;

public class Symbol implements Instruction {

    static final java.util.Map<String, Symbol> symbols = new HashMap<>();

    public final String name;
    
    private Symbol(String name) {
        this.name = name;
    }

    public static Symbol of(String name) {
        return symbols.computeIfAbsent(name, k -> new Symbol(name));
    }

    @Override
    public void execute(Context context) {
        Instruction instruction = context.variables.get(this);
        if (instruction == null)
            throw new RuntimeException("Symbol '%s' undefined".formatted(name));
        instruction.execute(context);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
