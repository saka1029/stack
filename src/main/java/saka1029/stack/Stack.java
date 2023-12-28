package saka1029.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import saka1029.Common;

public class Stack {
    
    static final Logger logger = Common.logger(Stack.class);
    
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
    
    static boolean b(Instruction instruction) {
        return ((Bool)instruction).value;
    }
    
    static Bool b(boolean value) {
        return Bool.of(value);
    }

    static int i(Instruction instruction) {
        return ((Int)instruction).value;
    }
    
    static Int i(int value) {
        return Int.of(value);
    }
    
    static List l(Instruction instruction) {
        return ((List)instruction);
    }

    static Map<Symbol, Instruction> standard() {
        Map<Symbol, Instruction> vars = new HashMap<>();
        put(vars, "@0", Context::dup);
        put(vars, "@1", c -> c.dup(1));
        put(vars, "@2", c -> c.dup(2));
        put(vars, "drop", Context::drop);
        put(vars, "true", Bool.TRUE);
        put(vars, "false", Bool.FALSE);
        put(vars, "+", c -> c.push(i(i(c.pop()) + i(c.pop()))));
        put(vars, "-", c -> c.push(i(-i(c.pop()) + i(c.pop()))));
        put(vars, "print", c -> System.out.print(c.pop()));
        put(vars, "if", c -> {
            Instruction orElse = c.pop(), then = c.pop();
            if (b(c.pop()))
                c.execute(then);
            else
                c.execute(orElse);
        });
        put(vars, "for", c -> {
            Instruction closure = c.pop();
            List list = l(c.pop());
            Iterator it = list.iterator();
            Iterator result = new Iterator() {
                
                Instruction current = it.next();

                @Override
                public Instruction next() {
                    if (current == null)
                        return null;
                    Instruction i = current;
                    current = it.next();
                    return cc -> {
                        cc.push(i);
                        cc.execute(closure);
                    };
                }
            };
            c.pushInstruction(result);
        });
        put(vars, "range", c -> {
            int end = i(c.pop()), start = i(c.pop());
            c.push(Range.of(start, end));
        });
        return vars;
    }
}
