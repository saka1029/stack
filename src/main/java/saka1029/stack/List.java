package saka1029.stack;

public interface List extends Instruction, Collection {
    
    public static final List NIL = new List() {

        @Override
        public Iter iter() {
            return () -> null;
        }
        
        @Override
        public String toString() {
            return "()";
        }
    };

    @Override
    default void execute(Context context) {
        context.codes.addLast(iter());
    }
}
