package saka1029.stack;

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
    public Iterator iterator() {
        return new Iterator() {

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
}
