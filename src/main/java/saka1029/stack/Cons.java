package saka1029.stack;

import java.util.ListIterator;

public class Cons extends List {

    public final Instruction car;
    public final List cdr;
    
    Cons(Instruction car, List cdr) {
        this.car = car;
        this.cdr = cdr;
    }
    
    public static Cons of(Instruction car, List cdr) {
        return new Cons(car, cdr);
    }

    @Override
    public Sequence sequence() {
        return new Sequence() {

            List list = Cons.this;

            @Override
            public Instruction next() {
                if (list instanceof Cons cons) {
                    list = cons.cdr;
                    return cons.car;
                }
                return null;
            }
        };
    }

    public static List of(java.util.List<Instruction> list) {
        List result = List.NIL;
        ListIterator<Instruction> it = list.listIterator(list.size());
        while (it.hasPrevious())
            result = of(it.previous(), result);
        return result;
    }

    public static List of(Instruction... instructions) {
        int size = instructions.length;
        List result = List.NIL;
        for (int i = size - 1; i >= 0; --i)
            result = of(instructions[i], result);
        return result;
    }
}
