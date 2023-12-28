package saka1029.stack;

import java.util.HashMap;
import java.util.Map;

public class Stack {
    
    private Stack() {
    }

    public static Context context() {
        return Context.of(standard());
    }
    
    public static List read(String source) {
        return Parser.of(source).read();
    }

    public static Instruction eval(Context context, String source) {
        return context.eval(read(source));
    }

    static void put(Map<Symbol, Instruction> variables, String name, Instruction instruction) {
        variables.put(Symbol.of(name), instruction);
    }
    
    static int i(Instruction inst) {
        return ((Int)inst).value;
    }
    
    static Int i(int value) {
        return Int.of(value);
    }

    static Map<Symbol, Instruction> standard() {
        Map<Symbol, Instruction> vars = new HashMap<>();
        put(vars, "+", c -> c.push(i(i(c.pop()) + i(c.pop()))));
        put(vars, "-", c -> c.push(i(-i(c.pop()) + i(c.pop()))));
        return vars;
    }
}
