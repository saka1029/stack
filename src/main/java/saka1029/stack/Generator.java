package saka1029.stack;

public class Generator extends List {

    final Context context;
    final List code;
    
    Generator(Context context, Instruction code) {
        this.context = context;
        this.code = code instanceof List list ? list : List.of(code);
    }
    
    public static Generator of(Context origin, Instruction code) {
        return new Generator(origin.child(), code);
    }

    public static Generator of(Context origin, Instruction code, Instruction arg1) {
        Generator g = of(origin, code);
        g.context.push(arg1);
        return g;
    }

    public static Generator of(Context origin, Instruction code, Instruction arg2, Instruction arg1) {
        Generator g = of(origin, code);
        g.context.push(arg1);
        g.context.push(arg2);
        return g;
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
