package saka1029.stack;

import java.util.ListIterator;

public interface List extends Instruction, Collection {
    
    public static final List NIL = new List() {

        @Override
        public Iterator iterator() {
            return () -> null;
        }
        
        @Override
        public String toString() {
            return "()";
        }
    };
    
    public static List of(Instruction instruction) {
        return new List() {

            @Override
            public Iterator iterator() {
                return new Iterator() {
                    Instruction i = instruction;
                    @Override
                    public Instruction next() {
                        Instruction r = i;
                        i = null;
                        return r;
                    }
                };
            }

            @Override
            public String toString() {
                return "(%s)".formatted(instruction);
            }
        };
    }

    public static List of(Instruction... instructions) {
        int size = instructions.length;
        List result = NIL;
        for (int i = size - 1; i >= 0; --i)
            result = Cons.of(instructions[i], result);
        return result;
    }
    
    public static List of(java.util.List<Instruction> list) {
        List result = NIL;
        ListIterator<Instruction> it = list.listIterator(list.size());
        while (it.hasPrevious())
            result = Cons.of(it.previous(), result);
        return result;
    }

    @Override
    default void execute(Context context) {
        context.instructions.addLast(iterator());
    }
}
