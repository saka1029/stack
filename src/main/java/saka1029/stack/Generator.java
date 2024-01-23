package saka1029.stack;

import java.util.Arrays;
import java.util.logging.Logger;

import saka1029.Common;

public class Generator extends List {
    
    static final Logger logger = Common.logger(Generator.class);
    
    static <T> T[] reverse(T[] args) {
        T temp;
        for (int i = 0, j = args.length - 1; i < j; ++i, --j) {
            temp = args[i];
            args[i] = args[j];
            args[j] = temp;
        }
        return args;
    }

    final Context context;
    final List code;
    final Instruction[] args;
    
    Generator(Context context, Instruction code, Instruction... args) {
        this.context = context;
        this.code = code instanceof List list ? list : List.of(code);
        this.args = reverse(args.clone());
    }
    
    public static Generator of(Context origin, Instruction code, Instruction... args) {
        return new Generator(origin.child(), code, args);
    }

    @Override
    public Iterator iterator() {
        context.clear();
        for (Instruction i : args)
            context.push(i);
        context.execute(code);
        return () -> {
            Terminal t = context.run();
            return switch (t) {
                case END -> null;
                case YIELD -> context.pop();
                default -> throw new RuntimeException("Unsupported terminator '%s'".formatted(t));
            };
        };
    }
    
    @Override
    public String toString() {
        return "generator[%s %s]".formatted(Arrays.toString(args), code);
    }
}
