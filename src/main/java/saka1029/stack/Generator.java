package saka1029.stack;

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
    
    /**
     * 
     * @param context
     * @param code
     * @param args 引数を逆順に指定します。
     *             これは以下のような書き方ができるようにするためです。
     *             <code>new Generator(code, c.pop(), c.pop())</code>
     */
    Generator(Context context, Instruction code, Instruction... args) {
        this.context = context;
        this.code = code instanceof List list ? list : Cons.list(code);
        this.args = reverse(args.clone());
    }
    
    public static Generator of(Context context, Instruction code, Instruction... args) {
        return new Generator(context, code, args);
    }

    @Override
    public Sequence sequence() {
        Context c = context.fork();
        for (Instruction i : args)
            c.push(i);
        c.execute(code);
        return () -> {
            Terminal t = c.run();
            return switch (t) {
                case END -> null;
                case YIELD -> c.pop();
                default -> throw new RuntimeException("Unsupported terminator '%s'".formatted(t));
            };
        };
    }
}
