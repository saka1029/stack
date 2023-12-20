package saka1029.stack;

public class Cons implements List {

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
    public Iter iter() {
        return new Iter() {

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(car);
        for (List list = cdr; list instanceof Cons cons; list = cons.cdr)
            sb.append(" ").append(cons.car);
        sb.append(")");
        return sb.toString();
    }
}
