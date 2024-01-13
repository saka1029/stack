package saka1029.stack;

public class Generator implements List {

    final Context context;
    final List code;
    
    Generator(Context context, Instruction code, Instruction arg) {
        this.context = context;
        this.context.push(arg);
        this.code = code instanceof List list ? list : List.of(code);
    }
    
    public static Generator of(Context origin, Instruction code, Instruction arg) {
        return new Generator(origin.child(), code, arg);
    }
    
    @Override
    public Iterator iterator() {
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
        return "generator[%s]".formatted(code);
    }

}
