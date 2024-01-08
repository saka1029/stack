package saka1029.stack;

public class Generator implements List {

    final Context context;
    final List code;
    
    Generator(Context context, Instruction code) {
        this.context = context;
        this.code = code instanceof List list ? list : List.of(code);
    }
    
    public static Generator of(Context origin, Instruction list) {
        return new Generator(origin.child(), list);
    }
    
    class Iter implements Iterator {
        
        Instruction current;
        
        Iter() {
            context.execute(code);
            this.current = advance();
        }
        
        Instruction advance() {
            Terminal t = context.run();
            switch (t) {
                case END:
                    return null;
                case YIELD:
                    return context.pop();
                default:
                    throw new RuntimeException("Unsupported terminator '%s'".formatted(t));
            }
        }

        @Override
        public Instruction next() {
            Instruction result = current;
            if (result != null)
                current = advance();
            return result;
        }
    }

    @Override
    public Iterator iterator() {
        return new Iter();
    }
    
    @Override
    public String toString() {
        return "generator[%s]".formatted(code);
    }

}
