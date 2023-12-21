package saka1029.stack;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(car, cdr);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Cons c && car.equals(c.car) && cdr.equals(c.cdr);
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
